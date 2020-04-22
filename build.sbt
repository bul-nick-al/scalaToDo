name := "project"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.6.3",
  "com.typesafe.akka" %% "akka-http" % "10.1.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "1.1.2",
  "com.github.pureconfig" %% "pureconfig" % "0.12.3",
  "mysql" % "mysql-connector-java" % "8.0.17",
  "io.getquill" %% "todo.quill-jdbc" % "3.5.1",
  "io.getquill" %% "todo.quill-jdbc-monix" % "3.5.1",
  "org.flywaydb" % "flyway-core" % "6.3.2",
  "io.spray" %%  "spray-json" % "1.3.5",
  "org.scalatest" %% "scalatest" % "3.1.0" % Test,
)

enablePlugins(JavaAppPackaging)