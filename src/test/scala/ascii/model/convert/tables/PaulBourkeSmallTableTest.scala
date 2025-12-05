package ascii.model.convert.tables

import org.scalatest.funsuite.AnyFunSuite

class PaulBourkeSmallTableTest extends AnyFunSuite {

  test("Should use correct default character set") {
    // Arrange
    val table = new PaulBourkeSmallTable()
    val expectedChars = " .:-=+*#%@"

    // Act & Assert
    // Verify boundaries to ensure it uses the full length of the expected string
    assert(table.map(0) == expectedChars.head)
    assert(table.map(255) == expectedChars.last)
  }
}