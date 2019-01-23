
name := "project1"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.197" % Test,
  "com.h2database" % "h2" % "1.4.197",

  "com.typesafe.akka" %% "akka-actor" % "2.5.17",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.17" % Test,

  "com.typesafe.akka" %% "akka-http" % "10.1.5",

  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",

  "com.typesafe.akka" %% "akka-stream" % "2.5.17",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.17" % Test,

  "com.typesafe.play" % "play-json_2.11" % "2.4.6",
  "io.spray" %% "spray-json" % "1.3.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",
  "org.typelevel" %% "cats-core" % "1.1.0",

  "com.typesafe.slick" %% "slick-codegen" % "3.2.3",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",

  "io.circe" %% "circe-core" % "0.10.0",
  "io.circe" %% "circe-generic" % "0.10.0",
  "io.circe" %% "circe-generic-extras" % "0.10.0",
  "io.circe" %% "circe-parser" % "0.10.0",
  "io.circe" %% "circe-literal" % "0.10.0"
)

val circeVersion = "0.9.3"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)