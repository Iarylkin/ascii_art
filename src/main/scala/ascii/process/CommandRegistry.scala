package ascii.process

object CommandRegistry {
  private val handlers: Map[String, CommandHandler] = Map(
    "image"          -> new ImageSourceHandler(),
    "image-random"   -> new RandomImageHandler(),
    "scale"          -> new ScaleFilterHandler(),
    "invert"         -> new InvertFilterHandler(),
    "brightness"     -> new BrightnessFilterHandler(),
    "flip"           -> new FlipFilterHandler(),
    "output-console" -> new ConsoleOutputHandler(),
    "output-file"    -> new FileOutputHandler()
  )

  def getHandler(name: String): Option[CommandHandler] = handlers.get(name.toLowerCase)
}