package circefx

import cats._
import cats.data._
import cats.implicits._
import fx.{_, given}
import io.circe._
import io.circe.parser._


object Circefx:

  def decodeTest[E, A](c: io.circe.HCursor)(
      using d: Decoder[A],
      cx: Control[NonEmptyList[E] | DecodingFailure]): A =
    d(c).bind

  def parsing[E, A](s: String)(
      using cx: Control[NonEmptyList[E] | ParsingFailure | DecodingFailure],
      d: Decoder[A]): A =
    val parsed = parse(s).bind
    parsed.as[A].bind
