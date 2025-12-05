package ascii.controller.handlers

/**
 * Central registry mapping command names to their corresponding execution handlers.
 * * This acts as the link between the parsed command strings and the application logic.
 */
class CommandRegistry {

  private val handlers: Map[String, CommandHandler] = Map(
    "image"          -> new ImageSourceHandler(),
    "image-random"   -> new RandomImageHandler(),
    "scale"          -> new ScaleFilterHandler(),
    "invert"         -> new InvertFilterHandler(),
    "brightness"     -> new BrightnessFilterHandler(),
    "flip"           -> new FlipFilterHandler(),
    "output-console" -> new ConsoleOutputHandler(),
    "output-file"    -> new FileOutputHandler(),
    "table"          -> new AsciiTableHandler(),
    "custom-table"   -> new CustomLinearTableHandler()
  )

  /**
   * Retrieves the handler associated with a specific command name.
   *
   * @param name The command name (e.g., "image", "scale").
   * @return Some(handler) if found, None otherwise.
   */
  def getHandler(name: String): Option[CommandHandler] = handlers.get(name)
}