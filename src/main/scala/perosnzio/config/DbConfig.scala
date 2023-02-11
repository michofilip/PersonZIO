package perosnzio.config

import io.getquill.jdbczio.Quill

object DbConfig {
    lazy val layer = Quill.DataSource.fromPrefix("PostgresDB")
}
