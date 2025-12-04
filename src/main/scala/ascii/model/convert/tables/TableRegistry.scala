package ascii.model.convert.tables

class TableRegistry {
  private val tables: Map[String, () => AsciiTable] = Map(

    "bourke-small" -> (() => new PaulBourkeSmallTable()),

    "gamma"  -> (() => new GammaTable())
  )

  def getTable(name: String): Option[AsciiTable] = {
    tables.get(name.toLowerCase).map(_.apply())
  }

  def defaultTable: AsciiTable = new PaulBourkeSmallTable()

  def listAvailable: Iterable[String] = tables.keys
}
