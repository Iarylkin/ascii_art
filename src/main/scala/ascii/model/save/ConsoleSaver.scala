package ascii.model.save

/**
 * Saves the ASCII image directly to the standard console.
 */
class ConsoleSaver extends AbstractSaver {

  /**
   * Prints the character to stdout without a newline.
   */
  override protected def writeChar(c: Char): Unit = print(c)

  /**
   * Prints a system-dependent newline.
   */
  override protected def writeNewLine(): Unit = println()
}