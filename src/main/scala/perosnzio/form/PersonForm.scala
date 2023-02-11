package perosnzio.form

import perosnzio.exceptions.ValidationException
import zio.IO
import zio.json.{DeriveJsonCodec, DeriveJsonEncoder, JsonCodec, JsonEncoder}
import zio.prelude.{Validation, ZValidation}

case class PersonForm(name: String, age: Int)

object PersonForm {
    given JsonCodec[PersonForm] = DeriveJsonCodec.gen

    def validate(personForm: PersonForm): IO[ValidationException, PersonForm] =
        ZValidation.validateWith(
            validateName(personForm.name),
            validateAge(personForm.age)
        )(PersonForm.apply)
            .mapError(ValidationException.apply)
            .toZIO

    private def validateName(name: String): Validation[String, String] =
        if name.isBlank then
            ZValidation.fail("Name cannot be blank")
        else
            ZValidation.succeed(name)

    private def validateAge(age: Int): Validation[String, Int] =
        if age <= 0 then
            ZValidation.fail("Age must be positive")
        else
            ZValidation.succeed(age)

}
