package ascii.model.filter

import ascii.model.image.GrayscaleImageData

/**
 * Flips the image around the specified axes.
 *
 * @param flipX If true, reflects the image across the X-axis (vertical flip).
 * @param flipY If true, reflects the image across the Y-axis (horizontal flip).
 */
class FlipFilter(flipX: Boolean, flipY: Boolean) extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val w = image.width
    val h = image.height

    val newPixels = (0 until h).flatMap { y =>
      (0 until w).map { x =>
        // To flip around X-axis, we must invert the Y-coordinate.
        val srcY = if (flipX) h - 1 - y else y

        // To flip around Y-axis, we must invert the X-coordinate.
        val srcX = if (flipY) w - 1 - x else x

        image.pixel(srcX, srcY)
      }
    }.toVector
    GrayscaleImageData(w, h, newPixels)
  }
}