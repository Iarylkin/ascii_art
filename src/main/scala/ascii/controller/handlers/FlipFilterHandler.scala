package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.FlipFilter

class FlipFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val flipX = cmd.params.contains("x")
    val flipY = cmd.params.contains("y")
    if (!flipX && !flipY) throw new IllegalArgumentException("Flip requires 'x' and/or 'y'")
    builder.addFilter(new FlipFilter(flipX, flipY))
  }
}