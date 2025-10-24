package Main

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

@main def main: Unit = {
  val path = "src/main/resources/image1.jpeg"
  val imageSource = new FileImageSource(path)
  val imageData = imageSource.load()
}

case class Pixel(r: Int, g: Int, b: Int) {
}

case class ImageData(
  width: Int,
  height: Int,
  pixels: Array[Pixel],
  format: String
)

object ImageData {
  def fromBufferedImage(img: BufferedImage, format: String): ImageData = {
    val w = img.getWidth
    val h = img.getHeight
    val pixels = Array.ofDim[Pixel](w * h)

    for (y <- 0 until h; x <- 0 until w) {
      val argb = img.getRGB(x, y)
      pixels(y * w + x) = fromARGB(argb)
    }

    ImageData(w, h, pixels, format)
  }

  private def fromARGB(argb: Int): Pixel =
    Pixel(
      (argb >> 16) & 0xFF,
      (argb >> 8) & 0xFF,
      argb & 0xFF
    )
}

trait ImageSource {
  def load() : ImageData
}

class FileImageSource(private val path: String) extends ImageSource {

  override def load(): ImageData = {
    val file = new File(path)
    if (!file.exists())
      throw new IllegalArgumentException(s"File not found: ${file.getPath}")

    val registry = ImageReaderRegistry.default
    val reader = registry
      .pickFor(file)
      .getOrElse(throw new IllegalArgumentException(s"Unsupported image extension: ${file.getName}"))

    reader.read(file)
  }
}

final class ImageReaderRegistry(private val readers: Vector[ImageReader]) {

  def pickFor(file: File): Option[ImageReader] = {
    val ext = extensionOf(file)
    ext.flatMap(e => readers.find(_.extensions.contains(e)))
  }

  private def extensionOf(file: File): Option[String] = {
    val name = file.getName
    val idx = name.lastIndexOf('.')
    if (idx >= 0 && idx < name.length - 1)
      Some(name.substring(idx + 1).toLowerCase)
    else None
  }

  def register(reader: ImageReader): ImageReaderRegistry =
    new ImageReaderRegistry(readers :+ reader)
}

object ImageReaderRegistry {
  def default: ImageReaderRegistry =
    new ImageReaderRegistry(Vector(
      new PngReader,
      new JpegReader,
      new GifReader
    ))
}

trait ImageReader {
  def name: String
  def extensions: Set[String]
  def read(file: File): ImageData
}

abstract class BufferedImageReader(val name: String, val extensions: Set[String])
  extends ImageReader {

  override def read(file: File): ImageData = {
    val img = ImageIO.read(file)
    if (img == null)
      throw new IllegalArgumentException(s"Cannot read $name image: ${file.getPath}")
    ImageData.fromBufferedImage(img, name)
  }
}

class PngReader extends BufferedImageReader("png", Set("png"))
class JpegReader extends BufferedImageReader("jpeg", Set("jpg", "jpeg"))
class GifReader extends BufferedImageReader("gif", Set("gif"))