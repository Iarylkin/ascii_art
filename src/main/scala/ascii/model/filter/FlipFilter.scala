package ascii.model.filter

import ascii.model.image.GrayscaleImageData

class FlipFilter(flipX: Boolean, flipY: Boolean) extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val w = image.width
    val h = image.height
    val pixels = image.pixels
    val flipped = Array.ofDim[Double](pixels.length)

    for (y <- 0 until h; x <- 0 until w) {
      val srcIndex = y * w + x
      val newX = if (flipY) w - 1 - x else x
      val newY = if (flipX) h - 1 - y else y
      val dstIndex = newY * w + newX
      flipped(dstIndex) = pixels(srcIndex)
    }
    image.copy(pixels = flipped)
  }
}
