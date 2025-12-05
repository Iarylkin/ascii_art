package ascii.model.image

/**
 * Represents a color image consisting of RGB pixels.
 *
 * @param w Image width.
 * @param h Image height.
 * @param d Vector of Pixel objects.
 * @param format The file format of the source image (e.g., "jpg", "png").
 */
case class RgbImageData(
                         w: Int,
                         h: Int,
                         d: Vector[Pixel],
                         format: String
                       ) extends AbstractGridImage[Pixel](w, h, d)