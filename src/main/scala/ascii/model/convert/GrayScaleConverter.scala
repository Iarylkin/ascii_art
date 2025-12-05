package ascii.model.convert

import ascii.model.image.{GrayscaleImageData, RgbImageData}

/**
 * Converts an RGB image to grayscale.
 */
class GrayScaleConverter extends Converter[RgbImageData, GrayscaleImageData] {

  /**
   * Calculates pixel brightness using the luminosity formula: 0.3R + 0.59G + 0.11B.
   */
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