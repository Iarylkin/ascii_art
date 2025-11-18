package ascii.process


object AsciiArtProcessor {
  def run(commands: List[CommandDTO]): Unit = {
    val builder = new PipelineBuilder()

    commands.foreach { cmd =>
      CommandRegistry.getHandler(cmd.name) match {
        case Some(handler) => handler.handle(cmd, builder)
        case None => println(s"Warning: Unknown command '${cmd.name}' ignored.")
      }
    }

    builder.build().run()
  }
}
