package perosnzio.db.repository.impl

import zio.stream.ZStream
import zio.{Task, ZEnvironment, ZIO}

import javax.sql.DataSource
import javax.xml.transform.stream.StreamResult
import scala.Conversion

trait DataSourceAutoProvider(dataSource: DataSource) {
    given[T]: Conversion[ZIO[DataSource, Any, T], Task[T]] = _.provideEnvironment(ZEnvironment(dataSource))

    given[T]: Conversion[ZStream[DataSource, Any, T], ZStream[Any, Throwable, T]] = _.provideEnvironment(ZEnvironment(dataSource))
}
