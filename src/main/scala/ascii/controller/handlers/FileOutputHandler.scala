package ascii.controller.handlers

import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder
import ascii.model.save.FileSaver

class FileOutputHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(throw new IllegalArgumentException("Output path missing"))
    builder.addSaver(new FileSaver(path))
  }
}
