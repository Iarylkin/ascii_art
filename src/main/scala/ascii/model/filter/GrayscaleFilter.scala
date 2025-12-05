package ascii.model.filter

import ascii.model.image.GrayscaleImageData

/**
 * Interface for filters that operate specifically on grayscale images.
 */
trait GrayscaleFilter extends ImageFilter[GrayscaleImageData, GrayscaleImageData]