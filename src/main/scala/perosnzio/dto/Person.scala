package perosnzio.dto

import perosnzio.db.model.PersonEntity
import zio.json.{DeriveJsonCodec, JsonCodec}

case class Person(id: Int, name: String, age: Int)

object Person {
    given JsonCodec[Person] = DeriveJsonCodec.gen

    def from(personEntity: PersonEntity): Person =
        Person(
            id = personEntity.id,
            name = personEntity.name,
            age = personEntity.age
        )
}
