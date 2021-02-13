/*
 * Copyright (c) 2020 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.podcastlooper.ploop.ploopserver.api

import cats.effect._
import org.podcastlooper.ploop.ploopserver.BaseSpec
import org.podcastlooper.ploop.ploopserver.models._
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.server.Router


class ChannelManagerTest extends BaseSpec {
  implicit val contextShift: ContextShift[IO] = IO.contextShift(executionContext)
  implicit val timer: Timer[IO]               = IO.timer(executionContext)

  implicit def decodeChannel: EntityDecoder[IO, Channel] = jsonOf

  "HelloWorld" when {
    "parameter 'name' is missing" must {
      val expectedStatusCode = Status.BadRequest

      s"return $expectedStatusCode" in {
        Uri.fromString("/hello") match {
          case Left(_) =>
            fail("Could not generate valid URI!")
          case Right(u) =>
            def service: HttpRoutes[IO] = Router("/" -> new ChannelManager[IO].routes)
            val request = Request[IO](
              method = Method.GET,
              uri = u
            )
            val response = service.orNotFound.run(request)
            for {
              result <- response.unsafeToFuture()
              body   <- result.as[String].unsafeToFuture()
            } yield {
              result.status must be(expectedStatusCode)
              body must be("Invalid value for: query parameter name")
            }
        }
      }
    }

    "parameter 'name' is set" when {
      "parameter value is invalid" must {
        val expectedStatusCode = Status.BadRequest

        s"return $expectedStatusCode" in {
          Uri.fromString("/hello?name=") match {
            case Left(_) =>
              fail("Could not generate valid URI!")
            case Right(u) =>
              def service: HttpRoutes[IO] = Router("/" -> new ChannelManager[IO].routes)
              val request = Request[IO](
                method = Method.GET,
                uri = u
              )
              val response = service.orNotFound.run(request)
              for {
                result <- response.unsafeToFuture()
                body   <- result.as[String].unsafeToFuture()
              } yield {
                result.status must be(expectedStatusCode)
                body must be(
                  "Invalid value for: query parameter name (expected value to have length greater than or equal to 1, but was )"
                )
              }
          }
        }
      }

      "parameter value is valid" must {
        val expectedStatusCode = Status.Ok

        s"return $expectedStatusCode" in {
          val name: NonEmptyString = "Captain Kirk"
          val expectedChannel = Channel(
            title = "Hello Captain Kirk!",
            headings = "Hello Captain Kirk, live long and prosper!",
            message = "This is a fancy message directly from http4s! :-)"
          )
          Uri.fromString(Uri.encode(s"/hello?name=$name")) match {
            case Left(e) =>
              println(e)
              fail("Could not generate valid URI!")
            case Right(u) =>
              def service: HttpRoutes[IO] = Router("/" -> new ChannelManager[IO].routes)
              val request = Request[IO](
                method = Method.GET,
                uri = u
              )
              val response = service.orNotFound.run(request)
              for {
                result <- response.unsafeToFuture()
                body   <- result.as[Channel].unsafeToFuture()
              } yield {
                result.status must be(expectedStatusCode)
                body must be(expectedChannel)
              }
          }
        }
      }
    }
  }

}
