package net.imglib2.examples;

import java.io.File;

import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.io.ImgOpener;

import ij.ImageJ;
import ij.ImagePlus;

/**
 * Here we want to copy an Image into another with a different Container one using a generic method,
 * using a Localizable and a LocalizableByDimCursor
 *
 * @author Stephan Preibisch &amp; Stephan Saalfeld
 *
 */
public class Example2c
{
	public Example2c()
	{
		// define the file to open
		File file = new File( "DrosophilaWing.tif" );

		// open with ImgOpener using an ArrayContainer
		Img<FloatType> image = new ImgOpener().openLOCIFloatType( file.getAbsolutePath(), new ArrayImgFactory<T>() );

		// copy the image
		Img<FloatType> duplicate = copyImage( image, new CellContainerFactory( 20 ) );

		// display the copy
		final ImagePlus imp = ImageJFunctions.show( duplicate );
		imp.resetDisplayRange();
		imp.show();
	}

	public <T extends Type<T>> Img<T> copyImage( final Img<T> input, final ContainerFactory containerFactory )
	{
		// create a new Image with the same dimensions
		ImageFactory<T> imageFactory = new ImageFactory<T>( input.createType(), containerFactory );
		Img<T> output = imageFactory.createImage( input.getDimensions(), "Copy of " + input.getName() );

		// create a cursor for both images
		LocalizableCursor<T> cursorInput = input.createLocalizableCursor();
		LocalizableByDimCursor<T> cursorOutput = output.createLocalizableByDimCursor();

		// iterate over the input cursor
		while ( cursorInput.hasNext() )
		{
			// move input cursor forward
			cursorInput.fwd();

			// set the output cursor to the position of the input cursor
			cursorOutput.setPosition( cursorInput );

			// set the value of this pixel of the output image, every Type supports T.set( T type )
			cursorOutput.getType().set( cursorInput.getType() );
		}

		// close the cursors
		cursorInput.close();
		cursorOutput.close();

		//. return the copy
		return output;
	}

	public static void main( String[] args )
	{
		// open an ImageJ window
		new ImageJ();

		// run the example
		new Example2c();
	}
}
