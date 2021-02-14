/*
 * Copyright (c) 2020 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.podcastlooper.ploop.ploopserver.api

import cats.effect._
import cats.implicits._
import com.typesafe.config.ConfigFactory
import org.podcastlooper.ploop.ploopserver.models._
import eu.timepit.refined.auto._
import org.podcastlooper.ploop.ploopserver.config.DatabaseConfig
import pureconfig.ConfigSource
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import sttp.model._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts

final class ChannelManager[F[_]: Concurrent: ContextShift: Sync: Timer] extends Http4sDsl[F] {
  implicit def decodeChannel: EntityDecoder[F, Channel] = jsonOf
  implicit def encodeChannel: EntityEncoder[F, Channel] = jsonEncoderOf

  val getChannels: HttpRoutes[F] = Http4sServerInterpreter.toRoutes(ChannelManager.channels) { _ =>
    val statement = sql"select * from channels".query[Channel].accumulate[List]
    val databaseChannels = getFilteredChannels(statement)
    val decoratedChannels = databaseChannels.map(DecoratedChannel.fromChannel(_, "toto.html"))
    Sync[F].delay(Either.right(decoratedChannels))
  }

  val getChannelItems: HttpRoutes[F] = Http4sServerInterpreter.toRoutes(ChannelManager.channelItems) { channelById: ChannelById =>
    val statement = sql"select * from items where channelId = ${channelById.id}".query[Item].accumulate[List]
    println(statement)
    val decoratedItems = List()
//    val databaseChannels = getFilteredChannels(statement)
//    val decoratedChannels = databaseChannels.map(DecoratedChannel.fromChannel(_, "toto.html"))
    Sync[F].delay(Either.right(decoratedItems))
  }

  private def getFilteredChannels(statement: doobie.ConnectionIO[List[Channel]]) = {
    // We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
    // is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

    val io = for {
      config <- IO(ConfigFactory.load(getClass.getClassLoader))
      dbConfig <- IO(ConfigSource.fromConfig(config).at(DatabaseConfig.CONFIG_KEY).loadOrThrow[DatabaseConfig])
      xa = Transactor.fromDriverManager[IO](
        dbConfig.driver,
        dbConfig.url,
        dbConfig.user,
        dbConfig.pass,
        Blocker.liftExecutionContext(ExecutionContexts.synchronous)
      )
      io <- statement.transact(xa)
    } yield io

    io.unsafeRunSync()
  }

  val routes: HttpRoutes[F] = getChannels <+> getChannelItems
}

case class ChannelById(id: Int)

object ChannelManager {
  val channels: Endpoint[Unit, StatusCode, List[DecoratedChannel], Any] =
    endpoint.get
      .in("channels")
//      .in(query[NonEmptyString]("name"))
      .errorOut(statusCode)
      .out(jsonBody[List[DecoratedChannel]].description("Return the channels stored"))
      .description(
        "Returns a JSON representation of the channel stored in the database."
      )

  val channelItems: Endpoint[ChannelById, StatusCode, List[DecoratedItem], Any] =
    endpoint.get
      .in(("channels" / path[Int]("channelId") / "items").mapTo(ChannelById))
      .errorOut(statusCode)
      .out(jsonBody[List[DecoratedItem]].description("Return the item related to the channel with ID channelID"))
      .description(
        "Returns a JSON representation of the items stored in the database for a given channel."
      )
}
