package sandboxzio.form

import zio.json.{DeriveJsonCodec, DeriveJsonEncoder, JsonCodec, JsonEncoder}

case class PersonForm(name: String, age: Int)

object PersonForm {
    given JsonCodec[PersonForm] = DeriveJsonCodec.gen
}
