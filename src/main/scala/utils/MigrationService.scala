package utils

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.Configuration

trait MigrationService extends MainConfig {

  private val flyway = Flyway
    .configure()
    .dataSource(
      config.database.url,
      config.database.user,
      config.database.password
    )
    .load();

  def migrate() = {
    flyway.migrate()
  }

  def reloadSchema() = {
    flyway.clean()
    flyway.migrate()
  }
}
