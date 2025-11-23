package ascii.model.save

import java.io.{File, PrintWriter}

class FileSaver(path: String) extends AbstractSaver {
  private var writer: PrintWriter = _

  override protected def open(): Unit = {
    writer = new PrintWriter(new File(path))
  }

  override protected def close(): Unit = {
    if (writer != null) writer.close()
  }

  override protected def writeChar(c: Char): Unit = writer.print(c)
  override protected def writeNewLine(): Unit = writer.println()
}
