/*
 * Copyright (c) 2020 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.podcastlooper.ploop.ploopserver.models

import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe._
import io.circe.generic.semiauto._
import io.circe.refined._

/**
  * A simple model for our hello world channel.
  *
  * @param title    A generic title.
  * @param headings Some header which might be presented prominently to the user.
  * @param message  A message for the user.
  */
case class Channel(title: GreetingTitle, headings: GreetingHeader, message: GreetingMessage)


object Channel {

  implicit val decoder: Decoder[Channel] = deriveDecoder[Channel]
  implicit val encoder: Encoder[Channel] = deriveEncoder[Channel]

  def fromName(verb: NonEmptyString, name: NonEmptyString): Channel = {
    val title = NonEmptyString.unsafeFrom(s"$verb $name")
    val headings = NonEmptyString.unsafeFrom(s"$verb $name, live long and prosper!")
    new Channel(
      title,
      headings,
      "This is a fancy message directly from http4s! :-)"
    )
  }

}