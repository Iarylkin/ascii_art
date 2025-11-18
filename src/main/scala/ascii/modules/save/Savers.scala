package ascii.modules.save

import ascii.model.AsciiImageData
import java.io.{File, PrintWriter}

trait Saver {
  def save(data: AsciiImageData): Unit
}

abstract class AbstractSaver extends Saver {
  protected def open(): Unit = {}
  protected def close(): Unit = {}
  protected def writeChar(c: Char): Unit
  protected def writeNewLine(): Unit

  final override def save(data: AsciiImageData): Unit = {
    open()
    try {
      for (i <- 0 until data.content.length) {
        writeChar(data.content.charAt(i))
        if ((i + 1) % data.width == 0)
          writeNewLine()
      }
    } finally {
      close()
    }
  }
}

class ConsoleSaver extends AbstractSaver {
  override protected def writeChar(c: Char): Unit = print(c)
  override protected def writeNewLine(): Unit = println()
}

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
