package ascii.model.convert

import ascii.model.convert.tables.AsciiTable
import ascii.model.image.{AsciiImageData, GrayscaleImageData}

class AsciiConverter(table: AsciiTable) extends Converter[GrayscaleImageData, AsciiImageData] {
  override def convert(input: GrayscaleImageData): AsciiImageData = {
    val asciiChars = (0 until input.height).flatMap { y =>
      (0 until input.width).map { x =>
        val grayValue = input.pixel(x, y)
        table.map(grayValue)
      }
    }.toVector
    
    AsciiImageData(input.width, input.height, asciiChars)
  }
}
