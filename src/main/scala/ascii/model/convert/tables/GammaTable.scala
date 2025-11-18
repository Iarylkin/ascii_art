package ascii.model.convert.tables

class GammaTable(val gamma: Double = 1.8) extends AsciiTable {
  private val charsTable = " .:-=+*#%@"

  override def map(gray: Double): Char = {
    val normalized = gray / 255.0
    val corrected = Math.pow(normalized, gamma)
    val index = (corrected * charsTable.length).toInt
    charsTable(Math.min(index, charsTable.length - 1))
  }
}
