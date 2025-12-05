package ascii.model.load

import ascii.model.image.RgbImageData
import ascii.model.load.reader.ImageReaderRegistry
import java.io.File

/**
 * Loads an image from the filesystem.
 *
 * It uses the ImageReaderRegistry to find a suitable reader for the file's extension.
 *
 * @param path Relative or absolute path to the image file.
 */
class FileImageSource(path: String) extends ImageSource {

  /**
   * Validates file existence and delegates reading to a registered ImageReader.
   *
   * @return The loaded RGB image.
   * @throws IllegalArgumentException If the file does not exist or the format is not supported.
   */
  override def load(): RgbImageData = {
    val file = new File(path)
    if (!file.exists()) {
      throw new IllegalArgumentException(s"File not found: $path")
    }

    // Attempt to find a reader for this file in the registry
    ImageReaderRegistry.default.pickFor(file)
      .map(_.read(file))
      .getOrElse(throw new IllegalArgumentException(s"Unsupported extension: ${file.getName}"))
  }
}