package perosnzio.config

import perosnzio.utils.ConfigUtils
import zio.config.*
import zio.config.magnolia.{Descriptor, descriptor}
import zio.config.typesafe.{TypesafeConfig, TypesafeConfigSource, configValueConfigDescriptor}
import zio.{URIO, ZIO, ZLayer}

case class ApplicationConfig(port: Int)

object ApplicationConfig {
    lazy val layer = ConfigUtils.fromPrefix[ApplicationConfig]("ApplicationConfig")
}
