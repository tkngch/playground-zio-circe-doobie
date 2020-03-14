package tkngch.example_032020

import zio.{Runtime}
import zio.console.{putStrLn}

object Main extends App {

  def write =
    for {
      jsonString <- Pokedex.jsonString
      pokemons <- Pokemon.fromJSON(jsonString)
      _ <- db.dropTable
      _ <- db.createTable
      _ <- db.insert(pokemons)
    } yield ()

  def printPikachu =
    for {
      maybePikachu <- selectProgram
      _ <- printPokemon(maybePikachu)
    } yield ()

  def selectPikachu =
    for {
      maybePikachu <- db.select("Pikachu")
    } yield maybePikachu

  def printPokemon(maybe: Option[Pokemon]) =
    maybe match {
      case Some(pokemon) => putStrLn(pokemon.toString)
      case None          => putStrLn("Pokemon not found")
    }

  def writeProgram = write.provide(DatabaseLive)
  def selectProgram = selectPikachu.provide(DatabaseLive)

  val runtime = Runtime.default
  runtime.unsafeRun(writeProgram.catchAll(err => putStrLn(err.toString)))
  runtime.unsafeRun(printPikachu.catchAll(err => putStrLn(err.toString)))
}
