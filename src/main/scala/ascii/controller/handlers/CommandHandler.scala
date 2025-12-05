package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder

/**
 * Interface for handling specific commands to configure the pipeline.
 */
trait CommandHandler {

  /**
   * Processes the command and updates the PipelineBuilder state.
   *
   * @param cmd     The parsed command data.
   * @param builder The shared builder instance to configure.
   */
  def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit
}