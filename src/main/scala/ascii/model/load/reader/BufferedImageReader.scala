package ascii.model.load.reader

import ascii.model.image.{Pixel, RgbImageData}

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

abstract class BufferedImageReader(val name: String, val extensions: Set[String])
  extends ImageReader {

  override def read(file: File): RgbImageData = {
    val img = ImageIO.read(file)
    if (img == null)
      throw new IllegalArgumentException(s"Cannot read $name image: ${file.getPath}")
    fromBufferedImage(img, name)
  }

  protected def fromBufferedImage(img: BufferedImage, format: String): RgbImageData = {
    val w = img.getWidth
    val h = img.getHeight
    
    val pixels: Vector[Pixel] = (0 until h).flatMap { y =>
      (0 until w).map { x =>
        val argb = img.getRGB(x, y)
        Pixel(
          (argb >> 16) & 0xFF,
          (argb >> 8) & 0xFF,
          argb & 0xFF
        )
      }
    }.toVector
    
    RgbImageData(w, h, pixels, format)
  }
}
