package ascii.ui

import ascii.process.CommandDTO
import scala.annotation.tailrec

class CommandLineParser {

  private val syntax: Map[String, (String, Int)] = Map(
    "--image"          -> ("image", 1),
    "--image-random"   -> ("image-random", 0),
    "--table"          -> ("table", 1),
    "--custom-table"   -> ("custom-table", 1),
    "--scale"          -> ("scale", 1),
    "--rotate"         -> ("rotate", 1),
    "--invert"         -> ("invert", 0),
    "--brightness"     -> ("brightness", 1),
    "--flip"           -> ("flip", 1),
    "--output-console" -> ("output-console", 0),
    "--output-file"    -> ("output-file", 1)
  )

  def parse(args: List[String]): List[CommandDTO] = {
    parseRecursive(args, List.empty)
  }

  @tailrec
  private def parseRecursive(currentArgs: List[String], acc: List[CommandDTO]): List[CommandDTO] = {
    currentArgs match {
      case Nil => acc

      case head :: tail =>
        syntax.get(head) match {
          case Some((name, paramCount)) =>

            if (tail.length < paramCount) {
              throw new IllegalArgumentException(s"Error: Argument '$head' requires $paramCount parameter(s).")
            }

            val params = tail.take(paramCount)

            if (params.exists(_.startsWith("--"))) {
              throw new IllegalArgumentException(s"Error: Value for '$head' cannot be another flag.")
            }

            val cmd = CommandDTO(name, params)
            val remaining = tail.drop(paramCount)

            parseRecursive(remaining, acc :+ cmd)

          case None =>
            throw new IllegalArgumentException(s"Error: Unknown argument '$head'")
        }
    }
  }
}
