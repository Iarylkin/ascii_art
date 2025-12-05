package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.load.RandomImageSource

/**
 * Handles the configuration for generating a random noise image.
 */
class RandomImageHandler extends CommandHandler {

  /**
   * Configures the pipeline to use a RandomImageSource.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.withImageSource(new RandomImageSource())
  }
}