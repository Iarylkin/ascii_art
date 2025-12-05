package ascii.model.convert.tables

import org.scalatest.funsuite.AnyFunSuite

class GammaTableTest extends AnyFunSuite {

  test("Should map boundaries correctly (0 -> first char, 255 -> last char)") {
    // Arrange
    val table = new GammaTable(1.8)
    val firstChar = ' '
    val lastChar = '@'

    // Act & Assert
    assert(table.map(0) == firstChar)
    assert(table.map(255) == lastChar)
  }

  test("Should respect gamma value (Linear vs High Gamma)") {
    // Arrange
    // Gamma 1.0 should act linearly. Middle grey (approx 127) should be in the middle of the char set.
    val linearGamma = new GammaTable(1.0)

    // Gamma 2.0 makes image darker (pushes values towards lower indices for same input)
    val darkGamma = new GammaTable(2.0)

    val inputGray = 127.0 // Middle brightness

    // Act
    val charLinear = linearGamma.map(inputGray)
    val charDark = darkGamma.map(inputGray)

    assert(charLinear != charDark, "Different gamma values should produce different mappings for mid-tones")
  }
}