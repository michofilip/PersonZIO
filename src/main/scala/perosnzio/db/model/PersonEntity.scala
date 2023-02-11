package perosnzio.db.model

import perosnzio.form.PersonForm

case class PersonEntity(name: String, age: Int, id: Int = 0) {
    def updated(personForm: PersonForm): PersonEntity =
        copy(
            name = personForm.name,
            age = personForm.age
        )
}

object PersonEntity {
    def from(personForm: PersonForm): PersonEntity =
        PersonEntity(
            name = personForm.name,
            age = personForm.age
        )
}
