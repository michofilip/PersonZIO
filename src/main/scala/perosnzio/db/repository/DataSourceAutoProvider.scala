package perosnzio.db.repository

import zio.stream.ZStream
import zio.{Task, ZEnvironment, ZIO, ZLayer}

import java.sql.SQLException
import javax.sql.DataSource
import javax.xml.transform.stream.StreamResult
import scala.Conversion

private[repository] trait DataSourceAutoProvider {

    protected val dataSource: DataSource
    
    inline given[T]: Conversion[ZIO[DataSource, SQLException, T], Task[T]] = _.provideLayer(ZLayer.succeed(dataSource))

    inline given[T]: Conversion[ZStream[DataSource, SQLException, T], ZStream[Any, Throwable, T]] = _.provideLayer(ZLayer.succeed(dataSource))
}
