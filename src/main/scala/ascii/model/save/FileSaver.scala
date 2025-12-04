package ascii.model.save

import java.io.{File, PrintWriter}

class FileSaver(path: String) extends AbstractSaver {
  private var writer: Option[PrintWriter] = None

  override protected def open(): Unit = {
    writer = Some(new PrintWriter(new File(path)))
  }

  override protected def close(): Unit = {
    writer.foreach(_.close())
  }

  override protected def writeChar(c: Char): Unit = writer.foreach(_.print(c))
  override protected def writeNewLine(): Unit = writer.foreach(_.println())
}
