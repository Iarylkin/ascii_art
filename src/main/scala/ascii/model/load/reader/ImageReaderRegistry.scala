package ascii.model.load.reader

import java.io.File

/**
 * Registry that manages available image readers.
 */
class ImageReaderRegistry(private val readers: Vector[ImageReader]) {

  /**
   * Selects a suitable reader based on the file extension.
   *
   * @param file The file to check.
   * @return An Option containing the reader if found.
   */
  def pickFor(file: File): Option[ImageReader] = {
    val name = file.getName
    val idx = name.lastIndexOf('.')

    // Check if dot exists and is not the last character
    if (idx >= 0 && idx < name.length - 1) {
      val ext = name.substring(idx + 1).toLowerCase
      readers.find(_.extensions.contains(ext))
    } else {
      None
    }
  }
}

/**
 * Factory for the default registry configuration.
 */
object ImageReaderRegistry {
  def default: ImageReaderRegistry =
    new ImageReaderRegistry(Vector(
      new PngReader,
      new JpegReader,
      new GifReader
    ))
}