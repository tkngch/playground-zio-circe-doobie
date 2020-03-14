package tkngch.example_032020

import scala.io.Source
import scala.util.Try

import zio.{Task, ZIO}

object Pokedex {
  val filename = "pokedex.json"
  val jsonString: Task[String] =
    for {
      json <- ZIO.fromTry(
        Try(
          Source.fromResource("pokedex.json").getLines.mkString
        )
      )
    } yield json
}
