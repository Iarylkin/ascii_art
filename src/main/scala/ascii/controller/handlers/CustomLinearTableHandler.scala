package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.convert.AsciiConverter
import ascii.model.convert.tables.LinearTable

class CustomLinearTableHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {

    val chars = cmd.params.headOption
      .getOrElse(throw new IllegalArgumentException("Argument '--custom-table' requires characters."))

    val table = new LinearTable(chars)

    builder.withAsciiConverter(new AsciiConverter(table))
  }
}
