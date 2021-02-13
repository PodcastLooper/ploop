/*
 * Copyright (c) 2020 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.podcastlooper.ploop.ploopserver.models

import io.circe._
import io.circe.generic.semiauto._

case class Channel(id: Int,
                   title: String,
                   description: String,
                   itunes_image: Option[String],
                   language: String,
                   itunes_category: Option[String],
                   itunes_explicit: Option[String],
                   itunes_author: Option[String],
                   link: Option[String],
                   itunes_owner: Option[String],
                   itunes_title: Option[String],
                   itunes_type: Option[String],
                   copyright: Option[String],
                   itunes_new_feed_url: Option[String],
                   itunes_block: Option[String],
                   itunes_complete: Option[String])

object Channel {
  implicit val decoder: Decoder[Channel] = deriveDecoder[Channel]
  implicit val encoder: Encoder[Channel] = deriveEncoder[Channel]
}
