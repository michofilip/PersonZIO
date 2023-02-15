package perosnzio

import perosnzio.config.ApplicationConfig
import perosnzio.controller.PersonController
import zio.http.{Http, Request, Server}
import zio.{ZIO, ZLayer}

case class App(private val personController: PersonController) {

    private val routes = personController.routes

    private def start = for
        _ <- ZIO.logInfo("Welcome to SandboxZIO")
        port <- Server.install(routes)
        _ <- ZIO.logInfo(s"Server started at port: $port")
        _ <- ZIO.never
    yield ()

}

object App {
    lazy val layer = ZLayer.derive[App]

    def start: ZIO[App & Server, Nothing, Unit] = ZIO.serviceWithZIO[App](_.start)
}
