package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.save.ConsoleSaver

/**
 * Handles the configuration for console output.
 */
class ConsoleOutputHandler extends CommandHandler {

  /**
   * Registers a ConsoleSaver to the pipeline.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.addSaver(new ConsoleSaver())
  }
}