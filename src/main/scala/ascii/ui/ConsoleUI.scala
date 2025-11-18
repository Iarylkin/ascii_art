package ascii.ui

import ascii.process.CommandDTO

object ConsoleUI {

  private val parser = new CommandLineParser()

  def start(args: Array[String]): List[CommandDTO] = {
    if (args.isEmpty) {
      println("Usage: run --image <path> [--rotate <deg>] [--scale <val>] ...")
      return List.empty
    }
    parser.parse(args.toList)
  }
}
