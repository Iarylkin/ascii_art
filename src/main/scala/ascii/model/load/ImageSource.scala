package ascii.model.load

import ascii.model.image.RgbImageData

/**
 * Common interface for loading or generating RGB images.
 */
trait ImageSource {

  /**
   * Loads an image into memory.
   *
   * @return A valid RgbImageData object.
   * @throws Exception if loading fails (e.g., file not found).
   */
  def load(): RgbImageData
}