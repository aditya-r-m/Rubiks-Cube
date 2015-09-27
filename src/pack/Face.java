package pack;

import java.awt.Color;

/*
 * The application will always have exactly six objects of the following class in Global.face[]
 * Each face contains size*size objects of rectangle class which each represent a sticker
 */
public class Face {

	// 2-d Array of stickers
	public Rectangle rect[][];
	
	// indices of coordinates of four corners of a face
	int fa, fb, fc, fd;

	// initializing all values of object without any regard to color or location
	public Face() {

		fa = 0;
		fb = 0;
		fc = 0;
		fd = 0;

		rect = new Rectangle[Global.size][Global.size];
		for (int i = 0; i < Global.size; i++)
			for (int j = 0; j < Global.size; j++) {
				rect[i][j] = new Rectangle();
			}
	}

	/*
	 * setFace() assigns proper values to the data members of face objects and it's stickers
	 * 
	 * The following convention is used,
	 * 
	 * fno -> color
	 *  0 -> white
	 *  1 -> yellow
	 *  2 -> green
	 *  3 -> blue
	 *  4 -> red
	 *  5 -> orange
	 *  
	 *  note that it's just the initial configuration, after scrambling and solving,
	 *  the above mapping need not be applicable
	 *  
	 *  The loops which use rect[][].setRectangle() use helper method from Global class (findIndex())
	 *  to find the index from location of vertices that should be set to the a,b,c,d values of sticker
	 *  
	 *  findIndex() is essentially inverse of coordinate-space-arrays
	 *  those arrays map a vertex index to the coordinate values,
	 *  while findIndex() maps coordinate values to vertex index.
	 *  
	 *  the method findIndex() is intended to be used just for initialization purpose and nothing more.
	 *  Not the best approach, but works as expected
	 *  
	 *  Also, here, the values given to findIndex() are given with assumption that the cube's corner vertex is
	 *  on origin and whole cube lies on +x+y+z octant, while in coordinate-space-arrays, the cube's body center is supposed to be the origin
	 *  findIndex() makes the necessary adjustment by translating the given coordinate values
	 *  
	 *  the order and value of arguments passed to findIndex() method are the key to ensure proper structure of
	 *  cube as well as proper drawing on Panel, since the vertices of sticker must be in cyclic order for
	 *  proper drawing of the polygon.
	 *  if any two indices such as rect[][].a or rect[][].b are switched, the sticker would not be drawn as
	 *  expected.
	 */
	public void setFace(int fno) {
		if (fno == 0 || fno == 1) {
			Color colorc;

			if (fno == 0)
				colorc = Color.WHITE;
			else
				colorc = Color.YELLOW;
			for (int i = 0; i < Global.size; i++)
				for (int j = 0; j < Global.size; j++) {
					rect[i][j].setRectangle(
							Global.findIndex(i * Global.length, j
									* Global.length, (Global.size)
									* Global.length * fno),
							Global.findIndex((i + 1) * Global.length, j
									* Global.length, (Global.size)
									* Global.length * fno),
							Global.findIndex((i + 1) * Global.length, (j + 1)
									* Global.length, (Global.size)
									* Global.length * fno),
							Global.findIndex(i * Global.length, (j + 1)
									* Global.length, (Global.size)
									* Global.length * fno), colorc);

				}
		} else if (fno == 3 || fno == 2) {

			Color colorc;
			int s = 0;
			if (fno == 3)
				colorc = Color.BLUE;
			else
				colorc = Color.GREEN;
			if (fno == 2)
				s = 1;
			for (int i = 0; i < Global.size; i++)
				for (int j = 0; j < Global.size; j++) {
					rect[i][j].setRectangle(Global.findIndex((Global.size)
							* Global.length * s, i * Global.length, j
							* Global.length), Global.findIndex((Global.size)
							* Global.length * s, (i + 1) * Global.length, j
							* Global.length), Global.findIndex((Global.size)
							* Global.length * s, (i + 1) * Global.length,
							(j + 1) * Global.length), Global.findIndex(
							(Global.size) * Global.length * s, i
									* Global.length, (j + 1) * Global.length),
							colorc);

				}

		} else if (fno == 4 || fno == 5) {
			int s = 0;
			Color colorc;
			if (fno == 4)
				colorc = Color.RED;
			else
				colorc = new Color(255, 140, 0);
			if (fno == 5)
				s = 1;
			for (int i = 0; i < Global.size; i++)
				for (int j = 0; j < Global.size; j++) {
					rect[i][j].setRectangle(Global.findIndex(i * Global.length,
							(Global.size) * Global.length * s, j
									* Global.length), Global.findIndex((i + 1)
							* Global.length, (Global.size) * Global.length * s,
							j * Global.length), Global.findIndex((i + 1)
							* Global.length, (Global.size) * Global.length * s,
							(j + 1) * Global.length), Global.findIndex(i
							* Global.length, (Global.size) * Global.length * s,
							(j + 1) * Global.length), colorc);

				}

		}
	}
}
