package tkngch.example_032020

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder
import io.circe.parser.decode
import zio.IO

case class Pokemon(
    id: Int,
    num: String,
    name: String,
    img: String,
    height: String,
    weight: String,
    candy: String,
    candy_count: Int = 0,
    egg: String,
    spawn_chance: Double,
    avg_spawns: Double,
    spawn_time: String
)

object Pokemon {
  implicit val config: Configuration = Configuration.default.withDefaults
  implicit val decoder: Decoder[Pokemon] = deriveConfiguredDecoder

  def fromJSON(json: String): IO[String, List[Pokemon]] =
    decode[Map[String, List[Pokemon]]](json) match {
      case Left(err)       => IO.fail(err.toString)
      case Right(pokemons) => IO.succeed(pokemons("pokemon"))
    }
}
