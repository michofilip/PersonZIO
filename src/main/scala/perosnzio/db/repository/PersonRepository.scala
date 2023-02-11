package perosnzio.db.repository

import perosnzio.db.model.PersonEntity
import zio.Task
import zio.stream.ZStream

trait PersonRepository {
    def findAll: Task[Seq[PersonEntity]]

    def findAllStream: ZStream[Any, Throwable, PersonEntity]

    def findById(id: Int): Task[Option[PersonEntity]]

    def save(personEntity: PersonEntity): Task[PersonEntity]

    def delete(id: Int): Task[Long]
}