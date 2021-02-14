package org.podcastlooper.ploop.ploopserver.api

import cats.effect._
import cats.implicits._
import org.podcastlooper.ploop.ploopserver.models._
import org.http4s._
import org.http4s.dsl._
import sttp.tapir.server.http4s._
import doobie.implicits._

final class RssFeedManager[F[_]: Concurrent: ContextShift: Sync: Timer] extends Http4sDsl[F] {
//  implicit def decodeRssFeed: EntityDecoder[F, Channel] = jsonOf
//  implicit def encodeRssFeed: EntityEncoder[F, Channel] = jsonEncoderOf

  val getChannelRssFeed: HttpRoutes[F] = Http4sServerInterpreter.toRoutes(ChannelManager.channelItems) {
    channelById: ChannelById => {

      val channelSearch = sql"select * from channels where id = ${channelById.id}".query[Item].option
      val itemSearch = sql"select * from items where channelId = ${channelById.id}".query[Item].accumulate[List]
      println(channelSearch)
      println(itemSearch)
      val decoratedItems = List()
      //    val databaseChannels = getFilteredChannels(statement)
      //    val decoratedChannels = databaseChannels.map(DecoratedChannel.fromChannel(_, "toto.html"))
      Sync[F].delay(Either.right(decoratedItems))
    }
  }

  val routes: HttpRoutes[F] = getChannelRssFeed
}
