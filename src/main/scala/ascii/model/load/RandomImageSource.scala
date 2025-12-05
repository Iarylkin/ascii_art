package ascii.model.load

import ascii.model.image.{Pixel, RgbImageData}
import scala.util.Random

/**
 * Generates a random RGB noise image.
 *
 * This source creates an image with random dimensions (between 20 and 200 pixels)
 * and fills every pixel with a random color.
 *
 * @param seed Optional seed for the random number generator (useful for testing).
 */
class RandomImageSource(seed: Option[Long] = None) extends ImageSource {

  /**
   * Generates the random image.
   *
   * @return A valid RgbImageData object containing random noise.
   */
  override def load(): RgbImageData = {
    // Use the provided seed or default to a new random sequence
    val rng = seed.map(new Random(_)).getOrElse(new Random())

    // Random dimensions between 20 and 200
    val width = 20 + rng.nextInt(181)
    val height = 20 + rng.nextInt(181)

    // Generate pixels with random RGB values [0, 255]
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