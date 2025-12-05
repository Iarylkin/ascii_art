package ascii.model.image

/**
 * Generic interface for an image grid holding data of type T.
 */
trait ImageData[T] {

  /**
   * Returns the image width.
   */
  def width: Int

  /**
   * Returns the image height.
   */
  def height: Int

  /**
   * Returns the pixel value at the given coordinates.
   *
   * @param x X coordinate.
   * @param y Y coordinate.
   * @return The data at (x, y).
   */
  def pixel(x: Int, y: Int): T
}