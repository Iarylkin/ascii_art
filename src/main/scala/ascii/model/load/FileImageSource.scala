package ascii.model.load

import ascii.model.image.RgbImageData
import ascii.model.load.reader.ImageReaderRegistry

import java.io.File

class FileImageSource(path: String) extends ImageSource {
  override def load(): RgbImageData = {
    val file = new File(path)
    if (!file.exists()) throw new IllegalArgumentException(s"File not found: $path")

    ImageReaderRegistry.default.pickFor(file)
      .map(_.read(file))
      .getOrElse(throw new IllegalArgumentException(s"Unsupported extension: ${file.getName}"))
  }
}
