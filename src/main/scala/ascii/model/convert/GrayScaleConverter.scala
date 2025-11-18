package ascii.model.convert

import ascii.model.image.{GrayscaleImageData, RgbImageData}

class GrayScaleConverter extends Converter[RgbImageData, GrayscaleImageData] {
  override def convert(input: RgbImageData): GrayscaleImageData = {
    val pixels = input.pixels.map { pixel =>
      0.3 * pixel.r + 0.59 * pixel.g + 0.11 * pixel.b
    }
    GrayscaleImageData(input.width, input.height, pixels)
  }
}
