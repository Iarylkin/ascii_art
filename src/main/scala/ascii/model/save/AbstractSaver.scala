package ascii.model.save

import ascii.model.image.AsciiImageData

/**
 * Base class for savers using the Template Method pattern.
 *
 * It handles the iteration logic and resource management (open/close),
 * requiring subclasses only to implement the specific writing operations.
 */
abstract class AbstractSaver extends Saver {

  /** Hook method to prepare resources (e.g., open a file). Default is empty. */
  protected def open(): Unit = {}

  /** Hook method to release resources (e.g., close a file). Default is empty. */
  protected def close(): Unit = {}

  /** Defines how to write a single character. */
  protected def writeChar(c: Char): Unit

  /** Defines how to write a newline sequence. */
  protected def writeNewLine(): Unit

  /**
   * The template method that executes the saving process.
   *
   * 1. Opens resources.
   * 2. Iterates over pixels row by row.
   * 3. Writes characters and newlines.
   * 4. Ensures resources are closed in a finally block.
   */
  final override def save(data: AsciiImageData): Unit = {
    open()
    try {
      for (y <- 0 until data.height) {
        for (x <- 0 until data.width) {
          writeChar(data.pixel(x, y))
        }
        writeNewLine()
      }
    } finally {
      close()
    }
  }
}