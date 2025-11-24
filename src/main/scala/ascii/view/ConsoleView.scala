package ascii.view

class ConsoleView extends View {
  override def showHelp(): Unit = {
    println("Usage: java -jar ascii.jar --image <path> [options]...")
    println("\nINPUT:")
    println("  --image <path>          Load image from file")
    println("  --image-random          Generate random RGB noise")
    println("\nFILTERS:")
    println("  --invert                Invert colors")
    println("  --scale <0.25|1|4>      Scale image")
    println("  --flip <x|y>            Flip horizontally (x) or vertically (y)")
    println("  --brightness <delta>    Adjust brightness (+/- int)")
    println("\nCONVERSION:")
    println("  --table <name>          Use preset table (linear, bourke-small, bourke, gamma)")
    println("  --custom-table <chars>  Use custom characters")
    println("\nOUTPUT:")
    println("  --output-console        Print to stdout (default)")
    println("  --output-file <path>    Save to file")
  }

  override def showError(msg: String): Unit = {
    System.err.println(s"ERROR: $msg")
  }

  override def showSuccess(): Unit = {
    println("Processing finished successfully.")
  }
}
