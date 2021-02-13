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
import org.podcastlooper.ploop.ploopserver.models._
import eu.timepit.refined.auto._
//import eu.timepit.refined.cats._
//import eu.timepit.refined.types.string.NonEmptyString
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import sttp.model._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.codec.refined._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s._
//import slick.jdbc.SQLiteProfile.api._

final class ChannelManager[F[_]: Concurrent: ContextShift: Sync: Timer] extends Http4sDsl[F] {
//  val db = Database.forConfig("database")

  implicit def decodeChannel: EntityDecoder[F, Channel] = jsonOf
  implicit def encodeChannel: EntityEncoder[F, Channel] = jsonEncoderOf

  private val getChannel: HttpRoutes[F] = Http4sServerInterpreter.toRoutes(ChannelManager.channels) { _ =>
    //noinspection ScalaUnnecessaryParentheses
//    val channel = (
//      NonEmptyString.from(name.show).toOption,
//      ).map { case (n) =>
//      Channel.fromName("Hello", n)
//    }
//    Sync[F].delay(channel.fold(StatusCode.BadRequest.asLeft[Channel])(_.asRight[StatusCode]))
    Sync[F].delay(Either.right(List()))
  }

  val routes: HttpRoutes[F] = getChannel
}

object ChannelManager {
  val channels: Endpoint[Unit, StatusCode, List[Channel], Any] =
    endpoint.get
      .in("channels")
//      .in(query[NonEmptyString]("name"))
      .errorOut(statusCode)
      .out(jsonBody[List[Channel]].description("Return the channels stored"))
      .description(
        "Returns a JSON representation of the channel stored in the database."
      )
}
