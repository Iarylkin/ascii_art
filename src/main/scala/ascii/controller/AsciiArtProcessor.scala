package ascii.controller

import ascii.controller.handlers.CommandRegistry
import ascii.controller.parsing.CommandDTO
import ascii.model.PipelineBuilder

object AsciiArtProcessor {
  def run(commands: List[CommandDTO]): Unit = {
    val builder = new PipelineBuilder()
    val commandRegistry = new CommandRegistry()

    commands.foreach { cmd =>
      commandRegistry.getHandler(cmd.name) match {
        case Some(handler) => handler.handle(cmd, builder)
        case None => println(s"Warning: Unknown command '${cmd.name}' ignored.")
      }
    }

    builder.build().run()
  }
}
