package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.save.FileSaver

/**
 * Handles the configuration for saving the output to a file.
 */
class FileOutputHandler extends CommandHandler {

  /**
   * Extracts the file path and registers a FileSaver to the pipeline.
   * Throws an exception if the path argument is missing.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(throw new IllegalArgumentException("Output path missing"))
    builder.addSaver(new FileSaver(path))
  }
}