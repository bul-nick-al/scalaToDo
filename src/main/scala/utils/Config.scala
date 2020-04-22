package utils
import pureconfig._
import pureconfig.generic.auto._

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
    url: String,
    driver: String,
    user: String,
    password: String,
    numThreads: Int,
    maxConnections: Int,
    minConnections: Int,
    registerMbeans: Boolean
)

object MainConfig {
  val config: Config = ConfigSource.default.load[Config].getOrElse(backupConfig)

  val backupConfig: Config = Config(
    Http(
      interface = "0.0.0.0",
      port = 8086
    ),
    Database(
      url = "jdbc:mysql://localhost:3306/akka_react?useUnicode=true&characterEncoding=UTF-8",
      driver = "com.mysql.jdbc.Driver",
      user = "root",
      password = "",
      numThreads = 1,
      maxConnections = 2,
      minConnections = 1,
      registerMbeans = true
    )
  )
}
