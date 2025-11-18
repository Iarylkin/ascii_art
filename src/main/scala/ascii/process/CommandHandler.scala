package ascii.process

import ascii.modules.load.{FileImageSource, RandomImageSource}
import ascii.modules.filter._
import ascii.modules.save.{FileSaver, ConsoleSaver}

trait CommandHandler {
  def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit
}

class ImageSourceHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(throw new IllegalArgumentException("Path missing"))
    builder.withImageSource(new FileImageSource(path))
  }
}

class RandomImageHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.withImageSource(new RandomImageSource()) 
  }
}

class ScaleFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val s = cmd.params.headOption.flatMap(_.toDoubleOption)
      .getOrElse(throw new IllegalArgumentException("Invalid scale"))
    builder.addFilter(new ScaleFilter(s))
  }
}

class InvertFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.addFilter(new InvertFilter())
  }
}

class BrightnessFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val delta = cmd.params.headOption.flatMap(_.toIntOption)
      .getOrElse(throw new IllegalArgumentException("Invalid brightness delta"))
    builder.addFilter(new BrightnessFilter(delta))
  }
}

class FlipFilterHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val flipX = cmd.params.contains("x")
    val flipY = cmd.params.contains("y")
    if (!flipX && !flipY) throw new IllegalArgumentException("Flip requires 'x' and/or 'y'")
    builder.addFilter(new FlipFilter(flipX, flipY))
  }
}

class ConsoleOutputHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    builder.addSaver(new ConsoleSaver())
  }
}

class FileOutputHandler extends CommandHandler {
  override def handle(cmd: CommandDTO, builder: PipelineBuilder): Unit = {
    val path = cmd.params.headOption.getOrElse(throw new IllegalArgumentException("Output path missing"))
    builder.addSaver(new FileSaver(path))
  }
}