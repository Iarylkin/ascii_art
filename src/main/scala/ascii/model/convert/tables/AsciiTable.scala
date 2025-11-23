package ascii.model.convert.tables

trait AsciiTable {
  def map(gray: Double): Char
}
