/*
 * Copyright (c) 2020 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.podcastlooper.ploop.ploopserver

import java.util.concurrent.Executors

import com.typesafe.config._
import org.podcastlooper.ploop.ploopserver.config._
import eu.timepit.refined.auto._
import pureconfig._
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach }
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import cats.effect.Blocker

/**
  * A base class for our tests.
  */
abstract class BaseSpec
    extends AsyncWordSpec
    with Matchers
    with ScalaCheckPropertyChecks
    with BeforeAndAfterAll
    with BeforeAndAfterEach {

  protected val config        = ConfigFactory.load()
  protected val dbConfig      = ConfigSource.fromConfig(config).at("database").load[DatabaseConfig]
  protected val serviceConfig = ConfigSource.fromConfig(config).at("service").load[ServiceConfig]

}

object BaseSpec {
  val blockingPool = Executors.newFixedThreadPool(2)
  val blocker      = Blocker.liftExecutorService(blockingPool)
}
