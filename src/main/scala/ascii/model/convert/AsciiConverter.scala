package ascii.model.convert

import ascii.model.convert.tables.AsciiTable
import ascii.model.image.{AsciiImageData, GrayscaleImageData}

/**
 * Converts a grayscale image into an ASCII image.
 */
class AsciiConverter(table: AsciiTable) extends Converter[GrayscaleImageData, AsciiImageData] {

  /**
   * Maps every grayscale pixel to a character from the table.
   */
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