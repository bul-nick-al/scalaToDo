package todo.utils
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.generic.ProductHint

import pureconfig.ConfigSource

case class Config(
    http: Http,
    database: Database
)

case class Http(
    interface: String,
    port: Int
)

case class Database(
    dataSourceClassName: String,
    dataSource: DataSource,
    connectionTimeout: Int
)

case class DataSource(
                       url: String,
                       user: String,
                       password: String,
                         cachePrepStmts: Boolean,
                         prepStmtCacheSize: Int,
                         prepStmtCacheSqlLimit: Int
                     )

object MainConfig {
  implicit def hint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))
  print(ConfigSource.default.load[Config])
  val config: Config = ConfigSource.default.load[Config].getOrElse(backupConfig)

  lazy val backupConfig: Config = Config(
    Http(
      interface = "0.0.0.0",
      port = 8086
    ),
    Database(
      dataSourceClassName = "com.mysql.cj.jdbc.MysqlDataSource",
       DataSource(
        url = "jdbc:mysql://localhost:3306/todo?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow",
        user = "root",
        password = "",
        cachePrepStmts = true,
        prepStmtCacheSize = 250,
        prepStmtCacheSqlLimit = 2048
      ),
      connectionTimeout = 3000
    )
  )
}
