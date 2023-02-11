package perosnzio.db

import io.getquill.*
import perosnzio.db.model.PersonEntity

object Schema {
    inline given SchemaMeta[PersonEntity] = schemaMeta("person", _.id -> "id", _.name -> "name", _.age -> "age")

    inline given InsertMeta[PersonEntity] = insertMeta[PersonEntity](_.id)

    inline given UpdateMeta[PersonEntity] = updateMeta[PersonEntity](_.id)
}
