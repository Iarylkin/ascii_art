package ascii.model.load.reader

import ascii.model.image.{Pixel, RgbImageData}

import java.awt.image.BufferedImage
import java.io.{File, IOException}
import javax.imageio.ImageIO

/**
 * Abstract base class for image readers that rely on Java's native ImageIO.
 *
 * This class handles the low-level conversion from java.awt.image.BufferedImage
 * to the project's internal RgbImageData format.
 *
 * @param name The format name (e.g., "jpg").
 * @param extensions A set of supported file extensions (e.g., "jpg", "jpeg").
 */
abstract class BufferedImageReader(val name: String, val extensions: Set[String])
  extends ImageReader {

  /**
   * Reads a file using ImageIO and converts it.
   *
   * @param file The file to read.
   * @return The parsed RgbImageData.
   * @throws IllegalArgumentException if the file cannot be read or decoded.
   */
  override def read(file: File): RgbImageData = {
    val maybeImage = try {
      Option(ImageIO.read(file))
    } catch {
      case _: IOException => None
    }

    maybeImage match {
      case Some(img) => fromBufferedImage(img, name)
      case None =>
        throw new IllegalArgumentException(s"Cannot read or decode $name image: ${file.getPath}")
    }
  }

  /**
   * Converts a BufferedImage into a Vector of Pixels.
   *
   * It iterates over the image dimensions and extracts RGB components
   * using bitwise operations.
   */
  private def fromBufferedImage(img: BufferedImage, format: String): RgbImageData = {
    val w = img.getWidth
    val h = img.getHeight

    // Flatten the 2D grid into a 1D Vector, strictly following row-major order.
    val pixels: Vector[Pixel] = (0 until h).flatMap { y =>
      (0 until w).map { x =>
        val argb = img.getRGB(x, y)
        Pixel(
          (argb >> 16) & 0xFF, // Red
          (argb >> 8) & 0xFF,  // Green
          argb & 0xFF          // Blue
        )
      }
    }.toVector

    RgbImageData(w, h, pixels, format)
  }
}