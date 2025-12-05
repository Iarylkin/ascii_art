package ascii.controller.parsing

/**
 * Data Transfer Object representing a parsed command.
 *
 * @param name   The internal name of the command (e.g., "image", "rotate").
 * @param params The list of arguments associated with this command.
 */
case class CommandDTO(name: String, params: List[String] = Nil)