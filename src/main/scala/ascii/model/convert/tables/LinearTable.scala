package ascii.model.convert.tables

class LinearTable(val charsTable: String) extends AsciiTable {
  require(charsTable.nonEmpty, "Table characters cannot be empty")
  
  override def map(gray: Double): Char = {
    
    val step = 256.0 / charsTable.length
    val index = Math.min((gray / step).toInt, charsTable.length - 1)
    charsTable.charAt(index)
  }
}
