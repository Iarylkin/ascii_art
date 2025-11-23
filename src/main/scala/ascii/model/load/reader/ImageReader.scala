package ascii.model.load.reader

import ascii.model.image.{Pixel, RgbImageData}
import ascii.model.load.*

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

trait ImageReader {
  def extensions: Set[String]
  def read(file: File): RgbImageData
}
