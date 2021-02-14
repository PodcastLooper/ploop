package org.podcastlooper.ploop.ploopserver.db

import cats.effect.{Blocker, ContextShift, IO}
import com.typesafe.config.ConfigFactory
import doobie.Transactor
import doobie.util.ExecutionContexts
import org.podcastlooper.ploop.ploopserver.config.DatabaseConfig
import org.podcastlooper.ploop.ploopserver.models.{Channel, DecoratedItem, Item}
import pureconfig.ConfigSource

import doobie.implicits._

import eu.timepit.refined.auto._

class Repository {
}

object Repository {
  def getItemsByChannelId(channelId: Int): List[DecoratedItem] = {
    val statement = sql"select * from items where channelId = $channelId".query[Item].accumulate[List]
    println(statement)
    val decoratedItems = List()
    decoratedItems
  }

  def getAllChannels: List[Channel] = {
    val statement = sql"select * from channels".query[Channel].accumulate[List]

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
}
