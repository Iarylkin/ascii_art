package ascii.view

trait View {
  def showHelp(): Unit
  def showError(msg: String): Unit
  def showSuccess(): Unit
}
