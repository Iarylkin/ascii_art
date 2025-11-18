package ascii.model.save

import ascii.model.image.AsciiImageData

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
