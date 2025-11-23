package ascii.model.load.reader

import java.io.File


class ImageReaderRegistry(private val readers: Vector[ImageReader]) {
  def pickFor(file: File): Option[ImageReader] = {
    val name = file.getName
    val idx = name.lastIndexOf('.')
    if (idx >= 0 && idx < name.length - 1) {
      val ext = name.substring(idx + 1).toLowerCase
      readers.find(_.extensions.contains(ext))
    } else {
      None
    }
  }
}

object ImageReaderRegistry {
  def default: ImageReaderRegistry =
    new ImageReaderRegistry(Vector(
      new PngReader,
      new JpegReader,
      new GifReader
    ))
}

