package Main

import java.awt.image.BufferedImage
import java.io.{File, PrintWriter}
import javax.imageio.ImageIO
import scala.util.Random

@main def main: Unit = {
  val path = "src/main/resources/linux.jpg"

  val imageSource = new FileImageSource(path)
  val imageData = imageSource.load()


  val longBorke = " .'`^\",:;Il!i><~+_-?][]{}1()|\\//tfrxnuvczyXUJCLQ0OZmwpqdbkhao*#MW&8%B@$"
  val customCharsTable = " 0"

  val converter = new AsciiConverter(GammaTable(2.2))
  val asciiArt = converter.convert(imageData)
  val saver = new FileSaver("src/main/resources/asciiArt2.txt")
  saver.save(asciiArt)
}

case class Pixel(r: Int, g: Int, b: Int) {
}


case class ImageData (
  width: Int,
  height: Int,
  pixels: Array[Pixel],
  format: String
)

case class AsciiImageData(
  width: Int,
  height: Int,
  pixels: String,
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

class RandomImageSource(seed: Option[Long] = None) extends ImageSource {
  override def load(): ImageData = {
    val rng = seed match {
      case Some(s) => new Random(s)
      case None => new Random()
    }
    val width = 20 + rng.nextInt(181)
    val height = 20 + rng.nextInt(181)

    val pixels = Array.ofDim[Pixel](width * height)

    for (i <- pixels.indices) {
      val r = rng.nextInt(256)
      val g = rng.nextInt(256)
      val b = rng.nextInt(256)
      pixels(i) = Pixel(r, g, b)
    }

    ImageData(width, height, pixels, "random")
  }
}

class PngReader extends BufferedImageReader("png", Set("png"))
class JpegReader extends BufferedImageReader("jpeg", Set("jpg", "jpeg"))
class GifReader extends BufferedImageReader("gif", Set("gif"))

class AsciiConverter(table: AsciiTable) {
  def convert(imageData: ImageData) : AsciiImageData = {
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
    AsciiImageData(imageData.width, imageData.height, sb.toString())
  }

  private def toGreyscale(r: Int, g: Int, b: Int): Double = {
    0.3 * r + 0.59 * g + 0.11 * b
  }
}

trait AsciiTable {
  def map(gray: Double): Char
}

class LinearTable(val charsTable: String = " .:-=+*#%@") extends AsciiTable {
  override def map(gray: Double): Char = {
    val step = 256.0 / charsTable.length
    val index = Math.min((gray / step).toInt, charsTable.length - 1)
    charsTable.charAt(index)
  }
}

class GammaTable(val gamma: Double = 1.8) extends AsciiTable {
  val charsTable = " .:-=+*#%@"

  override def map(gray: Double): Char = {
    val normalized = gray / 255.0
    val corrected = Math.pow(normalized, gamma)
    val index = (corrected * charsTable.length).toInt
    charsTable(Math.min(index, charsTable.length - 1))
  }
}


trait Saver {
  def save(data: AsciiImageData): Unit
}

abstract class AbstractSaver extends Saver {

  protected def open(): Unit = {}

  protected def writeChar(c: Char): Unit

  protected def writeNewLine(): Unit

  protected def close(): Unit = {}

  final override def save(data: AsciiImageData): Unit = {
    open()
    try {
      for (i <- 0 until data.pixels.length) {
        writeChar(data.pixels(i))
        if ((i + 1) % data.width == 0)
          writeNewLine()
      }
    } finally {
      close()
    }
  }
}

class ConsoleSaver extends AbstractSaver {
  override protected def writeChar(c: Char): Unit = print(c)
  override protected def writeNewLine(): Unit = println()
}

class FileSaver(path: String) extends AbstractSaver {
  private var writer: PrintWriter = _

  override protected def open(): Unit = {
    writer = new PrintWriter(new File(path))
  }

  override protected def writeChar(c: Char): Unit = writer.print(c)
  override protected def writeNewLine(): Unit = writer.println()
  override protected def close(): Unit = writer.close()
}