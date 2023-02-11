package perosnzio.config

import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.{LogLevel, Runtime}

object SLF4JConfig {
    lazy val layer = Runtime.removeDefaultLoggers >>> SLF4J.slf4j(LogFormat.colored)
}
