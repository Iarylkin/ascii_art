package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder

trait CommandHandler {
  def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit
}