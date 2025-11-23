package ascii.model.save

import ascii.model.image.AsciiImageData
import java.io.{File, PrintWriter}

trait Saver {
  def save(data: AsciiImageData): Unit
}


