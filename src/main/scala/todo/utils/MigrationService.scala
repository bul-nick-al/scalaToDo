package todo.utils

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.Configuration

class MigrationService(config: Config) {

  private val flyway = Flyway
    .configure()
    .dataSource(
      config.database.url,
      config.database.user,
      config.database.password
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
