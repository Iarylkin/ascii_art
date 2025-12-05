package ascii.model.filter

import ascii.model.image.GrayscaleImageData

/**
 * Filters the image resolution.
 *
 * Note: The scale parameter represents the change in total area (pixel count),
 * not the linear dimension change.
 * - 4.0  => 2x width, 2x height
 * - 0.25 => 0.5x width, 0.5x height
 *
 * @param scale The target area scale factor (only 1.0, 4.0, or 0.25).
 */
class ScaleFilter(scale: Double) extends GrayscaleFilter {

  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    scale match {
      case 1.0 => image
      case 4.0 => scaleUp(image)
      case 0.25 => scaleDown(image)
      case _ => throw new IllegalArgumentException("ScaleFilter supports only 0.25, 1, and 4.")
    }
  }

  /**
   * Doubles the image dimensions using Nearest Neighbor interpolation.
   */
  private def scaleUp(image: GrayscaleImageData): GrayscaleImageData = {
    val w = image.width
    val h = image.height
    val newW = w * 2
    val newH = h * 2

    val newPixels = (0 until newH).flatMap { y =>
      (0 until newW).map { x =>
        // Map target pixel back to source pixel (integer division handles the repetition)
        image.pixel(x / 2, y / 2)
      }
    }.toVector

    GrayscaleImageData(newW, newH, newPixels)
  }

  /**
   * Halves the image dimensions using Average Pooling (2x2 grid average).
   */
  private def scaleDown(image: GrayscaleImageData): GrayscaleImageData = {
    val w = image.width
    val h = image.height
    val newW = Math.max(1, w / 2)
    val newH = Math.max(1, h / 2)

    val newPixels = (0 until newH).flatMap { y =>
      (0 until newW).map { x =>
        val startX = x * 2
        val startY = y * 2

        var sum = 0.0
        var count = 0

        // Average the 2x2 block
        for (dy <- 0 until 2; dx <- 0 until 2) {
          val sx = startX + dx
          val sy = startY + dy

          if (sx < w && sy < h) {
            sum += image.pixel(sx, sy)
            count += 1
          }
        }

        if (count > 0) sum / count else 0.0
      }
    }.toVector

    GrayscaleImageData(newW, newH, newPixels)
  }
}