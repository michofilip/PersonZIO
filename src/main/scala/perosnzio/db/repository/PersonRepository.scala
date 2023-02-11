package perosnzio.db.repository

import perosnzio.db.model.PersonEntity
import zio.Task

trait PersonRepository {
    def findAll: Task[Seq[PersonEntity]]

    def findById(id: Int): Task[Option[PersonEntity]]

    def save(personEntity: PersonEntity): Task[PersonEntity]

    def delete(id: Int): Task[Long]
}