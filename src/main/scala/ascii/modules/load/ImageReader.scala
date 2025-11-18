package ascii.modules.load

import ascii.model.{Pixel, RgbImageData}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

trait ImageReader {
  def extensions: Set[String]
  def read(file: File): RgbImageData
}

object ImageReaderRegistry {
  def default: ImageReaderRegistry =
    new ImageReaderRegistry(Vector(
      new PngReader,
      new JpegReader,
      new GifReader
    ))
}

class ImageReaderRegistry(private val readers: Vector[ImageReader]) {
  def pickFor(file: File): Option[ImageReader] = {
    val name = file.getName
    val idx = name.lastIndexOf('.')
    if (idx >= 0 && idx < name.length - 1) {
      val ext = name.substring(idx + 1).toLowerCase
      readers.find(_.extensions.contains(ext))
    } else {
      None
    }
  }
}

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
    val pixels = Array.ofDim[Pixel](w * h)

    for (y <- 0 until h; x <- 0 until w) {
      val argb = img.getRGB(x, y)
      pixels(y * w + x) = Pixel(
        (argb >> 16) & 0xFF,
        (argb >> 8) & 0xFF,
        argb & 0xFF
      )
    }
    RgbImageData(w, h, pixels, format)
  }
}

class PngReader extends BufferedImageReader("png", Set("png"))
class JpegReader extends BufferedImageReader("jpeg", Set("jpg", "jpeg"))
class GifReader extends BufferedImageReader("gif", Set("gif"))
