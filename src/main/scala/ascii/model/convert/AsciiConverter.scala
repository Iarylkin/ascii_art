package ascii.model.convert

import ascii.model.convert.tables.AsciiTable
import ascii.model.image.{AsciiImageData, GrayscaleImageData}

class AsciiConverter(table: AsciiTable) extends Converter[GrayscaleImageData, AsciiImageData] {
  override def convert(input: GrayscaleImageData): AsciiImageData = {
    val sb = new StringBuilder
    val pixels = input.pixels
    val w = input.width
    val h = input.height

    for (y <- 0 until h) {
      for (x <- 0 until w) {
        val gray = pixels(y * w + x)
        val char = table.map(gray)
        sb.append(char)
      }
    }
    AsciiImageData(w, h, sb.toString())
  }
}
