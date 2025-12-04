package ascii.controller

import ascii.controller.handlers.CommandRegistry
import ascii.controller.parsing.CommandLineParser
import ascii.model.PipelineBuilder
import ascii.view.View

class Controller(
  view: View,
  builder: PipelineBuilder = new PipelineBuilder()) {

  private val parser = new CommandLineParser()
  private val commandRegistry = new CommandRegistry

  def run(args: Array[String]): Unit = {
    if (args.isEmpty) {
      view.showHelp()
      return
    }

    try {
      val commands = parser.parse(args.toList)

      commands.foreach { cmd =>
        commandRegistry.getHandler(cmd.name) match {
          case Some(handler) => handler.handle(cmd, builder)
          case None => throw new IllegalArgumentException(s"Unknown command: ${cmd.name}")
        }
      }

      builder.build().run()

      view.showSuccess()

    } catch {
      case e: Exception =>
        view.showError(e.getMessage)
    }
  }
}
