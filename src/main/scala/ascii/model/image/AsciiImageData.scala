package ascii.model.image

/**
 * Represents the final ASCII art image where each pixel is a character.
 *
 * @param w Image width.
 * @param h Image height.
 * @param d Vector of characters representing the image.
 */
case class AsciiImageData(w: Int, h: Int, d: Vector[Char])
  extends AbstractGridImage[Char](w, h, d)