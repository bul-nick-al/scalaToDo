package todo.utils

import io.getquill._
import io.getquill.context.monix.Runner
import monix.execution.Scheduler

trait DatabaseConfig {
  implicit lazy val ctx =
    new MysqlMonixJdbcContext(SnakeCase, "database", Runner.using(Scheduler.io()))
}
