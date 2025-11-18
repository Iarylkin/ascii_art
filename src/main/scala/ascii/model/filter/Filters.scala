package ascii.model.filter

import ascii.model.image.GrayscaleImageData

trait ImageFilter[I, O] {
  def apply(image: I): O
}

trait GrayscaleFilter extends ImageFilter[GrayscaleImageData, GrayscaleImageData]

class InvertFilter extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val invertedPixels = image.pixels.map(p => 255 - p)
    image.copy(pixels = invertedPixels)
  }
}

class BrightnessFilter(delta: Int) extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val adjusted = image.pixels.map { p =>
      Math.max(0, Math.min(255, p + delta))
    }
    image.copy(pixels = adjusted)
  }
}

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

class ScaleFilter(scale: Double) extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    scale match {
      case 1.0 => image
      case 4.0 => scaleUp4x(image)
      case 0.25 => scaleDown4x(image)
      case _ => throw new IllegalArgumentException("ScaleFilter supports only 0.25, 1, and 4.")
    }
  }

  private def scaleUp4x(image: GrayscaleImageData): GrayscaleImageData = {
    val w = image.width
    val h = image.height
    val newW = w * 2
    val newH = h * 2
    val newPixels = Array.ofDim[Double](newW * newH)

    for (y <- 0 until h; x <- 0 until w) {
      val value = image.pixels(y * w + x)
      for (dy <- 0 until 2; dx <- 0 until 2) {
        newPixels((y * 2 + dy) * newW + (x * 2 + dx)) = value
      }
    }
    GrayscaleImageData(newW, newH, newPixels)
  }

  private def scaleDown4x(image: GrayscaleImageData): GrayscaleImageData = {
    val w = image.width
    val h = image.height
    val newW = Math.max(1, w / 2)
    val newH = Math.max(1, h / 2)
    val newPixels = Array.ofDim[Double](newW * newH)

    for (y <- 0 until newH; x <- 0 until newW) {
      var sum = 0.0
      var count = 0
      for (dy <- 0 until 2; dx <- 0 until 2) {
        val sx = x * 2 + dx
        val sy = y * 2 + dy
        if (sx < w && sy < h) {
          sum += image.pixels(sy * w + sx)
          count += 1
        }
      }
      newPixels(y * newW + x) = if (count > 0) sum / count else 0
    }
    GrayscaleImageData(newW, newH, newPixels)
  }
}
