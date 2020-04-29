package todo.mock

import io.getquill._
import io.getquill.context.Context
import io.getquill.context.monix.Runner
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.idiom.Idiom
import monix.execution.Scheduler
import todo.utils.DatabaseConfig

trait DatabaseConfigMock extends DatabaseConfig {
  override implicit lazy val ctx = new MysqlMonixJdbcContext(SnakeCase, "test", Runner.using(Scheduler.io()))
}
