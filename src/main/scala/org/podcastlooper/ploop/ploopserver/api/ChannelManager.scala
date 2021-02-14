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
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.podcastlooper.ploop.ploopserver.db.Repository
import sttp.model._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s._
final class ChannelManager[F[_]: Concurrent: ContextShift: Sync: Timer] extends Http4sDsl[F] {
  implicit def decodeChannel: EntityDecoder[F, Channel] = jsonOf
  implicit def encodeChannel: EntityEncoder[F, Channel] = jsonEncoderOf

  val getChannels: HttpRoutes[F] = Http4sServerInterpreter.toRoutes(ChannelManager.channels) { _ =>
    val databaseChannels = Repository.getAllChannels
    val decoratedChannels = databaseChannels.map(DecoratedChannel.fromChannel)
    Sync[F].delay(Either.right(decoratedChannels))
  }

  val getChannelItems: HttpRoutes[F] = Http4sServerInterpreter.toRoutes(ChannelManager.channelItems) { channelById: ChannelById =>
    val decoratedItems = Repository.getItemsByChannelId(channelById.id)
    Sync[F].delay(Either.right(decoratedItems))
  }

  val routes: HttpRoutes[F] = getChannels <+> getChannelItems
}

object ChannelManager {
  val channels: Endpoint[Unit, StatusCode, List[DecoratedChannel], Any] =
    endpoint.get
      .in("channels")
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
