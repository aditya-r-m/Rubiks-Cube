package pack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/* This class JImagePanel implements all of the drawing functionalities
 * It contains most of the functionality for rendering cube from the data structures
 * An object of JImagePanel will be created and placed inside a JFrame in main function to use it 
 */

@SuppressWarnings("serial")
public class JImagePanel extends JPanel {

	/*
	 * Since all the stickers on the faces of cube will look skewed (Rhombus) on
	 * parallel projection, using a polygon will be the simpler way to draw the
	 * border and fill the color
	 */
	Polygon T = new Polygon();

	/*
	 * Just the offset from the (0,0) point. Notice that the cube is always
	 * centered around the origin in space, It is during the drawing phase that
	 * cube is translated to a specified
	 */
	public static int offset = 350;

	// boolean array to keep track of which of the six faces are visible.
	// 'v' stands for visible
	static boolean v[] = new boolean[6];

	/*
	 * The following is the data structure of key interest drawingRect[][]
	 * serves as a list of all the stickers on the faces of cube, ordered by
	 * z-index thus, helping to make sure that the stickers on the front are
	 * drawn after the back ones during animation
	 * 
	 * drawingRect[][] is defined as follows, rows -> 6*(size)^2 == Total number
	 * of stickers on the cube column -> 3 == Index of face in array
	 * Global.face[], and indices in face[i].rect[][]
	 * 
	 * for example, to get the rectangle object at 'x'th index in
	 * drawingRect[][], you need the following syntax,
	 * 
	 * Global.face[drawingRect[i][0]].rect[drawingRect[i][1]][drawingRect[i][2]]
	 * 
	 * this type of indexing might not be the simplest or best approach, but
	 * allows enough flexibility and is used a lot to implement data structures.
	 */

	public static int drawingRect[][];

	// Image is used to implement double buffering since the code doesn't use the default one provided in repaint() method
	public static Image screen = null;
	public static Graphics2D gscreen;

	// boolean flag to keep track of when to start drawing
	public static boolean initialized = false;

	// Just the fonts styles
	static Font f1, f2;

	/*
	 * Constructor, mainly to initialize drawingRect[][] in accordance with
	 * above definition Notice the row and column count. initially, it's just
	 * set to contain indices for all the stickers (rectangles) without any
	 * regard to z-coordinate of the stickers
	 */

