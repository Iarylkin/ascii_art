package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.FlipFilter

/**
 * Handles the configuration of the image flip filter.
 *
 * Checks for "x" and "y" arguments to determine the axis of reflection.
 */
class FlipFilterHandler extends CommandHandler {

  /**
   * analyzes the parameters and adds a FlipFilter to the pipeline.
   *
   * @throws IllegalArgumentException If neither "x" nor "y" is provided.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val flipX = cmd.params.contains("x")
    val flipY = cmd.params.contains("y")

    if (!flipX && !flipY) throw new IllegalArgumentException("Flip requires 'x' and/or 'y'")

    builder.addFilter(new FlipFilter(flipX, flipY))
  }
}