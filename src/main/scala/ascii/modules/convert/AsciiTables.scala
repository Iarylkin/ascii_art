package ascii.modules.convert

trait AsciiTable {
  def map(gray: Double): Char
}

class LinearTable(val charsTable: String = " .:-=+*#%@") extends AsciiTable {
  override def map(gray: Double): Char = {
    val step = 256.0 / charsTable.length
    val index = Math.min((gray / step).toInt, charsTable.length - 1)
    charsTable.charAt(index)
  }
}

class GammaTable(val gamma: Double = 1.8) extends AsciiTable {
  private val charsTable = " .:-=+*#%@"

  override def map(gray: Double): Char = {
    val normalized = gray / 255.0
    val corrected = Math.pow(normalized, gamma)
    val index = (corrected * charsTable.length).toInt
    charsTable(Math.min(index, charsTable.length - 1))
  }
}
