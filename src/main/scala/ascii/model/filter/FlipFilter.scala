package ascii.model.filter

import ascii.model.image.GrayscaleImageData

class FlipFilter(flipX: Boolean, flipY: Boolean) extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val w = image.width
    val h = image.height

    val newPixels = (0 until h).flatMap { y =>
      (0 until w).map { x =>
        val srcY = if (flipX) h - 1 - y else y
        val srcX = if (flipY) w - 1 - x else x

        image.pixel(srcX, srcY)
      }
    }.toVector
    GrayscaleImageData(w, h, newPixels)
  }
}
