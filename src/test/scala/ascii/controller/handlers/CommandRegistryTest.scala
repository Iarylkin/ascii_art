package ascii.controller.handlers

import org.scalatest.funsuite.AnyFunSuite

class CommandRegistryTest extends AnyFunSuite {

  private val registry = new CommandRegistry()

  test("Should return correct handler for known commands") {
    registry.getHandler("image") match {
      case Some(_: ImageSourceHandler) => succeed
      case other => fail(s"Expected ImageSourceHandler for 'image', but got $other")
    }

    registry.getHandler("scale") match {
      case Some(_: ScaleFilterHandler) => succeed
      case other => fail(s"Expected ScaleFilterHandler for 'scale', but got $other")
    }

    registry.getHandler("table") match {
      case Some(_: AsciiTableHandler) => succeed
      case other => fail(s"Expected AsciiTableHandler for 'table', but got $other")
    }

    registry.getHandler("output-console") match {
      case Some(_: ConsoleOutputHandler) => succeed
      case other => fail(s"Expected ConsoleOutputHandler for 'output-console', but got $other")
    }
  }

  test("Should return None for unknown commands") {
    assert(registry.getHandler("unknown-command").isEmpty)
  }
}