package ascii.model.filter

import ascii.model.image.GrayscaleImageData

class InvertFilter extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val invertedPixels = image.pixels.map(p => 255 - p)
    image.copy(pixels = invertedPixels)
  }
}
