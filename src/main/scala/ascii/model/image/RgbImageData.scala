package ascii.model.image

import ascii.model.image.ImageData


case class RgbImageData(
                         override val width: Int,
                         override val height: Int,
                         pixels: Array[Pixel],
                         format: String
                       ) extends ImageData(width, height)
