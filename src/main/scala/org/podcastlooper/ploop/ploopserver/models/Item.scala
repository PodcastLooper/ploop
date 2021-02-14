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

case class Item(id: Int,
                channelId: Int,
                title: String,
                enclosure: Option[String],
                guid: Option[String],
                pub_date: Option[String],
                description: Option[String],
                duration: Option[String],
                image: Option[String],
                explicit: Option[String],
                episode: Option[String],
                season: Option[String],
                episode_type: Option[String],
                block: Option[String],
                link: Option[String])

case class DecoratedItem(id: Int,
                         channelId: Int,
                         title: String,
                         enclosure: Option[String],
                         guid: Option[String],
                         pubDate: Option[String],
                         description: Option[String],
                         duration: Option[String],
                         image: Option[String],
                         explicit: Option[String],
                         episode: Option[String],
                         season: Option[String],
                         episodeType: Option[String],
                         block: Option[String],
                         link: Option[String])

object Item {
  implicit val decoder: Decoder[Item] = deriveDecoder[Item]
  implicit val encoder: Encoder[Item] = deriveEncoder[Item]
}

object DecoratedItem {
  implicit val decoder: Decoder[DecoratedItem] = deriveDecoder[DecoratedItem]
  implicit val encoder: Encoder[DecoratedItem] = deriveEncoder[DecoratedItem]

  def fromItem(item: Item): DecoratedItem = {
    val tupleToDecoratedItem = (DecoratedItem.apply _).tupled
    val arguments = Item.unapply(item).get
    tupleToDecoratedItem(arguments)
  }
}