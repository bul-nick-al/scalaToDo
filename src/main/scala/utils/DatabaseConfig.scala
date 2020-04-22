package utils

import io.getquill._
import io.getquill.context.monix.Runner
import monix.execution.Scheduler

import scala.io.Source

trait DatabaseConfig {
  implicit lazy val ctx = new MysqlMonixJdbcContext(SnakeCase, "ctx", Runner.using(Scheduler.io()))
}
