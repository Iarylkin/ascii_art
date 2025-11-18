package ascii.model.convert

import ascii.model.image.{AsciiImageData, GrayscaleImageData, ImageData, RgbImageData}

trait Converter[I <: ImageData, O <: ImageData] {
  def convert(input: I): O
}

class GrayScaleConverter extends Converter[RgbImageData, GrayscaleImageData] {
  override def convert(input: RgbImageData): GrayscaleImageData = {
    val pixels = input.pixels.map { pixel =>
      0.3 * pixel.r + 0.59 * pixel.g + 0.11 * pixel.b
    }
    GrayscaleImageData(input.width, input.height, pixels)
  }
}

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
