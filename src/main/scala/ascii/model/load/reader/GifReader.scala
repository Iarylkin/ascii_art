package ascii.model.load.reader

/**
 * Image reader implementation for GIF files.
 *
 * Delegates the actual loading to the parent BufferedImageReader,
 * specifying the "gif" extension.
 */
class GifReader extends BufferedImageReader("gif", Set("gif"))