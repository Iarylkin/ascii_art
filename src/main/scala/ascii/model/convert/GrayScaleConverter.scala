package ascii.model.convert

import ascii.model.image.{GrayscaleImageData, RgbImageData}

class GrayScaleConverter extends Converter[RgbImageData, GrayscaleImageData] {
  override def convert(input: RgbImageData): GrayscaleImageData = {
    val grayPixels = (0 until input.height).flatMap { y =>
      (0 until input.width).map { x =>
        val p = input.pixel(x, y)
        0.3 * p.r + 0.59 * p.g + 0.11 * p.b
      }
    }.toVector

    GrayscaleImageData(input.width, input.height, grayPixels)
  }
}
