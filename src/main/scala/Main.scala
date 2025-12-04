package Main

import ascii.controller.Controller
import ascii.view.ConsoleView


@main def main(args: String*): Unit = {
  val ui = new ConsoleView()

  val controller = new Controller(ui)

  controller.run(args.toArray)
}

