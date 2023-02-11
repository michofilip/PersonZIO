package sandboxzio

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import sandboxzio.Main.Environment
import sandboxzio.config.{ApplicationConfig, DbConfig, HttpServerConfig, SLF4JConfig}
import sandboxzio.controller.PersonController
import sandboxzio.db.repository.impl.PersonRepositoryImpl
import sandboxzio.service.PersonService
import sandboxzio.service.impl.PersonServiceImpl
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
