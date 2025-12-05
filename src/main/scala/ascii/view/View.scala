package ascii.view

/**
 * Interface for user interaction and feedback.
 * Decouples the application logic from specific output mechanisms (e.g., Console).
 */
trait View {

  /**
   * Displays usage instructions and available commands to the user.
   */
  def showHelp(): Unit

  /**
   * Displays an error message.
   *
   * @param msg The error description.
   */
  def showError(msg: String): Unit

  /**
   * Indicates that the operation completed successfully.
   */
  def showSuccess(): Unit
}