package ascii.model.filter

import ascii.model.image.GrayscaleImageData

class InvertFilter extends GrayscaleFilter {
  override def apply(image: GrayscaleImageData): GrayscaleImageData = {
    val invertedPixels = image.getData.map(p => 255 - p)
    
    GrayscaleImageData(image.width, image.height, invertedPixels)
  }
}
