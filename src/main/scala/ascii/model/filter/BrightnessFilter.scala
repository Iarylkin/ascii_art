package ascii.model.filter

import ascii.model.image.GrayscaleImageData

class BrightnessFilter(delta: Int) extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val adjustedPixels = image.getData.map { p =>
      Math.max(0, Math.min(255, p + delta))
    }
    GrayscaleImageData(image.width, image.height, adjustedPixels)
  }
}
