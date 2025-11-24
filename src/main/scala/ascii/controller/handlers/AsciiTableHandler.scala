package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.convert.AsciiConverter
import ascii.model.convert.tables.TableRegistry

class AsciiTableHandler extends CommandHandler {
  private val registry = new TableRegistry()

  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val tableName = cmd.params.headOption
      .getOrElse(throw new IllegalArgumentException("Argument '--table' requires a name."))
    
    val table = registry.getTable(tableName)
      .getOrElse(throw new IllegalArgumentException(
        s"Unknown table '$tableName'. Available: ${registry.listAvailable.mkString(", ")}"
      ))
    
    builder.withAsciiConverter(new AsciiConverter(table))
  }
}
