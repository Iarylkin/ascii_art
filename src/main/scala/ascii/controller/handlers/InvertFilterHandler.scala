package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.filter.InvertFilter

/**
 * Handles the configuration of the invert (negative) filter.
 */
class InvertFilterHandler extends CommandHandler {

  /**
   * Adds an InvertFilter to the pipeline.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.addFilter(new InvertFilter())
  }
}