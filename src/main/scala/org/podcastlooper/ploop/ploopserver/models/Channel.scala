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

case class Channel(id: Int,
                   title: String,
                   description: String,
                   language: String,
                   image: String,
                   category: String,
                   explicit: Boolean,
                   author: Option[String],
                   owner_name: Option[String],
                   owner_mail: Option[String],
                   podcast_type: Option[String],
                   copyright: Option[String],
                   new_feed_url: Option[String],
                   block: Option[Boolean],
                   complete: Option[Boolean],
                   link: Option[String])

case class DecoratedChannel(id: Int,
                            title: String,
                            description: String,
                            language: String,
                            image: String,
                            category: String,
                            explicit: Boolean,
                            author: Option[String],
                            ownerName: Option[String],
                            ownerMail: Option[String],
                            podcastType: Option[String],
                            copyright: Option[String],
                            newFeedUrl: Option[String],
                            block: Option[Boolean],
                            complete: Option[Boolean],
                            link: Option[String])

case class DecoratedChannelWithItems(id: Int,
                                     title: String,
                                     description: String,
                                     language: String,
                                     image: String,
                                     category: String,
                                     explicit: Boolean,
                                     author: Option[String],
                                     ownerName: Option[String],
                                     ownerMail: Option[String],
                                     podcastType: Option[String],
                                     copyright: Option[String],
                                     newFeedUrl: Option[String],
                                     block: Option[Boolean],
                                     complete: Option[Boolean],
                                     link: Option[String],
                                     items: List[DecoratedItem])

object Channel {
  implicit val decoder: Decoder[Channel] = deriveDecoder[Channel]
  implicit val encoder: Encoder[Channel] = deriveEncoder[Channel]
}

object DecoratedChannel {
  implicit val decoder: Decoder[DecoratedChannel] = deriveDecoder[DecoratedChannel]
  implicit val encoder: Encoder[DecoratedChannel] = deriveEncoder[DecoratedChannel]

  def fromChannel(channel: Channel): DecoratedChannel = {
    val tupleToDecoratedChannel = (DecoratedChannel.apply _).tupled
    val arguments = Channel.unapply(channel).get
    tupleToDecoratedChannel(arguments)
  }
}

object DecoratedChannelWithItems {
  implicit val decoder: Decoder[DecoratedChannelWithItems] = deriveDecoder[DecoratedChannelWithItems]
  implicit val encoder: Encoder[DecoratedChannelWithItems] = deriveEncoder[DecoratedChannelWithItems]

  def fromChannel(channel: Channel, items: List[Item]): DecoratedChannelWithItems = {
    val tupleToDecoratedChannelWithItems = (DecoratedChannelWithItems.apply _).tupled
    val decoratedItems = items.map(DecoratedItem.fromItem)
    val arguments = Channel.unapply(channel).get :+ decoratedItems
    tupleToDecoratedChannelWithItems(arguments)
  }
}