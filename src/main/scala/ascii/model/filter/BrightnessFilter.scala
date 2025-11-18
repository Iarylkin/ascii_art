package ascii.model.filter

import ascii.model.image.GrayscaleImageData

class BrightnessFilter(delta: Int) extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val adjusted = image.pixels.map { p =>
      Math.max(0, Math.min(255, p + delta))
    }
    image.copy(pixels = adjusted)
  }
}
