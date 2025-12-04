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
