package tkngch.example_032020

import doobie.Transactor
import doobie.implicits._
import zio.{Task, ZIO}
import zio.interop.catz._

object Database {
  trait Service {
    def createTable: Task[Unit]
    def dropTable: Task[Unit]
    def select: Task[List[Pokemon]]
    def select(id: Int): Task[Pokemon]
    def select(name: String): Task[Option[Pokemon]]
    def insert(pokemon: Pokemon): Task[Unit]
    def insert(pokemons: List[Pokemon]): Task[Unit]
    def delete(id: Int): Task[Unit]
  }
}

trait Database {
  def database: Database.Service
}

object db {
  def createTable: ZIO[Database, Throwable, Unit] =
    ZIO.accessM(_.database.createTable)

  def dropTable: ZIO[Database, Throwable, Unit] =
    ZIO.accessM(_.database.dropTable)

  def select: ZIO[Database, Throwable, List[Pokemon]] =
    ZIO.accessM(_.database.select)

  def select(name: String): ZIO[Database, Throwable, Option[Pokemon]] =
    ZIO.accessM(_.database.select(name))

  def select(id: Int): ZIO[Database, Throwable, Pokemon] =
    ZIO.accessM(_.database.select(id))

  def insert(pokemon: Pokemon): ZIO[Database, Throwable, Unit] =
    ZIO.accessM(_.database.insert(pokemon))

  def insert(pokemons: List[Pokemon]): ZIO[Database, Throwable, Unit] =
    ZIO.accessM(_.database.insert(pokemons))

  def delete(id: Int): ZIO[Database, Throwable, Unit] =
    ZIO.accessM(_.database.delete(id))
}

trait DatabaseLive extends Database {

  protected def tnx: Transactor[Task] = Transactor.fromDriverManager[Task](
    "org.sqlite.JDBC",
    "jdbc:sqlite:pokemon.db",
    "",
    ""
  )

  def database: Database.Service =
    new Database.Service {
      def createTable: Task[Unit] =
        sql"""
      CREATE TABLE IF NOT EXISTS
      Pokemon (
        id int PRIMARY KEY,
        num text,
        name text,
        img text,
        height text,
        weight text,
        candy text,
        candy_count int,
        egg text,
        spawn_chance real,
        avg_spawns real,
        spawn_time text)
      """.update.run
          .transact(tnx)
          .foldM(err => Task.fail(err), _ => Task.succeed(()))

      def dropTable: Task[Unit] =
        sql"""
      DROP TABLE IF EXISTS Pokemon
      """.update.run
          .transact(tnx)
          .foldM(err => {
            println(err)
            Task.fail(err)
          }, _ => Task.succeed(()))

      def select: Task[List[Pokemon]] =
        sql"""
      SELECT * FROM Pokemon
      """.query[Pokemon]
          .to[List]
          .transact(tnx)
          .foldM(err => Task.fail(err), pokemons => Task.succeed(pokemons))

      def select(name: String): Task[Option[Pokemon]] =
        sql"""
      SELECT * FROM Pokemon WHERE name = $name
      """.query[Pokemon]
          .option
          .transact(tnx)
          .foldM(
            err => Task.fail(err),
            option => Task.succeed(option)
          )

      def select(id: Int): Task[Pokemon] =
        sql"""
      SELECT * FROM Pokemon WHERE id = $id
      """.query[Pokemon]
          .option
          .transact(tnx)
          .foldM(
            err => Task.fail(err),
            option =>
              option match {
                case Some(pokemon) => ZIO.succeed(pokemon)
                case None          => ZIO.fail(new Throwable)
              }
          )

      def insert(pokemon: Pokemon): Task[Unit] =
        sql"""
      INSERT INTO Pokemon (
        id,
        num,
        name,
        img,
        height,
        weight,
        candy,
        candy_count,
        egg,
        spawn_chance,
        avg_spawns,
        spawn_time
      ) VALUES (
        ${pokemon.id},
        ${pokemon.num},
        ${pokemon.name},
        ${pokemon.img},
        ${pokemon.height},
        ${pokemon.weight},
        ${pokemon.candy},
        ${pokemon.candy_count},
        ${pokemon.egg},
        ${pokemon.spawn_chance},
        ${pokemon.avg_spawns},
        ${pokemon.spawn_time}
      )
      """.update.run
          .transact(tnx)
          .foldM(err => Task.fail(err), _ => Task.succeed(()))

      def insert(pokemons: List[Pokemon]): Task[Unit] =
        pokemons match {
          case h :: t => insert(h).zipRight(insert(t))
          case Nil    => Task.succeed(())
        }

      def delete(id: Int): Task[Unit] =
        sql"""DELETE FROM Pokemon WHERE id = $id""".update.run
          .transact(tnx)
          .unit
          .orDie
    }
}
object DatabaseLive extends DatabaseLive
