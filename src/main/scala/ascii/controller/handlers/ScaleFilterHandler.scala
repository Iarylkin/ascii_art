package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.ScaleFilter

class ScaleFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val s = cmd.params.headOption.flatMap(_.toDoubleOption)
      .getOrElse(throw new IllegalArgumentException("Invalid scale"))
    builder.addFilter(new ScaleFilter(s))
  }
}
