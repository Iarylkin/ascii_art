package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.ScaleFilter

/**
 * Handles the configuration of the image resolution scaling filter.
 *
 */
class ScaleFilterHandler extends CommandHandler {

  /**
   * Parses the scale factor and adds a ScaleFilter to the pipeline.
   * Throws an exception if the parameter is missing or not a valid number.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val s = cmd.params.headOption.flatMap(_.toDoubleOption)
      .getOrElse(throw new IllegalArgumentException("Invalid scale"))

    builder.addFilter(new ScaleFilter(s))
  }
}