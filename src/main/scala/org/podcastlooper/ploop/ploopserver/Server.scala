/*
 * Copyright (c) 2020 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.podcastlooper.ploop.ploopserver

import cats.effect._
import cats.implicits._
import com.typesafe.config._
import org.podcastlooper.ploop.ploopserver.api._
import org.podcastlooper.ploop.ploopserver.config._
import org.podcastlooper.ploop.ploopserver.db.FlywayDatabaseMigrator
import eu.timepit.refined.auto._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import pureconfig._
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.swagger.http4s.SwaggerHttp4s

import scala.concurrent.ExecutionContext

object Server extends IOApp.WithContext {
  val ec: ExecutionContext = ExecutionContext.global

  override protected def executionContextResource: Resource[SyncIO, ExecutionContext] = Resource.liftF(SyncIO(ec))

  override def run(args: List[String]): IO[ExitCode] = {
    val migrator = new FlywayDatabaseMigrator
    val program = for {
      config <- IO(ConfigFactory.load(getClass().getClassLoader()))
      dbConfig <- IO(
        ConfigSource.fromConfig(config).at(DatabaseConfig.CONFIG_KEY).loadOrThrow[DatabaseConfig]
      )
      serviceConfig <- IO(
        ConfigSource.fromConfig(config).at(ServiceConfig.CONFIG_KEY).loadOrThrow[ServiceConfig]
      )
      _ <- migrator.migrate(dbConfig.url, dbConfig.user, dbConfig.pass)
      helloWorldRoutes = new ChannelManager[IO]
      docs             = OpenAPIDocsInterpreter.toOpenAPI(List(ChannelManager.channels), "loop Server", "1.0.0")
      swagger          = new SwaggerHttp4s(docs.toYaml)
      routes           = helloWorldRoutes.routes <+> swagger.routes[IO]
      httpApp          = Router("/" -> routes).orNotFound
      server = BlazeServerBuilder[IO](ec)
        .bindHttp(serviceConfig.port, serviceConfig.ip)
        .withHttpApp(httpApp)
      fiber = server.serve.compile.drain.as(ExitCode.Success)
    } yield fiber
    program.attempt.unsafeRunSync() match {
      case Left(e) =>
        IO {
          e.printStackTrace()
          ExitCode.Error
        }
      case Right(s) => s
    }
  }

}
