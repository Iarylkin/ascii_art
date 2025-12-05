package ascii.model.save

import java.io.{File, PrintWriter}

/**
 * Saves the ASCII image to a text file.
 *
 * @param path The filesystem path where the file will be created or overwritten.
 */
class FileSaver(path: String) extends AbstractSaver {
  private var writer: Option[PrintWriter] = None

  /**
   * Opens the file for writing.
   */
  override protected def open(): Unit = {
    writer = Some(new PrintWriter(new File(path)))
  }

  /**
   * Closes the file to ensure data is flushed and resources released.
   */
  override protected def close(): Unit = {
    writer.foreach(_.close())
  }

  /**
   * Writes a character to the file.
   */
  override protected def writeChar(c: Char): Unit = writer.foreach(_.print(c))

  /**
   * Writes a newline to the file.
   */
  override protected def writeNewLine(): Unit = writer.foreach(_.println())
}