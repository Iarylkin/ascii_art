package Main

import java.awt.image.BufferedImage
import java.io.{File, PrintWriter}
import javax.imageio.ImageIO
import scala.util.Random

@main def main: Unit = {
  val path = "src/main/resources/linux.jpg"

  val imageSource = new FileImageSource(path)
  val rgbImageData = imageSource.load()


  val longBorke = " .'`^\",:;Il!i><~+_-?][]{}1()|\\//tfrxnuvczyXUJCLQ0OZmwpqdbkhao*#MW&8%B@$"
  val customCharsTable = " 0"

  val grayScaleConverter = new GrayScaleConverter()
  val grayScaleImageData = grayScaleConverter.convert(rgbImageData)

  val filter = new BrightnessFilter(-100)
  val invertGrayscaleImageData = filter.apply(grayScaleImageData)

  val asciiConverter = new AsciiConverter(GammaTable(2.2))
  val asciiArt = asciiConverter.convert(invertGrayscaleImageData)

  val saver = new FileSaver("src/main/resources/asciiArtFiltered.txt")
  saver.save(asciiArt)
}

case class Pixel(r: Int, g: Int, b: Int) {
}

abstract class ImageData(val width: Int, val height: Int)

case class RgbImageData(
                         override val width: Int,
                         override val height: Int,
                         pixels: Array[Pixel],
                         format: String
                       ) extends ImageData(width, height)

case class GrayscaleImageData(
                               override val width: Int,
                               override val height: Int,
                               pixels: Array[Double]
                             ) extends ImageData(width, height)

case class AsciiImageData(
                           override val width: Int,
                           override val height: Int,
                           pixels: String
                         ) extends ImageData(width, height)


trait ImageSource {
  def load() : ImageData
}

class FileImageSource(private val path: String) extends ImageSource {

  override def load(): RgbImageData = {
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
  def read(file: File): RgbImageData
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
      pixels(y * w + x) = fromARGB(argb)
    }

    RgbImageData(w, h, pixels, format)
  }

  protected def fromARGB(argb: Int): Pixel =
    Pixel(
      (argb >> 16) & 0xFF,
      (argb >> 8) & 0xFF,
      argb & 0xFF
    )
}

class RandomImageSource(seed: Option[Long] = None) extends ImageSource {
  override def load(): RgbImageData = {
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

    RgbImageData(width, height, pixels, "random")
  }
}

class PngReader extends BufferedImageReader("png", Set("png"))
class JpegReader extends BufferedImageReader("jpeg", Set("jpg", "jpeg"))
class GifReader extends BufferedImageReader("gif", Set("gif"))

trait ImageFilter[I <: ImageData, O <: ImageData] {
  def apply(imageData: I): O
}

class InvertFilter extends ImageFilter[GrayscaleImageData, GrayscaleImageData] {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val invertedPixels = image.pixels.map(p => 255 - p)
    image.copy(pixels = invertedPixels)
  }
}

class BrightnessFilter(delta: Int) extends ImageFilter[GrayscaleImageData, GrayscaleImageData] {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val adjusted = image.pixels.map { p =>
      val newValue = p + delta
      Math.max(0, Math.min(255, newValue))
    }
    image.copy(pixels = adjusted)
  }
}

trait Converter[I <: ImageData, O <: ImageData] {
  def convert(input: I): O
}

class GrayScaleConverter extends Converter[RgbImageData, GrayscaleImageData] {

  override def convert(input: RgbImageData): GrayscaleImageData = {
    val pixels = input.pixels.map { pixel =>
      0.3 * pixel.r + 0.59 * pixel.g + 0.11 * pixel.b
    }
    GrayscaleImageData(input.width, input.height, pixels)
  }
}

class AsciiConverter(table: AsciiTable) extends Converter[GrayscaleImageData, AsciiImageData] {

  override def convert(input: GrayscaleImageData): AsciiImageData = {
    val sb = new StringBuilder
    for (y <- 0 until input.height) {
      for (x <- 0 until input.width) {
        val gray = input.pixels(y * input.width + x)
        val asciiChar = table.map(gray)
        sb.append(asciiChar)
      }
    }
    AsciiImageData(input.width, input.height, sb.toString())
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