package ascii.model.convert.tables

/**
 * Registry for retrieving ASCII conversion tables by name.
 */
class TableRegistry {
  private val tables: Map[String, () => AsciiTable] = Map(
    "bourke-small" -> (() => new PaulBourkeSmallTable()),
    "gamma"        -> (() => new GammaTable())
  )

  /**
   * Returns a new instance of the requested table if found.
   */
  def getTable(name: String): Option[AsciiTable] = {
    tables.get(name.toLowerCase).map(_.apply())
  }

  /**
   * Returns the default conversion table.
   */
  def defaultTable: AsciiTable = new PaulBourkeSmallTable()

  /**
   * Lists the names of all registered tables.
   */
  def listAvailable: Iterable[String] = tables.keys
}