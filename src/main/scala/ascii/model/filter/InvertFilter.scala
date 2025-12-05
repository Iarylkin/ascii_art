package ascii.model.filter

import ascii.model.image.GrayscaleImageData

/**
 * Inverts the brightness of a grayscale image (negative effect).
 */
class InvertFilter extends GrayscaleFilter {

  /**
   * Replaces every pixel value 'p' with '255 - p'.
   */
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val invertedPixels = image.getData.map(p => 255 - p)
    GrayscaleImageData(image.width, image.height, invertedPixels)
  }
}