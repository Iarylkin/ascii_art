package ascii.model.image

/**
 * Represents a single immutable pixel in an RGB image.
 *
 * Each color channel must be within the range [0, 255].
 * An exception will be thrown if values are out of bounds.
 *
 * @param r Red component (0-255).
 * @param g Green component (0-255).
 * @param b Blue component (0-255).
 */
case class Pixel(r: Int, g: Int, b: Int) {
  require(r >= 0 && r <= 255, s"Red component must be between 0 and 255, but was $r")
  require(g >= 0 && g <= 255, s"Green component must be between 0 and 255, but was $g")
  require(b >= 0 && b <= 255, s"Blue component must be between 0 and 255, but was $b")
}
