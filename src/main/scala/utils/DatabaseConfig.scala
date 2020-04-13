package utils

import io.getquill._

import scala.io.Source

trait DatabaseConfig {
  implicit lazy val ctx = new MysqlJdbcContext(SnakeCase, "ctx")
}
