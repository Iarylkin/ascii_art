package ascii.model.load

import ascii.model.image.{Pixel, RgbImageData}

import java.io.File
import scala.util.Random

trait ImageSource {
  def load() : RgbImageData
}