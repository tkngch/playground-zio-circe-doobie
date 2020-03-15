name := "playground-zio-circe-doobie"
version := "0.0.1"

serverConnectionType := ConnectionType.Tcp
serverPort := 4273

scalaVersion := "2.13.1"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",   // source files are in UTF-8
  "-deprecation",         // warn about use of deprecated APIs
  "-unchecked",           // warn about unchecked type parameters
  "-feature",             // warn about misused language features
  "-language:higherKinds",// allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint",               // enable handy linter warnings
  "-Xfatal-warnings",     // turn compiler warnings into errors
)


val circeExtrasVersion = "0.13.0"
val circeVersion = "0.12.3"
val doobieVersion = "0.8.8"
val sqliteJdbcVersion = "3.30.1"
val zioInteropCatsVersion = "2.0.0.0-RC12"
val zioVersion = "1.0.0-RC18-2"

libraryDependencies ++= Seq(
  // ZIO
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-interop-cats" % zioInteropCatsVersion,
  // Circe
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeExtrasVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  // doobie
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  // JDBC for SQLite
  "org.xerial" % "sqlite-jdbc" % sqliteJdbcVersion
)
