name := "zio-circe-doobie-example"
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


val http4sVersion = "0.21.1"
val circeVersion = "0.12.3"
val zioVersion = "1.0.0-RC18-2"
val zioInteropCatsVersion = "2.0.0.0-RC12"
val doobieVersion = "0.8.8"

libraryDependencies ++= Seq(
  // ZIO
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-interop-cats" % zioInteropCatsVersion,
  // http4s
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  // Circe
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-generic-extras" % "0.13.0",
  "io.circe" %% "circe-parser" % circeVersion,
  // doobie
  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  //
  "org.xerial" % "sqlite-jdbc" % "3.30.1"
)
