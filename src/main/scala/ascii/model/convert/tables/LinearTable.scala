package ascii.model.convert.tables

/**
 * Maps brightness to characters linearly using a custom character set.
 *
 * @param charsTable String of characters ordered from dark to light (or vice versa).
 */
class LinearTable(val charsTable: String) extends AsciiTable {
  require(charsTable.nonEmpty, "Table characters cannot be empty")

  /**
   * Maps brightness to a character by dividing the range [0, 255] into equal steps.
   */
  override def map(gray: Double): Char = {
    val step = 256.0 / charsTable.length
    val index = Math.min((gray / step).toInt, charsTable.length - 1)
    charsTable.charAt(index)
  }
}