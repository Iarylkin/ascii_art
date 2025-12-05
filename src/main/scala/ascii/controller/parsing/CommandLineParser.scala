package ascii.controller.parsing

import scala.annotation.tailrec

/**
 * Parses raw command-line arguments into structured CommandDTOs.
 */
class CommandLineParser {

  // Maps "flag" -> ("internal command name", "number of required parameters")
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

  /**
   * Main entry point for parsing arguments.
   */
  def parse(args: List[String]): List[CommandDTO] = {
    parseRecursive(args, List.empty)
  }

  /**
   * Recursively consumes the argument list to build commands.
   */
  @tailrec
  private def parseRecursive(currentArgs: List[String], acc: List[CommandDTO]): List[CommandDTO] = {
    currentArgs match {
      case Nil => acc

      case head :: tail =>
        syntax.get(head) match {
          case Some((name, paramCount)) =>

            // Validation: Check if enough parameters exist
            if (tail.length < paramCount) {
              throw new IllegalArgumentException(s"Error: Argument '$head' requires $paramCount parameter(s).")
            }

            val params = tail.take(paramCount)

            // Validation: Ensure parameters are not other flags
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