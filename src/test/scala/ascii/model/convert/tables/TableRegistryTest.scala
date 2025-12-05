package ascii.model.convert.tables

import org.scalatest.funsuite.AnyFunSuite

class TableRegistryTest extends AnyFunSuite {

  private val registry = new TableRegistry()

  test("Should retrieve 'gamma' table") {
    registry.getTable("gamma") match {
      case Some(_: GammaTable) => succeed
      case Some(other) => fail(s"Expected GammaTable, but got ${other.getClass.getSimpleName}")
      case None => fail("Expected GammaTable, but got None")
    }
  }

  test("Should retrieve 'bourke-small' table") {
    registry.getTable("bourke-small") match {
      case Some(_: PaulBourkeSmallTable) => succeed
      case Some(other) => fail(s"Expected PaulBourkeSmallTable, but got ${other.getClass.getSimpleName}")
      case None => fail("Expected PaulBourkeSmallTable, but got None")
    }
  }

  test("Should be case insensitive") {
    registry.getTable("GAMMA") match {
      case Some(_) => succeed
      case None => fail("Should find table even with uppercase name")
    }
  }

  test("Should return None for unknown tables") {
    assert(registry.getTable("non-existent-table").isEmpty)
  }

  test("Should return a valid default table") {
    val default = registry.defaultTable
    assert(default != null)

    // Check if default is indeed PaulBourkeSmallTable (if that's the logic)
    default match {
      case _: PaulBourkeSmallTable => succeed
      case _ => fail("Default table should be PaulBourkeSmallTable")
    }
  }

  test("Should list available tables") {
    val list = registry.listAvailable.toSeq
    assert(list.contains("gamma"))
    assert(list.contains("bourke-small"))
  }
}