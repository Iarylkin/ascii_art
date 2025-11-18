package ascii.model.load

import ascii.model.image.{Pixel, RgbImageData}

import java.io.File
import scala.util.Random

trait ImageSource {
  def load() : RgbImageData
}

class FileImageSource(path: String) extends ImageSource {
  override def load(): RgbImageData = {
    val file = new File(path)
    if (!file.exists()) throw new IllegalArgumentException(s"File not found: $path")

    ImageReaderRegistry.default.pickFor(file)
      .map(_.read(file))
      .getOrElse(throw new IllegalArgumentException(s"Unsupported extension: ${file.getName}"))
  }
}

class RandomImageSource(seed: Option[Long] = None) extends ImageSource {
  override def load(): RgbImageData = {
    val rng = seed.map(new Random(_)).getOrElse(new Random())
    val width = 20 + rng.nextInt(181)
    val height = 20 + rng.nextInt(181)
    val pixels = Array.fill(width * height)(Pixel(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256)))
    RgbImageData(width, height, pixels, "random")
  }
}