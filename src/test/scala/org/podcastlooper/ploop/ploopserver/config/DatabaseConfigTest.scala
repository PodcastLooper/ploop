/*
 * Copyright (c) 2020 Contributors as noted in the AUTHORS.md file
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.podcastlooper.ploop.ploopserver.config

import eu.timepit.refined.auto._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.must.Matchers
import com.typesafe.config.ConfigFactory
import pureconfig._

class DatabaseConfigTest extends AnyWordSpec with Matchers {

  "DatabaseConfig" when {
    "loading the default application.conf" must {
      "read the configuration correctly" in {
        val cfg = ConfigFactory.load(getClass().getClassLoader())
        ConfigSource.fromConfig(cfg).at(DatabaseConfig.CONFIG_KEY).load[DatabaseConfig] match {
          case Left(e)  => fail(e.toList.mkString(", "))
          case Right(_) => succeed
        }
      }
    }
  }

}