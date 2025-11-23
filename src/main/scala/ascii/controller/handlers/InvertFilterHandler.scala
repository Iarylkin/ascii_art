package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.InvertFilter

class InvertFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.addFilter(new InvertFilter())
  }
}
