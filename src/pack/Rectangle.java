package pack;

import java.awt.Color;

/*
 * The objects of Rectangle class will be our stickers and thus, the basic building blocks of the application
 * Since the stickers are squares in 3-d space, and appear on projection as Rhomboidal, the name of class might be confusing
 * it's simply a rather unfortunate naming convention i made and got stuck with
 * 
 * In comments, sticker always implies an object of the following class
 */
public class Rectangle {

	/*
	 * The object never stores the actual coordinates in space, just an index
	 * which when given to coordinate-space-arrays, will give the corresponding x,y and z values
	 * This approach provides enough flexibility for various purposes
	 */
	
	public int a, b, c, d;
	
	// color of the sticker
	public Color color;
	
	// useful boolean flags
	public boolean markedInFaceCycle, beingCycled = false;
	
	// reference to horizontal and vertical cycles which span over the sticker
	public int pointingCycle[];
	
	// used for sortByDepth() in drawing procedures
	public double depth;

	// initial values
	public Rectangle() {
		
		// the order of the four vertices is of much concern, though they are initialized to simply the first entry of coordinate-array
		a = 0;
		b = 0;
		c = 0;
		d = 0;
		
		// all of these are just dummy values which will be set and used later on
		depth = 0;
		markedInFaceCycle = false;
		pointingCycle = new int[2];
		pointingCycle[0] = -1;
		pointingCycle[1] = -1;
		color = Color.BLACK;
	}
	
	// the setter method which truly initializes as sticker
	// the other data members will be set up properly when other data structures such as cycles are set up
	public void setRectangle(int ac, int bc, int cc, int dc, Color colorc) {
		a = ac;
		b = bc;
		c = cc;
		d = dc;
		color = colorc;
	}

}