	public JImagePanel() {
		drawingRect = new int[(6 * (Global.size * Global.size))][3];
		int counter = 0;
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < Global.size; j++)
				for (int k = 0; k < Global.size; k++) {
					drawingRect[counter][0] = i;
					drawingRect[counter][1] = j;
					drawingRect[counter][2] = k;
					counter++;
				}
		f1 = new Font("Arial", Font.BOLD, 30);
		f2 = new Font("Arial", Font.BOLD, 15);
	}

	// Setting up the drawing environment
	// polygons will be drawn using gscreen onto the image screen, then drawn to panel
	public void initializeDoubleBufferingComponents() {

		screen = createImage(1024, 768);
		gscreen = (Graphics2D) screen.getGraphics();

		gscreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gscreen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		initialized = true;
	}
	
	/*
	 *  The name says it all, the following function is responsible for ordering the drawingRect[][] data structure
	 *  to keep it consistent with it's definition.
	 *  Notice that if rotation of a layer is being shown as animation during a turn, and current sticker is within the
	 *  slice being cycled, animatedCoordinatedSpace[][]
	 *  is referred to for the sticker's coordinates instead of coordinateSpace[][].
	 *  This is how flexible the references can be as long as the various coordinateSpace lists are consistent.
	 *  
	 *  Also, while calculating depths, instead of averaging depths of four vertices, i simply used summation as the
	 *  order should remain same in both cases
	 */
	public void sortByDepth() {
		int tempf, tempa, tempb;
		
		// traversal throughout the faces and stickers in corresponding rect[][] arrays to set depth values
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < Global.size; j++)
				for (int k = 0; k < Global.size; k++) {
					if (!Global.face[i].rect[j][k].beingCycled)
						Global.face[i].rect[j][k].depth = Global.coordinateSpace[Global.face[i].rect[j][k].a][2]
								+ Global.coordinateSpace[Global.face[i].rect[j][k].b][2]
								+ Global.coordinateSpace[Global.face[i].rect[j][k].c][2]
								+ Global.coordinateSpace[Global.face[i].rect[j][k].d][2];
					else
						Global.face[i].rect[j][k].depth = Global.animatedCoordinateSpace[Global.face[i].rect[j][k].a][2]
								+ Global.animatedCoordinateSpace[Global.face[i].rect[j][k].b][2]
								+ Global.animatedCoordinateSpace[Global.face[i].rect[j][k].c][2]
								+ Global.animatedCoordinateSpace[Global.face[i].rect[j][k].d][2];
				}
		
		// O(n^2) sort is good enough
		for (int i = 0; i < 6 * Global.size * Global.size; i++) {
			for (int j = 0; j < i; j++) {
				if (Global.face[drawingRect[i][0]].rect[drawingRect[i][1]][drawingRect[i][2]].depth > Global.face[drawingRect[j][0]].rect[drawingRect[j][1]][drawingRect[j][2]].depth) {
					tempf = drawingRect[i][0];
					tempa = drawingRect[i][1];
					tempb = drawingRect[i][2];

					drawingRect[i][0] = drawingRect[j][0];
					drawingRect[i][1] = drawingRect[j][1];
					drawingRect[i][2] = drawingRect[j][2];

					drawingRect[j][0] = tempf;
					drawingRect[j][1] = tempa;
					drawingRect[j][2] = tempb;
				}
			}
		}
	}

	// if the cube is not being twisted, it's much easier to find which stickers will be displayed.
	// only the front three faces need to be drawn and so, v[] values are adjusted complementarily for opposite faces
	// though the function is not being used in current version, it is kept for future optimizations
	public void backFaceCulling() {
		double depth[] = new double[6];
		for (int i = 0; i < 6; i++) {
			depth[i] = (Global.coordinateSpace[Global.face[i].fa][2]
					+ Global.coordinateSpace[Global.face[i].fb][2]
					+ Global.coordinateSpace[Global.face[i].fc][2] + Global.coordinateSpace[Global.face[i].fd][2]) / 4;
		}

		// white vs yellow
		if (depth[0] < depth[1]) {
			v[0] = true;
			v[1] = false;
		} else {
			v[1] = true;
			v[0] = false;
		}
		
		// green vs blue
		if (depth[2] < depth[3]) {
			v[2] = true;
			v[3] = false;
		} else {
			v[3] = true;
			v[2] = false;
		}
		
		// red vs orange
		if (depth[4] < depth[5]) {
			v[4] = true;
			v[5] = false;
		} else {
			v[5] = true;
			v[4] = false;
		}

	}

	// The code that does the most visually appealing work
	public void DrawCube(Graphics g) {
		int fno, i, j;

		// clearing the screen
		gscreen.setColor(Color.BLACK);
		gscreen.fillRect(0, 0, 1024, 768);
		
		// traverse through the drawingRect[][] array, note that a cube with size 's' has 6*(s)^2 stickers
		for (int a = 0; a < (6 * Global.size * Global.size); a++) {

			// variables to store faceNo and row and column number for current Sticker
			fno = drawingRect[a][0];
			i = drawingRect[a][1];
			j = drawingRect[a][2];
			
			// if not being animated in rotating layer, refer to coordinateSpace[][]
			if (!Global.face[fno].rect[i][j].beingCycled) {
				// add the vertices a,b,c,d, and ignore z-indices (coordinateSpace[][2]) for simple parallel projection
				T.addPoint(
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].a][0] + offset),
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].a][1] + offset));
				T.addPoint(
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].b][0] + offset),
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].b][1] + offset));
				T.addPoint(
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].c][0] + offset),
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].c][1] + offset));
				T.addPoint(
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].d][0] + offset),
						(int) (Global.coordinateSpace[Global.face[fno].rect[i][j].d][1] + offset));
			} else {
				// else refer to the animatedCoordinateSpace[][]
				/*
				 * This simple approach enables us to display a layer entirely independent of other layers,
				 * even though every sticker shares the value indices referring to it's vertices location in 
				 * coordinate-space-arrays with exactly 8 other adjacent stickers
				 */
				
				// add the vertices a,b,c,d, and ignore z-indices (coordinateSpace[][2]) for simple parallel projection
				T.addPoint(
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].a][0] + offset),
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].a][1] + offset));
				T.addPoint(
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].b][0] + offset),
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].b][1] + offset));
				T.addPoint(
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].c][0] + offset),
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].c][1] + offset));
				T.addPoint(
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].d][0] + offset),
						(int) (Global.animatedCoordinateSpace[Global.face[fno].rect[i][j].d][1] + offset));
			}

			// once the points are added to T in proper order, draw and fill color
			gscreen.setColor(Global.face[fno].rect[i][j].color);
			gscreen.fillPolygon(T);
			gscreen.setColor(Color.BLACK);
			gscreen.drawPolygon(T);
			
			// reset it for next iteration
			T.reset();
		}
		
		// code to show solve button
		if (!StartingClass.solved && !StartingClass.solutionPrepared) {
			gscreen.setColor(Color.WHITE);
			gscreen.setFont(f1);
			gscreen.drawString("Solve", 550, 100);
		}
		
		// code to show move list controls
		if (StartingClass.solutionPrepared) {
			gscreen.setFont(f1);
			gscreen.setColor(Color.WHITE);
			gscreen.drawString(">", 450, 600);
			gscreen.drawString("<", 250, 600);

			gscreen.drawString(">|", 550, 600);
			gscreen.drawString("|<", 150, 600);

			gscreen.setFont(f2);
			gscreen.drawString("Phase-" + Global.cPhase + ":", 290, 595);
			gscreen.drawString((Global.movesMade.top + 1) + "/"
					+ (Global.movesMade.top + Global.movesToMake.top + 2), 365,
					595);
		}
		
		// feature disabled -> code to show back button
		//gscreen.setColor(Color.WHITE);
		//gscreen.setFont(f1);
		//gscreen.drawString("Back", 50, 100);
		
		// finally paint the so formed image to the screen
		g.drawImage(screen, 0, 0, this);

	}

	// the following code is called from the drawing thread every few milliseconds or so
	public void paintComponent(Graphics g) {

		// don't try to draw unless everything is initialized
		if (initialized) {
			
			// sync the coordinate space array to the always stationary baseCoordinateSpace[]
			for (int i = 0; i < Global.nco; i++)
				for (int j = 0; j < 3; j++)
					Global.coordinateSpace[i][j] = Global.baseCoordinateSpace[i][j];
			
			// set the current orientation of cube as set by user
			Global.thetaX = Global.thetaXSum;
			Global.thetaY = Global.thetaYSum;

			// orient the coordinateSpace[][] array according to how user has rotated the cube's view
			Global.rotateY();
			Global.rotateX();

			//backFaceCulling();  <------- reference for future optimization.
			
			// sort stickers and draw them!
			sortByDepth();
			DrawCube(g);
		}
	}

}
