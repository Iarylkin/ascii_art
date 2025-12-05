package ascii.model.image

/**
 * Represents a grayscale image where pixels are brightness values (Double).
 *
 * @param w Image width.
 * @param h Image height.
 * @param d Vector of double values (typically 0.0 to 255.0).
 */
case class GrayscaleImageData(w: Int, h: Int, d: Vector[Double])
  extends AbstractGridImage[Double](w, h, d)