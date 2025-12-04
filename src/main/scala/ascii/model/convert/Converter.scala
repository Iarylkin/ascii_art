package ascii.model.convert

import ascii.model.image.ImageData

trait Converter[I <: ImageData[?], O <: ImageData[?]] {
  def convert(input: I): O
}