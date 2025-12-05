package ascii.model.filter

/**
 * Generic functional interface for image transformations.
 *
 * @tparam I Input image type.
 * @tparam O Output image type.
 */
trait ImageFilter[I, O] {

  /**
   * Applies the transformation to the input image.
   */
  def apply(image: I): O
}