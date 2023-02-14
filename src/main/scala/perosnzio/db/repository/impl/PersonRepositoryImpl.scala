package perosnzio.db.repository.impl

import io.getquill.*
import io.getquill.jdbczio.Quill
import perosnzio.db.Schema.given
import perosnzio.db.model.PersonEntity
import perosnzio.db.repository.PersonRepository
import zio.*
import zio.stream.ZStream

import java.sql.SQLException
import javax.sql.DataSource

case class PersonRepositoryImpl(private val dataSource: DataSource)
    extends PostgresZioJdbcContext(SnakeCase)
        with PersonRepository
        with DataSourceAutoProvider(dataSource) {

    override def findAll: Task[Seq[PersonEntity]] = run {
        query[PersonEntity]
    }

    override def findAllStream: ZStream[Any, Throwable, PersonEntity] = stream {
        query[PersonEntity]
    }
//        .provideEnvironment(ZEnvironment(dataSource))

    override def findById(id: Int): Task[Option[PersonEntity]] = run {
        query[PersonEntity].filter(p => p.id == lift(id))
    }.map(_.headOption)

    override def save(personEntity: PersonEntity): Task[PersonEntity] = {
        if (personEntity.id == 0) {
            run(insert(lift(personEntity)))
        } else {
            run(update(lift(personEntity)))
        }
    }

    override def delete(id: Int): Task[Long] = run {
        query[PersonEntity].filter(p => p.id == lift(id)).delete
    }

    private inline def insert = quote { (personEntity: PersonEntity) =>
        query[PersonEntity].insertValue(personEntity).returning(p => p)
    }

    private inline def update = quote { (personEntity: PersonEntity) =>
        query[PersonEntity].filter(p => p.id == personEntity.id).updateValue(personEntity).returning(p => p)
    }

}

object PersonRepositoryImpl {
    lazy val layer = ZLayer.fromFunction(PersonRepositoryImpl.apply _)
}