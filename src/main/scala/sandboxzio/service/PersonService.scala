package sandboxzio.service

import sandboxzio.dto.Person
import sandboxzio.form.PersonForm
import zio.{Task, ZIO}

trait PersonService {
    def findAll: Task[Seq[Person]]

    def findById(id: Int): Task[Person]

    def create(personForm: PersonForm): Task[Person]

    def update(id: Int, personForm: PersonForm): Task[Person]

    def delete(id: Int): Task[Unit]
}
