package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.convert.AsciiConverter
import ascii.model.convert.tables.LinearTable

/**
 * Handles the configuration of a custom linear ASCII table.
 */
class CustomLinearTableHandler extends CommandHandler {

  /**
   * Creates a LinearTable using the provided character string and configures the converter.
   * Throws an exception if the character string is missing.
   */
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val chars = cmd.params.headOption
      .getOrElse(throw new IllegalArgumentException("Argument '--custom-table' requires characters."))

    val table = new LinearTable(chars)

    builder.withAsciiConverter(new AsciiConverter(table))
  }
}