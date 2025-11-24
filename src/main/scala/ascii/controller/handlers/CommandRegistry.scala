package ascii.controller.handlers

import ascii.controller.handlers.*

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

  def getHandler(name: String): Option[CommandHandler] = handlers.get(name)
}