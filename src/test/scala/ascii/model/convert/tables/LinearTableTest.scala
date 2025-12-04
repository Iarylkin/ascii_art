package ascii.model.convert.tables

import org.scalatest.funsuite.AnyFunSuite

class LinearTableTest extends AnyFunSuite {

  test("Should throw IllegalArgumentException when chars string is empty") {
    assertThrows[IllegalArgumentException] {
      new LinearTable("")
    }
  }

  test("Should map 0 to the first character and 255 to the last") {
    // Arrange
    val chars = "ABC"
    val table = new LinearTable(chars)

    // Act & Assert
    assert(table.map(0) == 'A', "0 should map to first char")
    assert(table.map(255) == 'C', "255 should map to last char")
  }

  test("Should distribute values evenly (2 chars table)") {
    // Arrange
    // 2 chars: "A" covers [0, 127], "B" covers [128, 255] (step is 128.0)
    val table = new LinearTable("AB")

    // Act & Assert
    assert(table.map(0) == 'A')
    assert(table.map(127) == 'A') // Upper bound of first half

    assert(table.map(128) == 'B') // Lower bound of second half
    assert(table.map(255) == 'B')
  }

  test("Should handle double input correctly") {
    val table = new LinearTable("AB")
    // 127.9 should still technically be in the first bucket if using floor/cast
    // Logic: 127.9 / 128.0 = 0.999 -> toInt -> 0
    assert(table.map(127.9) == 'A')
  }
}