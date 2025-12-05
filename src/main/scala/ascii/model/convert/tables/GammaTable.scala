package ascii.model.convert.tables

/**
 * Maps brightness to characters using non-linear gamma correction.
 *
 * @param gamma Correction factor (default 1.8). Values > 1 darken midtones.
 */
class GammaTable(val gamma: Double = 1.8) extends AsciiTable {
  private val charsTable = " .:-=+*#%@"

  /**
   * Applies gamma correction and maps the result to a character.
   */
  override def map(gray: Double): Char = {
    val normalized = gray / 255.0
    val corrected = Math.pow(normalized, gamma)
    val index = (corrected * charsTable.length).toInt
    charsTable(Math.min(index, charsTable.length - 1))
  }
}