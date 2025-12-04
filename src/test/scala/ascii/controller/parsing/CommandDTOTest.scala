package ascii.controller.parsing

import org.scalatest.funsuite.AnyFunSuite

class CommandDTOTest extends AnyFunSuite {

  test("Should create CommandDTO with name and parameters") {
    val cmd = CommandDTO("image", List("test.jpg"))
    assert(cmd.name == "image")
    assert(cmd.params == List("test.jpg"))
  }

  test("Should verify default parameters are empty list") {
    val cmd = CommandDTO("invert")
    assert(cmd.name == "invert")
    assert(cmd.params.isEmpty)
    assert(cmd.params == Nil)
  }

  test("Should be equal to another identical CommandDTO (Equality check)") {
    val cmd1 = CommandDTO("scale", List("0.25"))
    val cmd2 = CommandDTO("scale", List("0.25"))

    assert(cmd1 == cmd2)
  }
}