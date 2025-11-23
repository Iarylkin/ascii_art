package ascii.model.image

case class AsciiImageData(
                           override val width: Int,
                           override val height: Int,
                           content: String
                         ) extends ImageData(width, height)
