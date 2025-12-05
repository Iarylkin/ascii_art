package ascii.model.convert.tables

/**
 * Interface for mapping brightness values to characters.
 */
trait AsciiTable {

  /**
   * Maps a grayscale value to a character.
   */
  def map(gray: Double): Char
}