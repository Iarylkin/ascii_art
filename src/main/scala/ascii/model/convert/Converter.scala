package ascii.model.convert

import ascii.model.image.ImageData

/**
 * Generic interface for converting between image types.
 */
trait Converter[I <: ImageData[?], O <: ImageData[?]] {

  /**
   * Converts the input image to the target format.
   */
  def convert(input: I): O
}