package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.BrightnessFilter

/**
 * Handles the configuration of the brightness filter.
 *
 */
class BrightnessFilterHandler extends CommandHandler {

  /**
   * Parses the brightness delta and adds a BrightnessFilter to the pipeline.
   * Throws an exception if the parameter is missing or not a valid integer.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val delta = cmd.params.headOption.flatMap(_.toIntOption)
      .getOrElse(throw new IllegalArgumentException("Invalid brightness delta"))

    builder.addFilter(new BrightnessFilter(delta))
  }
}