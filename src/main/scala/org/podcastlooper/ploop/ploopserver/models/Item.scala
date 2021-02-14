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
import shapeless.syntax.std.tuple._

case class Item(id: Int,
                channelId: Int,
                title: String,
                enclosure: Option[String],
                guid: Option[String],
                pubDate: Option[String],
                description: Option[String],
                itunes_duration: Option[String],
                itunes_image: Option[String],
                itunes_explicit: Option[String],
                itunes_title: Option[String],
                itunes_episode: Option[String],
                itunes_season: Option[String],
                itunes_episodeType: Option[String],
                itunes_block: Option[String])

case class DecoratedItem(id: Int,
                         channelId: Int,
                         title: String,
                         enclosure: Option[String],
                         guid: Option[String],
                         pubDate: Option[String],
                         description: Option[String],
                         itunes_duration: Option[String],
                         itunes_image: Option[String],
                         itunes_explicit: Option[String],
                         itunes_title: Option[String],
                         itunes_episode: Option[String],
                         itunes_season: Option[String],
                         itunes_episodeType: Option[String],
                         itunes_block: Option[String],
                         link: String)

object Item {
  implicit val decoder: Decoder[Item] = deriveDecoder[Item]
  implicit val encoder: Encoder[Item] = deriveEncoder[Item]
}

object DecoratedItem {
  implicit val decoder: Decoder[DecoratedItem] = deriveDecoder[DecoratedItem]
  implicit val encoder: Encoder[DecoratedItem] = deriveEncoder[DecoratedItem]

  def fromItem(item: Item, link: String): DecoratedItem = {
    val tupleToDecoratedItem = (DecoratedItem.apply _).tupled
    val arguments = Item.unapply(item).get :+ link
    tupleToDecoratedItem(arguments)
  }
}