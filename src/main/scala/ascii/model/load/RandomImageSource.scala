package ascii.model.load

import ascii.model.image.{Pixel, RgbImageData}

import scala.util.Random

class RandomImageSource(seed: Option[Long] = None) extends ImageSource {
  override def load(): RgbImageData = {
    val rng = seed.map(new Random(_)).getOrElse(new Random())
    val width = 20 + rng.nextInt(181)
    val height = 20 + rng.nextInt(181)
    
    val pixels: Vector[Pixel] = Vector.fill(width * height) {
      Pixel(
        rng.nextInt(256),
        rng.nextInt(256),
        rng.nextInt(256)
      )
    }
    
    RgbImageData(width, height, pixels, "random")
  }
}
