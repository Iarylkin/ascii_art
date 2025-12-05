package ascii.model.save

import ascii.model.image.AsciiImageData

/**
 * Common interface for outputting the generated ASCII art.
 */
trait Saver {

  /**
   * Persists or displays the ASCII image data.
   *
   * @param data The ASCII image to save.
   */
  def save(data: AsciiImageData): Unit
}