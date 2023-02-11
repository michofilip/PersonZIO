package sandboxzio.db.repository.impl

import io.getquill.*
import io.getquill.jdbczio.Quill
import sandboxzio.db.Schema.given
import sandboxzio.db.model.PersonEntity
import sandboxzio.db.repository.PersonRepository
import zio.*

import java.sql.SQLException
import javax.sql.DataSource

case class PersonRepositoryImpl(private val dataSource: DataSource)
    extends PostgresZioJdbcContext(SnakeCase)
        with PersonRepository {

    override def findAll: Task[Seq[PersonEntity]] = run {
        query[PersonEntity]
    }.provideEnvironment(ZEnvironment(dataSource))

    override def findById(id: Int): Task[Option[PersonEntity]] = run {
        query[PersonEntity].filter(p => p.id == lift(id))
    }.map(_.headOption).provideEnvironment(ZEnvironment(dataSource))

    override def save(personEntity: PersonEntity): Task[PersonEntity] = {
        if (personEntity.id == 0) {
            run(insert(lift(personEntity)))
        } else {
            run(update(lift(personEntity)))
        }
    }.provideEnvironment(ZEnvironment(dataSource))

    override def delete(id: Int): ZIO[Any, SQLException, Long] = run {
        query[PersonEntity].filter(p => p.id == lift(id)).delete
    }.provideEnvironment(ZEnvironment(dataSource))

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