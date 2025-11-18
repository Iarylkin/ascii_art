package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.load.FileImageSource

class ImageSourceHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(throw new IllegalArgumentException("Path missing"))
    builder.withImageSource(new FileImageSource(path))
  }
}