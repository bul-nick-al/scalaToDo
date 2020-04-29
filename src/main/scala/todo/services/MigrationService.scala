package todo.services

import org.flywaydb.core.Flyway
import todo.utils.Config

class MigrationService(config: Config) {

  private val flyway = Flyway
    .configure()
    .dataSource(
      config.database.dataSource.url,
      config.database.dataSource.user,
      config.database.dataSource.password
    )
    .load()

  def migrate(): Int = {
    flyway.migrate()
  }

  def reloadSchema(): Int = {
    flyway.clean()
    flyway.migrate()
  }
}
