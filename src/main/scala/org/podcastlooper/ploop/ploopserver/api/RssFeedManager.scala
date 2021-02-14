package org.podcastlooper.ploop.ploopserver.api

import cats.effect._
import cats.implicits._
import com.typesafe.config.ConfigFactory
import org.podcastlooper.ploop.ploopserver.models._
import org.http4s._
import org.http4s.dsl._
import doobie.Transactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import org.podcastlooper.ploop.ploopserver.config.DatabaseConfig
import eu.timepit.refined.auto._
import pureconfig.ConfigSource
import sttp.model.StatusCode
import sttp.tapir.json.circe.jsonBody
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.server.http4s._


final class RssFeedManager[F[_] : Concurrent : ContextShift : Sync : Timer] extends Http4sDsl[F] {
  //  implicit def decodeRssFeed: EntityDecoder[F, Channel] = jsonOf
  //  implicit def encodeRssFeed: EntityEncoder[F, Channel] = jsonEncoderOf

  val getChannelRssFeed: HttpRoutes[F] = Http4sServerInterpreter.toRoutes(RssFeedManager.rssFeed) {
    channelById: ChannelById => {
      val decoratedChannelWithItems = getChannelInfo(channelById.id) match {
        case (channel: Channel, items: List[Item]) =>
          DecoratedChannelWithItems.fromChannel(channel, "toto.html", items)
      }

      //    val databaseChannels = getFilteredChannels(statement)
      //    val decoratedChannels = databaseChannels.map(DecoratedChannel.fromChannel(_, "toto.html"))
      Sync[F].delay(Either.right(decoratedChannelWithItems))
    }
  }

  def getChannelInfo(channelId: Int): (Channel, List[Item]) = {
    val channelSearch = sql"select * from channels where id = $channelId".query[Channel].option
    val itemSearch = sql"select * from items where channelId = $channelId".query[Item].accumulate[List]

    // We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
    // is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

    val requestIo = for {
      config <- IO(ConfigFactory.load(getClass.getClassLoader))
      dbConfig <- IO(ConfigSource.fromConfig(config).at(DatabaseConfig.CONFIG_KEY).loadOrThrow[DatabaseConfig])
      xa = Transactor.fromDriverManager[IO](
        dbConfig.driver,
        dbConfig.url,
        dbConfig.user,
        dbConfig.pass,
        Blocker.liftExecutionContext(ExecutionContexts.synchronous)
      )
      channelOption <- channelSearch.transact(xa)
      channel = channelOption.get
      channelItems <- itemSearch.transact(xa)
    } yield (channel, channelItems)

    requestIo.unsafeRunSync()
  }


  val routes: HttpRoutes[F] = getChannelRssFeed
}

object RssFeedManager {
  val rssFeed: Endpoint[ChannelById, StatusCode, DecoratedChannelWithItems, Any] =
    endpoint.get
      .in(("channels" / path[Int]("channelId") / "rss").mapTo(ChannelById))
      .errorOut(statusCode)
      .out(jsonBody[DecoratedChannelWithItems].description("Return the item related to the channel with ID channelID"))
      .description(
        "Returns a JSON representation of the items stored in the database for a given channel."
      )
}
