package Main

import java.awt.image.BufferedImage
import java.io.{File, PrintWriter}
import javax.imageio.ImageIO
import scala.util.Random

@main def main(args: String*): Unit = {
  try {

    val commands = ConsoleUI.start(args.toArray)
    if (commands.isEmpty) return

    AsciiArtProcessor.run(commands)

  } catch {
    case e: Exception =>
      println(s"Error: ${e.getMessage}")
  }
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
  def load() : RgbImageData
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
class FlipFilter(flipX: Boolean, flipY: Boolean) extends ImageFilter[GrayscaleImageData, GrayscaleImageData] {
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
class ScaleFilter(scale: Double) extends ImageFilter[GrayscaleImageData, GrayscaleImageData] {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    scale match {
      case 1.0 =>
        image

      case 4.0 =>
        val w = image.width
        val h = image.height
        val src = image.pixels
        val newW = w * 2
        val newH = h * 2
        val newPixels = Array.ofDim[Double](newW * newH)

        for (y <- 0 until h; x <- 0 until w) {
          val value = src(y * w + x)
          for (dy <- 0 until 2; dx <- 0 until 2) {
            val nx = x * 2 + dx
            val ny = y * 2 + dy
            newPixels(ny * newW + nx) = value
          }
        }

        GrayscaleImageData(newW, newH, newPixels)

      case 0.25 =>
        val w = image.width
        val h = image.height
        val src = image.pixels
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
              sum += src(sy * w + sx)
              count += 1
            }
          }
          newPixels(y * newW + x) = if (count > 0) sum / count else 0
        }

        GrayscaleImageData(newW, newH, newPixels)

      case _ =>
        throw new IllegalArgumentException("ScaleFilter supports only 0.25, 1, and 4.")
    }
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

class Pipeline(
                val imageSource: ImageSource,
                val grayConverter: Converter[RgbImageData, GrayscaleImageData],
                val filters: Seq[ImageFilter[GrayscaleImageData, GrayscaleImageData]],
                val asciiConverter: Converter[GrayscaleImageData, AsciiImageData],
                val savers: Seq[Saver]
              ) {
  def run(): Unit = {
    val rgb = imageSource.load()
    val gray = grayConverter.convert(rgb)
    val filtered = filters.foldLeft(gray)((img, f) => f.apply(img))
    val ascii = asciiConverter.convert(filtered)
    savers.foreach(_.save(ascii))
  }
}

class PipelineBuilder {
  private var imageSource: Option[ImageSource] = None
  private var grayConverter: Option[Converter[RgbImageData, GrayscaleImageData]] = None
  private var filters: Seq[ImageFilter[GrayscaleImageData, GrayscaleImageData]] = Seq.empty
  private var asciiConverter: Option[Converter[GrayscaleImageData, AsciiImageData]] = None
  private var savers: Seq[Saver] = Seq.empty

  def withImageSource(src: ImageSource): PipelineBuilder = {
    imageSource = Some(src); this
  }

  def withGrayConverter(conv: Converter[RgbImageData, GrayscaleImageData]): PipelineBuilder = {
    grayConverter = Some(conv); this
  }

  def addFilter(f: ImageFilter[GrayscaleImageData, GrayscaleImageData]): PipelineBuilder = {
    filters = filters :+ f; this
  }

  def withAsciiConverter(conv: Converter[GrayscaleImageData, AsciiImageData]): PipelineBuilder = {
    asciiConverter = Some(conv); this
  }

  def addSaver(s: Saver): PipelineBuilder = {
    savers = savers :+ s; this
  }

  def build(): Pipeline = {
    new Pipeline(
      imageSource.getOrElse(throw new IllegalStateException("ImageSource not set")),
      grayConverter.getOrElse(new GrayScaleConverter()),
      filters,
      asciiConverter.getOrElse(new AsciiConverter(new LinearTable())),
      if (savers.nonEmpty) savers else Seq(new ConsoleSaver())
    )
  }
}

case class CommandDTO(name: String, params: List[String] = Nil)

trait CommandHandler {
  def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit
}

class ImageSourceHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(
      throw new IllegalArgumentException("Error: Command '--image' requires a file path argument.")
    )

    builder.withImageSource(new FileImageSource(path))
  }
}

class ScaleFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val paramStr = cmd.params.headOption.getOrElse(
      throw new IllegalArgumentException("Error: Command '--scale' requires a number argument (e.g., 0.25, 1, 4).")
    )

    val scale = paramStr.toDoubleOption.getOrElse(
      throw new IllegalArgumentException(s"Error: '$paramStr' is not a valid number for scale.")
    )

    builder.addFilter(new ScaleFilter(scale))
  }
}

class FileOutputHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(
      throw new IllegalArgumentException("Error: Command '--output-file' requires a file path argument.")
    )

    builder.addSaver(new FileSaver(path))
  }
}

class ConsoleOutputHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.addSaver(new ConsoleSaver())
  }
}

class InvertFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.addFilter(new InvertFilter())
  }
}

class BrightnessFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {

    val param = cmd.params.headOption.getOrElse(
      throw new IllegalArgumentException("Error: Command '--brightness' requires an integer argument.")
    )

    val delta = param.toIntOption.getOrElse(
      throw new IllegalArgumentException(s"Error: '$param' is not a valid integer for brightness.")
    )

    builder.addFilter(new BrightnessFilter(delta))
  }
}

class FlipFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {

    val flipX = cmd.params.contains("x")
    val flipY = cmd.params.contains("y")

    if (!flipX && !flipY) {
      throw new IllegalArgumentException("Error: Command '--flip' requires 'x' and/or 'y' parameters.")
    }

    builder.addFilter(new FlipFilter(flipX, flipY))
  }
}

object CommandRegistry {
  private val handlers = Map(
    "image"          -> new ImageSourceHandler(),
    "scale"          -> new ScaleFilterHandler(),
    "invert"         -> new InvertFilterHandler(),
    "output-console" -> new ConsoleOutputHandler(),
    "brightness" -> new BrightnessFilterHandler(),
    "flip"       -> new FlipFilterHandler(),
    "output-file"    -> new FileOutputHandler()
  )

  def getHandler(name: String): Option[CommandHandler] = handlers.get(name.toLowerCase)
}

object AsciiArtProcessor {

  def run(commands: List[CommandDTO]): Unit = {
    val builder = new PipelineBuilder()

    commands.foreach { cmd =>
      CommandRegistry.getHandler(cmd.name) match {
        case Some(handler) => handler.handle(cmd, builder)
        case None => println(s"Warning: Unknown command '${cmd.name}'")
      }
    }

    builder.build().run()
  }
}

class CommandLineParser {
  private val syntax: Map[String, (String, Int)] = Map(
    "--image"          -> ("image", 1),
    "--image-random"   -> ("image-random", 0),

    "--table"          -> ("table", 1),
    "--custom-table"   -> ("custom-table", 1),

    "--scale"          -> ("scale", 1),
    "--rotate"         -> ("rotate", 1),
    "--invert"         -> ("invert", 0),
    "--brightness"     -> ("brightness", 1),
    "--flip"           -> ("flip", 1),

    "--output-console" -> ("output-console", 0),
    "--output-file"    -> ("output-file", 1)
  )
  def parse(args: List[String]): List[CommandDTO] = {
    parseRecursive(args, List.empty)
  }

  private def parseRecursive(currentArgs: List[String], acc: List[CommandDTO]): List[CommandDTO] = {
    currentArgs match {
      case Nil => acc

      case head :: tail =>
        syntax.get(head) match {
          case Some((name, paramCount)) =>

            if (tail.length < paramCount) {
              throw new IllegalArgumentException(s"Error: Argument '$head' requires $paramCount parameter(s).")
            }

            val params = tail.take(paramCount)

            if (params.exists(_.startsWith("--"))) {
              throw new IllegalArgumentException(s"Error: Value for '$head' cannot be another flag (found '${params.find(_.startsWith("--")).get}').")
            }

            val cmd = CommandDTO(name, params)
            val remaining = tail.drop(paramCount)

            parseRecursive(remaining, acc :+ cmd)

          case None =>
            throw new IllegalArgumentException(s"Error: Unknown argument '$head'")
        }
    }
  }
}

object ConsoleUI {

  private val parser = new CommandLineParser()

  def start(args: Array[String]): List[CommandDTO] = {

    if (args.isEmpty) {
      println("Usage: run --image <path> [--rotate <deg>] [--scale <val>] ...")
      return List.empty
    }
    parser.parse(args.toList)
  }
}

