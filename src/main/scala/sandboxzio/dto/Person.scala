package sandboxzio.dto

import zio.json.{DeriveJsonCodec, JsonCodec}

case class Person(id: Int, name: String, age: Int)

object Person {
    given JsonCodec[Person] = DeriveJsonCodec.gen
}
