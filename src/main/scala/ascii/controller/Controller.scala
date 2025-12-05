package ascii.controller

import ascii.controller.handlers.CommandRegistry
import ascii.controller.parsing.CommandLineParser
import ascii.model.PipelineBuilder
import ascii.view.View

/**
 * The main application controller.
 *
 * Orchestrates the application flow by:
 * 1. Parsing raw command-line arguments.
 * 2. Mapping parsed commands to specific handlers.
 * 3. Configuring the processing pipeline.
 * 4. Executing the pipeline and updating the view.
 *
 * @param view    The interface for communicating with the user.
 * @param builder The builder used to construct the processing pipeline (injectable for testing).
 */
class Controller(
                  view: View,
                  builder: PipelineBuilder = new PipelineBuilder()) {

  private val parser = new CommandLineParser()
  private val commandRegistry = new CommandRegistry

  /**
   * Entry point for processing user input.
   *
   * @param args Raw command-line arguments.
   */
  def run(args: Array[String]): Unit = {
    // If no arguments are provided, show usage instructions
    if (args.isEmpty) {
      view.showHelp()
      return
    }

    try {
      // 1. Parse arguments into structured commands
      val commands = parser.parse(args.toList)

      // 2. Map commands to handlers and configure the builder
      commands.foreach { cmd =>
        commandRegistry.getHandler(cmd.name) match {
          case Some(handler) => handler.handle(cmd, builder)
          case None => throw new IllegalArgumentException(s"Unknown command: ${cmd.name}")
        }
      }

      // 3. Build and execute the pipeline
      builder.build().run()

      // 4. Notify user of success
      view.showSuccess()

    } catch {
      case e: Exception =>
        // Gracefully handle any errors (parsing, I/O, validation)
        view.showError(e.getMessage)
    }
  }
}