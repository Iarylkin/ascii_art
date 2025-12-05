package ascii.model.filter

import ascii.model.image.GrayscaleImageData

/**
 * Adjusts the brightness of a grayscale image.
 *
 * @param delta The amount to add to pixel values (positive to brighten, negative to darken).
 */
class BrightnessFilter(delta: Int) extends GrayscaleFilter {

  /**
   * Applies the brightness change, clamping values to [0, 255].
   */
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val adjustedPixels = image.getData.map { p =>
      Math.max(0, Math.min(255, p + delta))
    }
    GrayscaleImageData(image.width, image.height, adjustedPixels)
  }
}