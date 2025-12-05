package ascii.model.load.reader

import ascii.model.image.RgbImageData
import java.io.File

/**
 * Interface for reading a specific image file format.
 */
trait ImageReader {

  /**
   * Supported file extensions.
   */
  def extensions: Set[String]

  /**
   * Reads a file and converts it to an RGB image.
   */
  def read(file: File): RgbImageData
}