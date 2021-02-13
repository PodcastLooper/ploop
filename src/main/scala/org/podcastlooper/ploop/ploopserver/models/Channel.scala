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
                   itunes_image: String,
                   language: String,
                   itunes_category: String,
                   itunes_explicit: String,
                   itunes_author: String,
                   link: String,
                   itunes_owner: String,
                   itunes_title: String,
                   itunes_type: String,
                   copyright: String,
                   itunes_new_feed_url: String,
                   itunes_block: String,
                   itunes_complete: String)

object Channel {
  implicit val decoder: Decoder[Channel] = deriveDecoder[Channel]
  implicit val encoder: Encoder[Channel] = deriveEncoder[Channel]
}
