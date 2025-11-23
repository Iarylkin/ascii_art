package ascii.model.save

class ConsoleSaver extends AbstractSaver {
  override protected def writeChar(c: Char): Unit = print(c)
  override protected def writeNewLine(): Unit = println()
}
