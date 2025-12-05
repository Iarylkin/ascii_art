package ascii.model.image

/**
 * Base implementation of ImageData using a flat Vector for storage.
 * * Validates that dimensions are positive and that the data size matches the dimensions.
 *
 * @param width Image width.
 * @param height Image height.
 * @param data Flattened vector containing image data (row-major order).
 * @tparam T Type of data per pixel.
 */
abstract class AbstractGridImage[T](
                                     override val width: Int,
                                     override val height: Int,
                                     protected val data: Vector[T]
                                   ) extends ImageData[T] {

  require(width > 0 && height > 0, s"Dimensions must be positive, got ${width}x${height}")
  require(data.length == width * height, s"Data length (${data.length}) does not match dimensions ($width x $height = ${width * height})")

  /**
   * Retrieves the pixel at (x, y) by mapping 2D coordinates to the 1D vector index.
   */
  override def pixel(x: Int, y: Int): T = {
    data(y * width + x)
  }

  /**
   * Returns the underlying data vector.
   */
  def getData: Vector[T] = data
}