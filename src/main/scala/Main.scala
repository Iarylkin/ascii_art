package Main

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

@main def main: Unit = {
  val path = "src/main/resources/smile.jpg"
  val imageSource = new FileImageSource(path)
  val imageData = imageSource.load()

  val converter = new AsciiConverter(LinearTable())
  val asciiArt = converter.convert(imageData)
  for(i <- 0 until asciiArt.length) {
    print(asciiArt(i))
    if ((i + 1) % imageData.width == 0) {
      println()
    }
  }
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

class AsciiConverter(table: AsciiTable) {
  def convert(imageData: ImageData) : String = {
    val sb = new StringBuilder
    val pixels = imageData.pixels
    for(y <- 0 until imageData.height) {
      for(x <- 0 until imageData.width) {
        val index = y * imageData.width + x
        val pixel = pixels(index)
        val greyscalePixel = toGreyscale(pixel.r, pixel.g, pixel.b)
        val asciiPixel = table.map(greyscalePixel)
        sb.append(asciiPixel)
      }
    }
    sb.toString()
  }

  private def toGreyscale(r: Int, g: Int, b: Int): Double = {
    0.3 * r + 0.59 * g + 0.11 * b
  }
}

trait AsciiTable {
  def map(gray: Double): Char
}

class LinearTable extends AsciiTable {
  val chars = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. "
  override def map(gray: Double): Char = {
    val norm = gray / 255
    val index = ((chars.length - 1) - norm * (chars.length - 1)).toInt
    chars.charAt(index)
  }
}
