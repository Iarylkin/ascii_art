package ascii.model.convert

import ascii.model.image.ImageData

trait Converter[I <: ImageData[_], O <: ImageData[_]] {
  def convert(input: I): O
}