package ascii.view

class ConsoleView extends View {
  override def showHelp(): Unit = {
    println("Usage: run --image <path> [options]...")
    println("Options: --rotate, --scale, --invert, --output-console, --output-file...")
  }

  override def showError(msg: String): Unit = {
    System.err.println(s"ERROR: $msg")
  }

  override def showSuccess(): Unit = {
    println("Processing finished successfully.")
  }
}
