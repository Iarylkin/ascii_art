package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.load.FileImageSource

/**
 * Handles the selection of the input image file.
 */
class ImageSourceHandler extends CommandHandler {

  /**
   * Extracts the file path and configures the pipeline to load the image from that file.
   * Throws an exception if the path argument is missing.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(throw new IllegalArgumentException("Path missing"))
    builder.withImageSource(new FileImageSource(path))
  }
}