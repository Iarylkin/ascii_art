package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.BrightnessFilter

class BrightnessFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val delta = cmd.params.headOption.flatMap(_.toIntOption)
      .getOrElse(throw new IllegalArgumentException("Invalid brightness delta"))
    builder.addFilter(new BrightnessFilter(delta))
  }
}
