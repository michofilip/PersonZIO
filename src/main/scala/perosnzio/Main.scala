package perosnzio

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import perosnzio.Main.Environment
import perosnzio.config.{ApplicationConfig, DbConfig, HttpServerConfig, SLF4JConfig}
import perosnzio.controller.PersonController
import perosnzio.db.repository.impl.PersonRepositoryImpl
import perosnzio.service.PersonService
import perosnzio.service.impl.PersonServiceImpl
import zio.http.{Server, ServerConfig}
import zio.{Console, Scope, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}

object Main extends ZIOAppDefault {

    override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] = SLF4JConfig.layer

    private val layer = ZLayer.make[App & Server](
        App.layer,
        PersonController.layer,
        PersonServiceImpl.layer,
        PersonRepositoryImpl.layer,
        DbConfig.layer,
        ApplicationConfig.layer,
        HttpServerConfig.layer,
        ZLayer.Debug.mermaid
    )

    override def run: ZIO[Environment & ZIOAppArgs & Scope, Any, Any] =
        App.start.provideLayer(layer).exitCode

}
