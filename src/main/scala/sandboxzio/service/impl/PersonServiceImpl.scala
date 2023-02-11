package sandboxzio.service.impl

import sandboxzio.db.model.PersonEntity
import sandboxzio.db.repository.PersonRepository
import sandboxzio.dto.Person
import sandboxzio.exceptions.NotFoundException
import sandboxzio.form.PersonForm
import sandboxzio.service.PersonService
import sandboxzio.utils.Log
import zio.{Task, ZIO, ZLayer}

case class PersonServiceImpl(private val personRepository: PersonRepository) extends PersonService {

    override def findAll: Task[Seq[Person]] = {
        for
            people <- personRepository.findAll
        yield people.map(personFrom)
    } @@ Log.timed("PersonServiceImpl::findAll")

    override def findById(id: Int): Task[Person] = {
        getById(id).map(personFrom)
    } @@ Log.timed("PersonServiceImpl::findById")

    override def create(personForm: PersonForm): Task[Person] =
        personRepository.save(PersonEntity.from(personForm)).map(personFrom) @@ Log.timed("PersonServiceImpl::create")

    override def update(id: Int, personForm: PersonForm): Task[Person] = {
        for
            personEntity <- getById(id)
            personEntityUpdated <- personRepository.save(personEntity.updated(personForm))
        yield personFrom(personEntityUpdated)
    } @@ Log.timed("PersonServiceImpl::update")

    override def delete(id: Int): Task[Unit] = for
        _ <- getById(id)
        _ <- personRepository.delete(id)
    yield ()

    private def getById(id: Int): Task[PersonEntity] = personRepository.findById(id).flatMap {
        case Some(personEntity) => ZIO.succeed(personEntity)
        case None => ZIO.fail(NotFoundException(s"Person id: $id not found"))
    }

    private def personFrom(personEntity: PersonEntity) =
        Person(personEntity.id, personEntity.name, personEntity.age)
}


object PersonServiceImpl {
    lazy val layer = ZLayer.fromFunction(PersonServiceImpl.apply _)
}