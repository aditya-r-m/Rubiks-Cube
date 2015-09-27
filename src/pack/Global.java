package pack;

import java.awt.Color;
import java.awt.Image;
import java.awt.Polygon;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class Global {

	// most of the shared variables
	public static int chosenX, chosenY;
	public static boolean useAnimation = true;
	public static boolean inMenu = true;
	public static int size, length, nco; // size of cube, length of sticker's
											// edge, and number of coordinates
											// of interest
	public static double thetaX = 0.02, thetaY = 0.02, thetaZ = 0,
			thetaX2 = 0.01, thetaY2 = 0.01, thetaZ2 = 0.01, thetaX2Sum = 0,
			thetaY2Sum = 0, thetaZ2Sum = 0, thetaXSum = 0, thetaYSum = 0,
			xIncrement = 0.05, yIncrement = 0.05; // various theta values for
													// various rotations

	/*
	 * baseCoordinateSpace -> never altered after init, serves as a reference
	 * coordinateSpace -> rotated baseCoordinate space accordin to thetaXSum and
	 * thetaYSym for main display of cube's body
	 * 
	 * rotatingCoordinateSpace -> derived from baseCoordinateSpace by applying
	 * continuous rotation effect alog x,y or z axis animatedCoordinateSpace ->
	 * derived from rotatingCoordinateSpace by applying view based rotation
	 * 
	 * this makes animatedCoordinateSpace extremely useful for animating
	 * rotation of slices of cube
	 */
	public static double coordinateSpace[][], baseCoordinateSpace[][],
			rotatingCoordinateSpace[][], animatedCoordinateSpace[][];

	// the face array contains the raw data of the details of cube, everything
	// else just references it
	// it always has strictly six elements
	public static Face face[];

	// all the individual twists that can be applied on the cube
	public static Cycle cycle[];

	// some other flags and variables for user interface purpose
	public static boolean grabbed = false, twisting = false, rotateX = false,
			rotateY = false, direction;

	public static int oldCX = 0, oldCY = 0, newCX = 0, newCY = 0;

	// shared variables to communicate data between some functions
	public static int fno[] = new int[2], a[] = new int[2], b[] = new int[2];
	public static int affno, afa, afb;
	public static int idf = -1, idi = -1, idj = -1;
	public static int initrectf, initrecti, initrectj, finalrectf, finalrecti,
			finalrectj, cycno;
	public static int index[] = new int[2];

	// stacks to store solution details
	public static MoveStack movesMade = new MoveStack(),
			movesToMake = new MoveStack(), reverseMovesMade = new MoveStack();

	// the following function rotates the points of array coordinateSpace (and
	// in turn, the displayed location of stationary cube's points)
	// the angle of rotation is given by the variable thetaX and rotation is
	// along x-axis
	// notice that coordinateSpace[w][0] is the x-coordinate of point
	// 'w',coordinateSpace[w][1] is y-coordinate of point 'w'
	// and coordinateSpace[w][2] is the z-coordinate of point 'w'
	public static void rotateX() {
		for (int w = 0; w < nco; w++) {
			double oldY = coordinateSpace[w][1];
			double oldZ = coordinateSpace[w][2];
			coordinateSpace[w][1] = oldY * Math.cos(thetaX) - oldZ
					* Math.sin(thetaX);
			coordinateSpace[w][2] = oldY * Math.sin(thetaX) + oldZ
					* Math.cos(thetaX);

		}
	}

	// the following function rotates the points of global array coordinateSpace
	// (and in turn, the displayed location of stationary cube's points)
	// the angle of rotation is given by the variable thetaY and rotation is
	// about y-axis
	public static void rotateY() {
		for (int w = 0; w < nco; w++) {
			double oldX = coordinateSpace[w][0];
			double oldZ = coordinateSpace[w][2];

			coordinateSpace[w][0] = oldZ * Math.sin(thetaY) + oldX
					* Math.cos(thetaY);
			coordinateSpace[w][2] = oldZ * Math.cos(thetaY) - oldX
					* Math.sin(thetaY);

		}

	}

	// the following function rotates the points of global array coordinateSpace
	// (and in turn, the displayed location of stationary cube's points)
	// the angle of rotation is given by the variable thetaX and rotation is
	// about z-axis
	public static void rotateX2() {
		for (int w = 0; w < nco; w++) {
			double oldY = rotatingCoordinateSpace[w][1];
			double oldZ = rotatingCoordinateSpace[w][2];
			rotatingCoordinateSpace[w][1] = oldY * Math.cos(thetaX2) - oldZ
					* Math.sin(thetaX2);
			rotatingCoordinateSpace[w][2] = oldY * Math.sin(thetaX2) + oldZ
					* Math.cos(thetaX2);

		}
	}

	public static void rotateY2() {
		for (int w = 0; w < nco; w++) {
			double oldX = rotatingCoordinateSpace[w][0];
			double oldZ = rotatingCoordinateSpace[w][2];

			oldZ = rotatingCoordinateSpace[w][2];
			rotatingCoordinateSpace[w][0] = oldZ * Math.sin(thetaY2) + oldX
					* Math.cos(thetaY2);
			rotatingCoordinateSpace[w][2] = oldZ * Math.cos(thetaY2) - oldX
					* Math.sin(thetaY2);

		}

	}

	public static void rotateZ2() {

		for (int w = 0; w < nco; w++) {
			double oldX = rotatingCoordinateSpace[w][0];
			double oldY = rotatingCoordinateSpace[w][1];
			rotatingCoordinateSpace[w][0] = oldX * Math.cos(thetaZ2) - oldY
					* Math.sin(thetaZ2);
			rotatingCoordinateSpace[w][1] = oldX * Math.sin(thetaZ2) + oldY
					* Math.cos(thetaZ2);
		}
	}

	public static void rotateX3() {
		for (int w = 0; w < nco; w++) {
			double oldY = animatedCoordinateSpace[w][1];
			double oldZ = animatedCoordinateSpace[w][2];
			animatedCoordinateSpace[w][1] = oldY * Math.cos(thetaXSum) - oldZ
					* Math.sin(thetaXSum);
			animatedCoordinateSpace[w][2] = oldY * Math.sin(thetaXSum) + oldZ
					* Math.cos(thetaXSum);

		}
	}

	public static void rotateY3() {
		for (int w = 0; w < nco; w++) {
			double oldX = animatedCoordinateSpace[w][0];
			double oldZ = animatedCoordinateSpace[w][2];

			oldZ = animatedCoordinateSpace[w][2];
			animatedCoordinateSpace[w][0] = oldZ * Math.sin(thetaYSum) + oldX
					* Math.cos(thetaYSum);
			animatedCoordinateSpace[w][2] = oldZ * Math.cos(thetaYSum) - oldX
					* Math.sin(thetaYSum);
		}

	}

	// helper method used to initialize rectangles
	public static int findIndex(double x, double y, double z) {
		for (int i = 0; i < nco; i++) {
			if ((coordinateSpace[i][0] == x
					- ((Global.size * Global.length) / 2))
					&& (coordinateSpace[i][1] == y
							- ((Global.size * Global.length) / 2))
					&& coordinateSpace[i][2] == z
							- ((Global.size * Global.length) / 2)) {
				return i;
			}
		}
		return -1;
	}

	// helper method to find shared vertices between two sticker [f1,a1,b1] and
	// [f2,a2,b2]
	// the result is store in global array index[];
	public static boolean findIndex(int f1, int a1, int b1, int f2, int a2,
			int b2) {

		int iterator = 0;
		if (face[f1].rect[a1][b1].a == face[f2].rect[a2][b2].a) {
			index[iterator] = face[f1].rect[a1][b1].a;
			iterator++;
		} else if (face[f1].rect[a1][b1].a == face[f2].rect[a2][b2].b) {
			index[iterator] = face[f1].rect[a1][b1].a;
			iterator++;
		} else if (face[f1].rect[a1][b1].a == face[f2].rect[a2][b2].c) {
			index[iterator] = face[f1].rect[a1][b1].a;
			iterator++;
		} else if (face[f1].rect[a1][b1].a == face[f2].rect[a2][b2].d) {
			index[iterator] = face[f1].rect[a1][b1].a;
			iterator++;
		}

		if (face[f1].rect[a1][b1].b == face[f2].rect[a2][b2].a) {
			index[iterator] = face[f1].rect[a1][b1].b;
			iterator++;
		} else if (face[f1].rect[a1][b1].b == face[f2].rect[a2][b2].b) {
			index[iterator] = face[f1].rect[a1][b1].b;
			iterator++;
		} else if (face[f1].rect[a1][b1].b == face[f2].rect[a2][b2].c) {
			index[iterator] = face[f1].rect[a1][b1].b;
			iterator++;
		} else if (face[f1].rect[a1][b1].b == face[f2].rect[a2][b2].d) {
			index[iterator] = face[f1].rect[a1][b1].b;
			iterator++;
		}

		if (face[f1].rect[a1][b1].c == face[f2].rect[a2][b2].a) {
			index[iterator] = face[f1].rect[a1][b1].c;
			iterator++;
		} else if (face[f1].rect[a1][b1].c == face[f2].rect[a2][b2].b) {
			index[iterator] = face[f1].rect[a1][b1].c;
			iterator++;
		} else if (face[f1].rect[a1][b1].c == face[f2].rect[a2][b2].c) {
			index[iterator] = face[f1].rect[a1][b1].c;
			iterator++;
		} else if (face[f1].rect[a1][b1].c == face[f2].rect[a2][b2].d) {
			index[iterator] = face[f1].rect[a1][b1].c;
			iterator++;
		}

		if (face[f1].rect[a1][b1].d == face[f2].rect[a2][b2].a) {
			index[iterator] = face[f1].rect[a1][b1].d;
			iterator++;
		} else if (face[f1].rect[a1][b1].d == face[f2].rect[a2][b2].b) {
			index[iterator] = face[f1].rect[a1][b1].d;
			iterator++;
		} else if (face[f1].rect[a1][b1].d == face[f2].rect[a2][b2].c) {
			index[iterator] = face[f1].rect[a1][b1].d;
			iterator++;
		} else if (face[f1].rect[a1][b1].d == face[f2].rect[a2][b2].d) {
			index[iterator] = face[f1].rect[a1][b1].d;
			iterator++;
		}

		if (iterator == 2)
			return true;
		else
			return false;
	}

	// create faces
	public static void createFace() {

		face = new Face[6];
		for (int i = 0; i < 6; i++) {
			face[i] = new Face();
			face[i].setFace(i);
		}
	}

	// create cycles
	public static void createCycle() {
		cycle = new Cycle[3 * size];
		cycle[0] = new Cycle(0, 0, 0, true, true, 4, 0);
		cycle[1] = new Cycle(0, 0, 0, false, true, 3, 1);
		cycle[2] = new Cycle(0, size - 1, size - 1, true, true, 5, 2);
		cycle[3] = new Cycle(0, size - 1, size - 1, false, true, 2, 3);
		cycle[4] = new Cycle(3, 0, 0, true, true, 0, 4);
		cycle[5] = new Cycle(3, 0, size - 1, true, true, 1, 5);
		int iterator = 6;
		for (int i = 1; i < size - 1; i++) {
			cycle[iterator] = new Cycle(0, i, i, true, false, -1, iterator);
			iterator++;
			cycle[iterator] = new Cycle(0, i, i, false, false, -1, iterator);
			iterator++;
			cycle[iterator] = new Cycle(3, 0, i, true, false, -1, iterator);
			iterator++;
		}
	}

	// find the two stickers which are stuck to coordinates ci1 and ci2
	// result is stored in global arrays [fno[],a[],b[]]
	public static void findRect(int ci1, int ci2) {
		int i = 0;
		for (int f = 0; f < 6; f++) {
			for (int ca = 0; ca < Global.size; ca++) {
				for (int cb = 0; cb < Global.size; cb++) {
					if ((face[f].rect[ca][cb].a == ci1
							|| face[f].rect[ca][cb].b == ci1
							|| face[f].rect[ca][cb].c == ci1 || face[f].rect[ca][cb].d == ci1)
							&& (face[f].rect[ca][cb].a == ci2
									|| face[f].rect[ca][cb].b == ci2
									|| face[f].rect[ca][cb].c == ci2 || face[f].rect[ca][cb].d == ci2)) {
						fno[i] = f;
						a[i] = ca;
						b[i] = cb;

						if (i == 0)
							i++;
					}
				}
			}
		}
	}

	// find sticker adjacent to sticker[fn,i,j] and on face[adf]
	public static boolean findRect(int fn, int i, int j, int adf) {

		int ci1, ci2;
		for (int iterator = 0; iterator < 4; iterator++) {
			ci1 = face[fn].rect[i][j].a;
			ci2 = face[fn].rect[i][j].b;
			if (iterator == 1) {

				ci1 = face[fn].rect[i][j].b;
				ci2 = face[fn].rect[i][j].c;
			}
			if (iterator == 2) {

				ci1 = face[fn].rect[i][j].c;
				ci2 = face[fn].rect[i][j].d;
			}
			if (iterator == 3) {

				ci1 = face[fn].rect[i][j].d;
				ci2 = face[fn].rect[i][j].a;
			}

			for (int f = 0; f < 6; f++) {
				for (int ca = 0; ca < size; ca++) {
					for (int cb = 0; cb < size; cb++) {
						if ((face[f].rect[ca][cb].a == ci1
								|| face[f].rect[ca][cb].b == ci1
								|| face[f].rect[ca][cb].c == ci1 || face[f].rect[ca][cb].d == ci1)
								&& (face[f].rect[ca][cb].a == ci2
										|| face[f].rect[ca][cb].b == ci2
										|| face[f].rect[ca][cb].c == ci2 || face[f].rect[ca][cb].d == ci2)) {
							if ((f == adf)
									&& (!face[f].rect[ca][cb].markedInFaceCycle)) {
								affno = f;
								afa = ca;
								afb = cb;
								return true;
							}
						}
					}
				}
			}
		}
		affno = -1;
		afa = -1;
		afb = -1;
		return false;
	}

	// identify sticker which covers coordinates x and y, to get sticker from mouse location
	// since two stickers may contain the point on projection, depth-sorted array is used for priority based search
	
	// result is stored in idf,idi,idj
	public static boolean identifyRect(double x, double y) {
		Polygon t = new Polygon();
		int i, j, k;
		for (int a = 6 * Global.size * Global.size - 1; a > 0; a--) {

			i = JImagePanel.drawingRect[a][0];
			j = JImagePanel.drawingRect[a][1];
			k = JImagePanel.drawingRect[a][2];

			t.reset();

			t.addPoint((int) coordinateSpace[face[i].rect[j][k].a][0],
					(int) coordinateSpace[face[i].rect[j][k].a][1]);
			t.addPoint((int) coordinateSpace[face[i].rect[j][k].b][0],
					(int) coordinateSpace[face[i].rect[j][k].b][1]);
			t.addPoint((int) coordinateSpace[face[i].rect[j][k].c][0],
					(int) coordinateSpace[face[i].rect[j][k].c][1]);
			t.addPoint((int) coordinateSpace[face[i].rect[j][k].d][0],
					(int) coordinateSpace[face[i].rect[j][k].d][1]);
			if (t.contains(x, y)) {
				idf = i;
				idi = j;
				idj = k;
				return true;
			} else {
				t.reset();
			}
		}
		idf = -1;
		idi = -1;
		idj = -1;
		return false;
	}

	public static void animate() {

		int axis = -1;
		int avgX1 = (int) (Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[0][0]].rect[cycle[cycno].rectPointer[0][1]][cycle[cycno].rectPointer[0][2]].a][0] + Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[0][0]].rect[cycle[cycno].rectPointer[0][1]][cycle[cycno].rectPointer[0][2]].c][0]);
		int avgX2 = (int) (Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[size][0]].rect[cycle[cycno].rectPointer[size][1]][cycle[cycno].rectPointer[size][2]].a][0] + Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[size][0]].rect[cycle[cycno].rectPointer[size][1]][cycle[cycno].rectPointer[size][2]].c][0]);

		int avgY1 = (int) (Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[0][0]].rect[cycle[cycno].rectPointer[0][1]][cycle[cycno].rectPointer[0][2]].a][1] + Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[0][0]].rect[cycle[cycno].rectPointer[0][1]][cycle[cycno].rectPointer[0][2]].c][1]);
		int avgY2 = (int) (Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[size][0]].rect[cycle[cycno].rectPointer[size][1]][cycle[cycno].rectPointer[size][2]].a][1] + Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[size][0]].rect[cycle[cycno].rectPointer[size][1]][cycle[cycno].rectPointer[size][2]].c][1]);

		int avgZ1 = (int) (Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[0][0]].rect[cycle[cycno].rectPointer[0][1]][cycle[cycno].rectPointer[0][2]].a][2] + Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[0][0]].rect[cycle[cycno].rectPointer[0][1]][cycle[cycno].rectPointer[0][2]].c][2]);
		int avgZ2 = (int) (Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[size][0]].rect[cycle[cycno].rectPointer[size][1]][cycle[cycno].rectPointer[size][2]].a][2] + Global.baseCoordinateSpace[face[cycle[cycno].rectPointer[size][0]].rect[cycle[cycno].rectPointer[size][1]][cycle[cycno].rectPointer[size][2]].c][2]);

		if (avgX1 == avgX2) {
			axis = 0;
		}
		if (avgY1 == avgY2) {
			axis = 1;
		}
		if (avgZ1 == avgZ2) {
			axis = 2;
		}

		if (axis == 0) {
			while ((thetaX2Sum < 1.6) && (thetaX2Sum > -1.6)) {
				if (direction)
					thetaX2 = -0.4;
				else
					thetaX2 = 0.4;
				rotateX2();
				thetaX2Sum += thetaX2;
				for (int i = 0; i < nco; i++)
					for (int j = 0; j < 3; j++)
						animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];

				rotateY3();
				rotateX3();
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			thetaX2Sum = 0;
		}
		if (axis == 1) {
			while ((thetaY2Sum < 1.6) && (thetaY2Sum > -1.6)) {
				if (direction)
					thetaY2 = 0.4;
				else
					thetaY2 = -0.4;
				rotateY2();
				thetaY2Sum += thetaY2;
				for (int i = 0; i < nco; i++)
					for (int j = 0; j < 3; j++)
						animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];
				rotateY3();
				rotateX3();
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			thetaY2Sum = 0;
		}
		if (axis == 2) {
			while ((thetaZ2Sum < 1.6) && (thetaZ2Sum > -1.6)) {
				if (direction)
					thetaZ2 = 0.4;
				else
					thetaZ2 = -0.4;
				rotateZ2();
				thetaZ2Sum += thetaZ2;
				for (int i = 0; i < nco; i++)
					for (int j = 0; j < 3; j++)
						animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];
				rotateY3();
				rotateX3();
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			thetaZ2Sum = 0;
		}

		for (int i = 0; i < nco; i++)
			for (int j = 0; j < 3; j++) {
				rotatingCoordinateSpace[i][j] = baseCoordinateSpace[i][j];
				animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];
			}
	}

	// find the cycle to turn given initial sticker and final sticker
	public static boolean identifyCycle() {
		int cycleNo = -1, iteratori = 0, iteratorf;
		boolean cycleFound = false, initfound = false, forward = true;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 2; j++) {
				if (face[initrectf].rect[initrecti][initrectj].pointingCycle[i] == face[finalrectf].rect[finalrecti][finalrectj].pointingCycle[j]) {
					cycleNo = face[initrectf].rect[initrecti][initrectj].pointingCycle[i];
					cycleFound = true;
				}
			}

		if (cycleFound) {
			while (!initfound) {
				if ((cycle[cycleNo].rectPointer[iteratori][0] == initrectf)
						&& (cycle[cycleNo].rectPointer[iteratori][1] == initrecti)
						&& (cycle[cycleNo].rectPointer[iteratori][2] == initrectj)) {
					initfound = true;
					break;
				} else {
					iteratori++;
				}

			}
			for (iteratorf = iteratori; iteratorf != ((iteratori + 2 * Global.size) % (4 * Global.size)); iteratorf = ((iteratorf + 1) % (4 * Global.size))) {
				if ((cycle[cycleNo].rectPointer[iteratorf][0] == finalrectf)
						&& (cycle[cycleNo].rectPointer[iteratorf][1] == finalrecti)
						&& (cycle[cycleNo].rectPointer[iteratorf][2] == finalrectj)) {
					forward = false;
				}
			}
			cycno = cycleNo;
			direction = forward;

			for (int i = 0; i < 4 * Global.size; i++) {
				face[cycle[cycleNo].rectPointer[i][0]].rect[cycle[cycleNo].rectPointer[i][1]][cycle[cycleNo].rectPointer[i][2]].beingCycled = true;
			}

			if (cycle[cycleNo].terminal) {
				int fn = cycle[cycleNo].facePointer[0][0][0];
				for (int i = 0; i < size; i++)
					for (int j = 0; j < size; j++)
						face[fn].rect[i][j].beingCycled = true;
			}
			if (Global.useAnimation)
				animate();
			cycle[cycleNo].turn(forward);
			for (int i = 0; i < 4 * Global.size; i++) {
				face[cycle[cycleNo].rectPointer[i][0]].rect[cycle[cycleNo].rectPointer[i][1]][cycle[cycleNo].rectPointer[i][2]].beingCycled = false;
			}

			if (cycle[cycleNo].terminal) {
				int fn = cycle[cycleNo].facePointer[0][0][0];
				for (int i = 0; i < size; i++)
					for (int j = 0; j < size; j++)
						face[fn].rect[i][j].beingCycled = false;
			}
		}
		return cycleFound;
	}

	public static Image loadImage(String s) {
		Image i = null;
		try {
			i = ImageIO.read(new File(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	// check if cube is solved
	public static boolean isCubeSolved() {
		Color temp;
		for (int i = 0; i < 6; i++) {
			temp = face[i].rect[0][0].color;

			for (int j = 0; j < size; j++)
				for (int k = 0; k < size; k++)
					if (face[i].rect[j][k].color != temp)
						return false;
		}
		return true;
	}

	// check only for corners
	public static boolean areCornersSolved() {
		Color temp;
		for (int i = 0; i < 6; i++) {
			temp = face[i].rect[0][0].color;

			for (int j = 0; j < size; j += size - 1)
				for (int k = 0; k < size; k += size - 1)
					if (face[i].rect[j][k].color != temp)
						return false;
		}
		return true;
	}

	public static int tcycleIndex[] = new int[6];
	public static int selectedTcycleIndex[] = new int[3];

	public static void createTerminalIndices() {
		int counter = 0;
		int counterS = 0;
		for (int i = 0; i < (3 * size); i++) {
			if (cycle[i].terminal) {
				cycle[i].initializeCornerPointers();
				tcycleIndex[counter++] = i;
				if ((cycle[i].terminalFace == 1)
						|| (cycle[i].terminalFace == 2)
						|| (cycle[i].terminalFace == 5))
					selectedTcycleIndex[counterS++] = i;
			}
		}
	}

	static int d[] = new int[12], ds = 0;

	// Recursive Brute Force for solving corners!, Here lies the code for solver logics
	
	// Most code that follows is far to complex to be explained in comments
	public static boolean solveCorners(int depth) {
		if (depth == 0) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cycle[selectedTcycleIndex[i]].turnCorners();
				if (areCornersSolved()) {
					movesToMake.push(selectedTcycleIndex[i], j + 1);
					reverseMovesMade.push(selectedTcycleIndex[i], j + 1);
					return true;
				} else if (solveCorners(depth - 1)) {
					movesToMake.push(selectedTcycleIndex[i], j + 1);
					reverseMovesMade.push(selectedTcycleIndex[i], j + 1);
					return true;
				}
			}
			cycle[selectedTcycleIndex[i]].turnCorners();
		}
		return false;
	}

	public static void applyCornerSolution() {
		int temp, tempi, tempc;
		while (reverseMovesMade.top >= 0) {
			temp = reverseMovesMade.pop();
			tempc = temp % 4;
			tempi = temp / 4;
			movesMade.push(tempi, tempc);
		}
		while (movesMade.top >= 0) {
			temp = movesMade.pop();
			tempc = temp % 4;
			tempi = temp / 4;
			for (int w = 0; w < 4 - tempc; w++)
				cycle[tempi].turnCorners();
		}
		while (movesToMake.top >= 0) {
			temp = movesToMake.pop();
			tempc = temp % 4;
			tempi = temp / 4;
			movesMade.push(tempi, tempc);
			for (int w = 0; w < tempc; w++)
				cycle[tempi].turn(true);
		}
	}

	public static boolean solveCenters() {
		int count = 0;
		if (size % 2 == 0)
			return true;
		else {
			while (count < 8) {
				if (count < 4) {
					if (face[0].rect[size / 2][size / 2].color == face[0].rect[0][0].color)
						break;
					cycle[face[0].rect[size / 2][size / 2].pointingCycle[0]]
							.turn(true);
					movesMade.push(
							face[0].rect[size / 2][size / 2].pointingCycle[0],
							1);
					count++;
				} else {
					if (face[0].rect[size / 2][size / 2].color == face[0].rect[0][0].color)
						break;
					cycle[face[0].rect[size / 2][size / 2].pointingCycle[1]]
							.turn(true);
					movesMade.push(
							face[0].rect[size / 2][size / 2].pointingCycle[1],
							1);
					count++;
				}
			}
			count = 0;
			while (count < 4) {
				if (face[3].rect[size / 2][size / 2].color == face[3].rect[0][0].color)
					break;
				cycle[face[3].rect[size / 2][size / 2].pointingCycle[1]]
						.turn(true);
				movesMade.push(
						face[3].rect[size / 2][size / 2].pointingCycle[1], 1);
				count++;
			}
			return true;
		}
	}

	public static Color reqColorCEC[][] = new Color[12][2];
	public static Color actualColorCEC[][] = new Color[12][2];
	public static int CEC[][][] = new int[12][2][3];

	public static void setReqColorsCEC() {
		int counter = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 2; j < 6; j++) {
				reqColorCEC[counter][0] = face[i].rect[0][0].color;
				reqColorCEC[counter++][1] = face[j].rect[0][0].color;
			}
		}

		reqColorCEC[counter][0] = face[4].rect[0][0].color;
		reqColorCEC[counter++][1] = face[2].rect[0][0].color;

		reqColorCEC[counter][0] = face[2].rect[0][0].color;
		reqColorCEC[counter++][1] = face[5].rect[0][0].color;

		reqColorCEC[counter][0] = face[5].rect[0][0].color;
		reqColorCEC[counter++][1] = face[3].rect[0][0].color;

		reqColorCEC[counter][0] = face[3].rect[0][0].color;
		reqColorCEC[counter++][1] = face[4].rect[0][0].color;
	}

	public static void initializeCEC() {
		int count1, count2, rI, rJ, ia, ib, index = 0, foundIn = 0;
		for (int i = 0; i < 2; i++)
			for (int j = 2; j < 6; j++) {
				count1 = 0;
				while (count1 < 4) {
					if (count1 == 0) {
						rI = 0;
						rJ = size / 2;
					} else if (count1 == 1) {
						rI = size / 2;
						rJ = 0;
					} else if (count1 == 2) {
						rI = size - 1;
						rJ = size / 2;
					} else {
						rI = size / 2;
						rJ = size - 1;
					}
					count2 = 0;
					while (count2 < 4) {
						if (count2 == 0) {
							ia = face[i].rect[rI][rJ].a;
							ib = face[i].rect[rI][rJ].b;
						} else if (count2 == 1) {
							ia = face[i].rect[rI][rJ].b;
							ib = face[i].rect[rI][rJ].c;
						} else if (count2 == 2) {
							ia = face[i].rect[rI][rJ].c;
							ib = face[i].rect[rI][rJ].d;
						} else {
							ia = face[i].rect[rI][rJ].d;
							ib = face[i].rect[rI][rJ].a;
						}
						findRect(ia, ib);

						if ((fno[0] == j) || (fno[1] == j)) {
							CEC[index][0][0] = i;
							CEC[index][0][1] = rI;
							CEC[index][0][2] = rJ;

							if (fno[1] == j)
								foundIn = 1;
							else
								foundIn = 0;

							CEC[index][1][0] = j;
							CEC[index][1][1] = a[foundIn];
							CEC[index][1][2] = b[foundIn];

							index++;
						}

						count2++;
					}
					count1++;
				}
			}

		int i, j, counter = 0;
		while (counter < 4) {
			if (counter == 0) {
				i = 4;
				j = 2;
			} else if (counter == 1) {
				i = 2;
				j = 5;
			} else if (counter == 2) {
				i = 5;
				j = 3;
			} else {
				i = 3;
				j = 4;
			}
			count1 = 0;
			while (count1 < 4) {
				if (count1 == 0) {
					rI = 0;
					rJ = size / 2;
				} else if (count1 == 1) {
					rI = size / 2;
					rJ = 0;
				} else if (count1 == 2) {
					rI = size - 1;
					rJ = size / 2;
				} else {
					rI = size / 2;
					rJ = size - 1;
				}
				count2 = 0;
				while (count2 < 4) {
					if (count2 == 0) {
						ia = face[i].rect[rI][rJ].a;
						ib = face[i].rect[rI][rJ].b;
					} else if (count2 == 1) {
						ia = face[i].rect[rI][rJ].b;
						ib = face[i].rect[rI][rJ].c;
					} else if (count2 == 2) {
						ia = face[i].rect[rI][rJ].c;
						ib = face[i].rect[rI][rJ].d;
					} else {
						ia = face[i].rect[rI][rJ].d;
						ib = face[i].rect[rI][rJ].a;
					}
					findRect(ia, ib);

					if ((fno[0] == j) || (fno[1] == j)) {
						CEC[index][0][0] = i;
						CEC[index][0][1] = rI;
						CEC[index][0][2] = rJ;

						if (fno[1] == j)
							foundIn = 1;
						else
							foundIn = 0;

						CEC[index][1][0] = j;
						CEC[index][1][1] = a[foundIn];
						CEC[index][1][2] = b[foundIn];

						index++;
					}

					count2++;
				}
				count1++;
			}
			counter++;
		}
	}

	public static void setActualColorCEC() {
		int counter = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 2; j < 6; j++) {
				actualColorCEC[counter][0] = face[i].rect[CEC[counter][0][1]][CEC[counter][0][2]].color;
				actualColorCEC[counter][1] = face[j].rect[CEC[counter][1][1]][CEC[counter][1][2]].color;
				counter++;
			}
		}

		actualColorCEC[counter][0] = face[4].rect[CEC[counter][0][1]][CEC[counter][0][2]].color;
		actualColorCEC[counter][1] = face[2].rect[CEC[counter][1][1]][CEC[counter][1][2]].color;
		counter++;

		actualColorCEC[counter][0] = face[2].rect[CEC[counter][0][1]][CEC[counter][0][2]].color;
		actualColorCEC[counter][1] = face[5].rect[CEC[counter][1][1]][CEC[counter][1][2]].color;
		counter++;

		actualColorCEC[counter][0] = face[5].rect[CEC[counter][0][1]][CEC[counter][0][2]].color;
		actualColorCEC[counter][1] = face[3].rect[CEC[counter][1][1]][CEC[counter][1][2]].color;
		counter++;

		actualColorCEC[counter][0] = face[3].rect[CEC[counter][0][1]][CEC[counter][0][2]].color;
		actualColorCEC[counter][1] = face[4].rect[CEC[counter][1][1]][CEC[counter][1][2]].color;
		counter++;

	}

	public static int CECCycleIndex[] = new int[3];

	public static void initializeCECCycleIndices() {
		CECCycleIndex[0] = face[0].rect[size / 2][size / 2].pointingCycle[0];
		CECCycleIndex[1] = face[0].rect[size / 2][size / 2].pointingCycle[1];
		if ((face[2].rect[size / 2][size / 2].pointingCycle[0] != CECCycleIndex[0])
				&& (face[2].rect[size / 2][size / 2].pointingCycle[0] != CECCycleIndex[1])) {
			CECCycleIndex[2] = face[2].rect[size / 2][size / 2].pointingCycle[0];
		} else {
			CECCycleIndex[2] = face[2].rect[size / 2][size / 2].pointingCycle[1];
		}
	}

	public static int tcyclePermutation[][] = new int[6][4];
	public static int CECCyclePermutation[][] = new int[3][4];

	public static void initializeCyclePermutations() {
		int index;
		for (int i = 0; i < 6; i++) {
			index = 0;
			for (int j = 0; j < 4 * size; j++)
				for (int k = 0; k < 12; k++) {
					if (((CEC[k][0][0] == cycle[tcycleIndex[i]].rectPointer[j][0])
							&& (CEC[k][0][1] == cycle[tcycleIndex[i]].rectPointer[j][1]) && (CEC[k][0][2] == cycle[tcycleIndex[i]].rectPointer[j][2]))
							|| ((CEC[k][1][0] == cycle[tcycleIndex[i]].rectPointer[j][0])
									&& (CEC[k][1][1] == cycle[tcycleIndex[i]].rectPointer[j][1]) && (CEC[k][1][2] == cycle[tcycleIndex[i]].rectPointer[j][2]))) {
						tcyclePermutation[i][index++] = k;
					}
				}
		}

		for (int i = 0; i < 3; i++) {
			index = 0;
			for (int j = 0; j < 4 * size; j++)
				for (int k = 0; k < 12; k++) {
					if ((CEC[k][0][0] == cycle[CECCycleIndex[i]].rectPointer[j][0])
							&& (CEC[k][0][1] == cycle[CECCycleIndex[i]].rectPointer[j][1])
							&& (CEC[k][0][2] == cycle[CECCycleIndex[i]].rectPointer[j][2])) {
						CECCyclePermutation[i][index++] = k;
					}
				}
		}
	}

	public static boolean CECposition[] = new boolean[12];

	public static void checkCECPositions() {
		for (int i = 0; i < 12; i++) {
			if (((actualColorCEC[i][0] == reqColorCEC[i][0]) && (actualColorCEC[i][1] == reqColorCEC[i][1]))
					|| ((actualColorCEC[i][0] == reqColorCEC[i][1]) && (actualColorCEC[i][1] == reqColorCEC[i][0]))) {
				CECposition[i] = true;
			} else {
				CECposition[i] = false;
			}
		}
	}

	public static int findCorrectCECPosition(int j) {
		for (int i = 0; i < 12; i++) {
			if (((actualColorCEC[j][0] == reqColorCEC[i][0]) && (actualColorCEC[j][1] == reqColorCEC[i][1]))
					|| ((actualColorCEC[j][0] == reqColorCEC[i][1]) && (actualColorCEC[j][1] == reqColorCEC[i][0]))) {
				return i;
			}
		}
		return -1;
	}

	public static int findTcycle(int a, int b) {
		int match;
		for (int i = 0; i < 6; i++) {
			match = 0;
			for (int j = 0; j < 4; j++) {
				if ((tcyclePermutation[i][j] == a)
						|| ((tcyclePermutation[i][j] == b)))
					match++;
				if (match == 2)
					return i;
			}
		}
		return -1;
	}

	public static int findCECCycle(int a, int b) {
		int match;
		for (int i = 0; i < 3; i++) {
			match = 0;
			for (int j = 0; j < 4; j++) {
				if ((CECCyclePermutation[i][j] == a)
						|| ((CECCyclePermutation[i][j] == b)))
					match++;
				if (match == 2)
					return i;
			}
		}
		return -1;
	}

	public static int findCECCycle(int a) {
		int match;
		for (int i = 0; i < 3; i++) {
			match = 0;
			for (int j = 0; j < 4; j++) {
				if ((CECCyclePermutation[i][j] == a))
					match++;
				if (match == 1)
					return i;
			}
		}
		return -1;
	}

	public static int findDistance(int a, int b, int n, boolean terminal) {
		int d = 0;
		if (terminal)
			for (int i = 0; i < 4; i++) {
				if (tcyclePermutation[n][i] == a) {
					d++;
					for (int j = i + 1;; j++) {
						j = j % 4;
						if (d > 3) {
							return -1;
						}
						if (tcyclePermutation[n][j] == b)
							return d;
						else
							d++;
					}
				}
			}
		else {
			for (int i = 0; i < 4; i++) {
				if (CECCyclePermutation[n][i] == a) {
					d++;
					for (int j = i + 1;; j++) {
						j = j % 4;
						if (d > 3) {
							return -1;
						}
						if (CECCyclePermutation[n][j] == b)
							return d;
						else
							d++;
					}
				}
			}
		}
		return -1;
	}

	public static int findIndexInPermutation(int a, int n) {
		for (int i = 0; i < 4; i++)
			if (CECCyclePermutation[n][i] == a)
				return i;
		return -1;
	}

	public static boolean solveCEC() {
		int cP, y, c1, c2, c3, c4, c3d = -1, c5, c5d = -1, oldcP = -1;
		outer: for (int i = 0; i < 12; i++) {
			c5 = -1;
			setActualColorCEC();
			checkCECPositions();
			if (!CECposition[i]) {
				cP = findCorrectCECPosition(i);
				y = findTcycle(i, cP);
				if (y == -1) {
					c5loop: for (int tc1 = 0; tc1 < 6; tc1++)
						for (int tc2 = 0; tc2 < 6; tc2++) {
							if (tc1 != tc2) {
								boolean ifound = false, cPfound = false;
								for (int iteration = 0; iteration < 4; iteration++) {
									if (tcyclePermutation[tc1][iteration] == i)
										ifound = true;
									if (tcyclePermutation[tc2][iteration] == cP)
										cPfound = true;
								}
								if (ifound && cPfound) {
									for (int iteration1 = 0; iteration1 < 4; iteration1++)
										for (int iteration2 = 0; iteration2 < 4; iteration2++) {
											if (tcyclePermutation[tc1][iteration1] == tcyclePermutation[tc2][iteration2]) {
												oldcP = cP;
												cP = tcyclePermutation[tc2][iteration2];
												y = findTcycle(i, cP);
												c5 = tcycleIndex[tc2];
												c5d = findDistance(cP, oldcP,
														tc2, true);

												for (int itemp = 0; itemp < c5d; itemp++) {
													boolean dummy = CECposition[tcyclePermutation[tc2][0]];
													for (int itemp2 = 0; itemp2 < 3; itemp2++) {
														CECposition[tcyclePermutation[tc2][itemp2]] = CECposition[tcyclePermutation[tc2][itemp2 + 1]];
													}
													CECposition[tcyclePermutation[tc2][3]] = dummy;
													cycle[c5].turn(true);
													movesMade.push(c5, 1);
												}
												break c5loop;
											}
										}
								}
							}
						}
				}
				int dy = findDistance(i, cP, y, true);
				inner: for (int j = 0; j < 12; j++) {
					if ((i != j) && (cP != j) && (!CECposition[j])) {
						int t1, t2, t3, t4, cmatch = 0;
						c1 = -1;
						c2 = -1;
						c3 = -1;
						c4 = -1;
						t1 = tcyclePermutation[y][0];
						t2 = tcyclePermutation[y][1];
						t3 = tcyclePermutation[y][2];
						t4 = tcyclePermutation[y][3];
						if (findTcycle(j, t1) != -1)
							cmatch++;
						if (findTcycle(j, t2) != -1)
							cmatch++;
						if (findTcycle(j, t3) != -1)
							cmatch++;
						if (findTcycle(j, t4) != -1)
							cmatch++;
						if (cmatch == 2) {
							boolean mj, micP;
							int iteratorC = -1;
							for (int iterator6 = 0; iterator6 < 6; iterator6++) {
								mj = false;
								micP = false;
								for (int iterator7 = 0; iterator7 < 4; iterator7++) {
									if (tcyclePermutation[iterator6][iterator7] == j)
										mj = true;
									if (tcyclePermutation[iterator6][iterator7] == i)
										micP = true;
									if (tcyclePermutation[iterator6][iterator7] == cP)
										micP = true;
								}
								if (mj && !micP) {
									iteratorC = iterator6;
									c3 = tcycleIndex[iterator6];
									break;
								}

							}
							if (c3 == -1) {
								int tempc = findCECCycle(j);
								c4 = CECCycleIndex[tempc];
								cycle[c4].turn(true);
								movesMade.push(c4, 1);
								for (int iterator8 = 0; iterator8 < 4; iterator8++) {
									if (CECCyclePermutation[tempc][iterator8] == j) {
										if (iterator8 > 0) {
											j = CECCyclePermutation[tempc][iterator8 - 1];
											break;
										} else {
											j = CECCyclePermutation[tempc][3];
											break;
										}
									}
								}

								for (int iterator6 = 0; iterator6 < 6; iterator6++) {
									mj = false;
									micP = false;
									for (int iterator7 = 0; iterator7 < 4; iterator7++) {
										if (tcyclePermutation[iterator6][iterator7] == j)
											mj = true;
										if (tcyclePermutation[iterator6][iterator7] == i)
											micP = true;
										if (tcyclePermutation[iterator6][iterator7] == cP)
											micP = true;
									}
									if (mj && !micP) {
										iteratorC = iterator6;
										c3 = tcycleIndex[iterator6];
										break;
									}
								}
							}

							fcloop2: for (int iterator1 = 0; iterator1 < 6; iterator1++) {
								if ((tcyclePermutation[iterator1][0] == t1)
										|| (tcyclePermutation[iterator1][1] == t1)
										|| (tcyclePermutation[iterator1][2] == t1)
										|| (tcyclePermutation[iterator1][3] == t1)
										|| (tcyclePermutation[iterator1][0] == t2)
										|| (tcyclePermutation[iterator1][1] == t2)
										|| (tcyclePermutation[iterator1][2] == t2)
										|| (tcyclePermutation[iterator1][3] == t2)
										|| (tcyclePermutation[iterator1][0] == t3)
										|| (tcyclePermutation[iterator1][1] == t3)
										|| (tcyclePermutation[iterator1][2] == t3)
										|| (tcyclePermutation[iterator1][3] == t3)
										|| (tcyclePermutation[iterator1][0] == t4)
										|| (tcyclePermutation[iterator1][1] == t4)
										|| (tcyclePermutation[iterator1][2] == t4)
										|| (tcyclePermutation[iterator1][3] == t4)) {
									continue fcloop2;
								}
								int r1 = tcyclePermutation[iterator1][0], r2 = tcyclePermutation[iterator1][1], r3 = tcyclePermutation[iterator1][2], r4 = tcyclePermutation[iterator1][3];
								for (int iterator2 = 0; iterator2 < 4; iterator2++) {
									if ((tcyclePermutation[iteratorC][iterator2] == r1)
											|| (tcyclePermutation[iteratorC][iterator2] == r2)
											|| (tcyclePermutation[iteratorC][iterator2] == r3)
											|| (tcyclePermutation[iteratorC][iterator2] == r4)) {
										int oldj = j;
										j = tcyclePermutation[iteratorC][iterator2];
										c3d = findDistance(j, oldj, iteratorC,
												true);
										for (int iter = 0; iter < c3d; iter++)
											cycle[c3].turn(true);
										movesMade.push(c3, c3d);

									}
								}
							}
						}

						if (cmatch > 2) {
							boolean matchj, matchicP;
							c2loop: for (int iterator3 = 0; iterator3 < 6; iterator3++) {
								matchj = false;
								matchicP = false;
								for (int iterator4 = 0; iterator4 < 4; iterator4++) {
									if (tcyclePermutation[iterator3][iterator4] == j)
										matchj = true;
									if (tcyclePermutation[iterator3][iterator4] == i)
										matchicP = true;
									if (tcyclePermutation[iterator3][iterator4] == cP)
										matchicP = true;
								}
								if (matchj && !matchicP) {
									c2 = tcycleIndex[iterator3];
									cycle[c2].turn(true);
									cycle[c2].turn(true);
									movesMade.push(c2, 2);
									for (int iterator5 = 0; iterator5 < 4; iterator5++) {
										if (tcyclePermutation[iterator3][iterator5] == j) {
											if (iterator5 > 1) {
												j = tcyclePermutation[iterator3][iterator5 - 2];
												break c2loop;
											} else {
												j = tcyclePermutation[iterator3][iterator5 + 2];
												break c2loop;
											}
										}
									}
								}
							}
						}

						if (findCECCycle(i, j) != -1) {

							if ((findTcycle(i, j) == -1)
									|| (findTcycle(cP, j) == -1)) {
								fcloop: for (int iterator1 = 0; iterator1 < 6; iterator1++) {
									if ((tcyclePermutation[iterator1][0] == t1)
											|| (tcyclePermutation[iterator1][1] == t1)
											|| (tcyclePermutation[iterator1][2] == t1)
											|| (tcyclePermutation[iterator1][3] == t1)
											|| (tcyclePermutation[iterator1][0] == t2)
											|| (tcyclePermutation[iterator1][1] == t2)
											|| (tcyclePermutation[iterator1][2] == t2)
											|| (tcyclePermutation[iterator1][3] == t2)
											|| (tcyclePermutation[iterator1][0] == t3)
											|| (tcyclePermutation[iterator1][1] == t3)
											|| (tcyclePermutation[iterator1][2] == t3)
											|| (tcyclePermutation[iterator1][3] == t3)
											|| (tcyclePermutation[iterator1][0] == t4)
											|| (tcyclePermutation[iterator1][1] == t4)
											|| (tcyclePermutation[iterator1][2] == t4)
											|| (tcyclePermutation[iterator1][3] == t4)) {
										continue fcloop;
									}
									c1 = tcycleIndex[iterator1];
									cycle[c1].turn(true);
									movesMade.push(c1, 1);
									for (int iterator2 = 0; iterator2 < 4; iterator2++) {
										if (tcyclePermutation[iterator1][iterator2] == j) {
											if (iterator2 > 0) {
												j = tcyclePermutation[iterator1][iterator2 - 1];
												break fcloop;
											} else {
												j = tcyclePermutation[iterator1][3];
												break fcloop;
											}
										}
									}
								}
							}
						}
						// face[CEC[i][0][0]].rect[CEC[i][0][1]][CEC[i][0][2]].color
						// = Color.BLACK;
						// face[CEC[j][0][0]].rect[CEC[j][0][1]][CEC[j][0][2]].color
						// = Color.CYAN;
						// face[CEC[cP][0][0]].rect[CEC[cP][0][1]][CEC[cP][0][2]].color
						// = Color.GRAY;

						// if (true)
						// break outer;
						innermost: for (int im = 0; im < 12; im++) {
							int iimcec = findCECCycle(i, im);
							if (iimcec == -1)
								continue innermost;
							int jimt = findTcycle(j, im);
							if (jimt == -1)
								continue innermost;
							int iimd = findDistance(im, i, iimcec, false);
							if (iimd == 2)
								continue innermost;
							int jimd = findDistance(im, j, jimt, true);

							for (int temp = 0; temp < iimd; temp++)
								cycle[CECCycleIndex[iimcec]].turn(true);
							movesMade.push(CECCycleIndex[iimcec], iimd);

							for (int temp = 0; temp < jimd; temp++)
								cycle[tcycleIndex[jimt]].turn(true);
							movesMade.push(tcycleIndex[jimt], jimd);

							for (int temp = 0; temp < 4 - iimd; temp++)
								cycle[CECCycleIndex[iimcec]].turn(true);
							movesMade.push(CECCycleIndex[iimcec], 4 - iimd);

							for (int temp = 0; temp < dy; temp++)
								cycle[tcycleIndex[y]].turn(true);
							movesMade.push(tcycleIndex[y], dy);

							for (int temp = 0; temp < iimd; temp++)
								cycle[CECCycleIndex[iimcec]].turn(true);
							movesMade.push(CECCycleIndex[iimcec], iimd);

							for (int temp = 0; temp < 4 - jimd; temp++)
								cycle[tcycleIndex[jimt]].turn(true);
							movesMade.push(tcycleIndex[jimt], 4 - jimd);

							for (int temp = 0; temp < 4 - iimd; temp++)
								cycle[CECCycleIndex[iimcec]].turn(true);
							movesMade.push(CECCycleIndex[iimcec], 4 - iimd);

							for (int temp = 0; temp < 4 - dy; temp++)
								cycle[tcycleIndex[y]].turn(true);
							movesMade.push(tcycleIndex[y], 4 - dy);

							break;
						}
						if (c1 != -1) {
							cycle[c1].turn(true);
							cycle[c1].turn(true);
							cycle[c1].turn(true);
							movesMade.push(c1, 3);
						}
						if (c2 != -1) {
							cycle[c2].turn(true);
							cycle[c2].turn(true);
							movesMade.push(c2, 2);
						}
						if (c3 != -1) {
							for (int iter = 0; iter < 4 - c3d; iter++)
								cycle[c3].turn(true);
							movesMade.push(c3, 4 - c3d);
						}
						if (c4 != -1) {
							cycle[c4].turn(true);
							cycle[c4].turn(true);
							cycle[c4].turn(true);
							movesMade.push(c4, 3);
						}

						break inner;
					}
				}
				if (c5 != -1) {
					for (int iterate = 0; iterate < 4 - c5d; iterate++)
						cycle[c5].turn(true);
					movesMade.push(c5, 4 - c5d);
				}
				break outer;
			}
		}
		setActualColorCEC();
		checkCECPositions();
		for (int i = 0; i < 12; i++)
			if (!CECposition[i])
				solveCEC();
		return true;
	}

	public static boolean CECOrientation[] = new boolean[12];

	public static void checkCECOrientations() {
		for (int i = 0; i < 12; i++) {
			if ((actualColorCEC[i][0] == reqColorCEC[i][0])
					&& (actualColorCEC[i][1] == reqColorCEC[i][1])) {
				CECOrientation[i] = true;
			} else {
				CECOrientation[i] = false;
			}
		}
	}

	public static boolean OrientCEC() {

		outerloop: for (int i = 0; i < 12; i++) {
			setActualColorCEC();
			checkCECOrientations();

			if (!CECOrientation[i])
				for (int j = 0; j < 12; j++) {
					if ((j != i) && (!CECOrientation[j])) {
						int tc, tcd, cecc, im = -1, ceccd = -1, otc = -1, conjugate = -1, conjugateD = -1;
						tc = findTcycle(i, j);
						if (tc == -1) {
							conjugateloop: for (int m = 0; m < 6; m++)
								for (int n = 0; n < 6; n++) {
									boolean ifound = false;
									boolean jfound = false;
									for (int l = 0; l < 4; l++) {
										if (tcyclePermutation[m][l] == i)
											ifound = true;
										if (tcyclePermutation[n][l] == j)
											jfound = true;
									}
									if (!ifound || !jfound)
										continue;
									for (int k = 0; k < 4; k++)
										for (int l = 0; l < 4; l++) {
											if (tcyclePermutation[m][k] == tcyclePermutation[n][l]) {
												int oldj = j;
												j = tcyclePermutation[n][l];
												conjugate = tcycleIndex[n];
												conjugateD = findDistance(j,
														oldj, n, true);

												for (int x = 0; x < conjugateD; x++)
													cycle[conjugate].turn(true);
												movesMade.push(conjugate,
														conjugateD);
												tc = findTcycle(i, j);
												break conjugateloop;

											}
										}
								}
						}
						tcd = findDistance(i, j, tc, true);
						cecc = findCECCycle(i);
						int t1, t2, t3, t4;
						t1 = tcyclePermutation[tc][0];
						t2 = tcyclePermutation[tc][1];
						t3 = tcyclePermutation[tc][2];
						t4 = tcyclePermutation[tc][3];

						otcloop: for (int a = 0; a < 6; a++) {
							for (int b = 0; b < 4; b++) {
								if ((tcyclePermutation[a][b] == t1)
										|| (tcyclePermutation[a][b] == t2)
										|| (tcyclePermutation[a][b] == t3)
										|| (tcyclePermutation[a][b] == t4)) {
									continue otcloop;
								}
							}
							otc = a;
						}

						imloop: for (int a = 0; a < 4; a++) {
							im = CECCyclePermutation[cecc][a];
							if (findTcycle(im, j) != -1)
								continue imloop;
							ceccd = findDistance(im, i, cecc, false);
							if (ceccd == 2)
								continue imloop;
							else
								break imloop;
						}

						for (int x = 0; x < ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], ceccd);

						for (int x = 0; x < 2; x++)
							cycle[tcycleIndex[otc]].turn(true);
						movesMade.push(tcycleIndex[otc], 2);

						for (int x = 0; x < 4 - ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], 4 - ceccd);

						for (int x = 0; x < 1; x++)
							cycle[tcycleIndex[otc]].turn(true);
						movesMade.push(tcycleIndex[otc], 1);

						for (int x = 0; x < ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], ceccd);

						for (int x = 0; x < 3; x++)
							cycle[tcycleIndex[otc]].turn(true);
						movesMade.push(tcycleIndex[otc], 3);

						for (int x = 0; x < 4 - ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], 4 - ceccd);

						for (int x = 0; x < tcd; x++)
							cycle[tcycleIndex[tc]].turn(true);
						movesMade.push(tcycleIndex[tc], tcd);

						for (int x = 0; x < ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], ceccd);

						for (int x = 0; x < 1; x++)
							cycle[tcycleIndex[otc]].turn(true);
						movesMade.push(tcycleIndex[otc], 1);

						for (int x = 0; x < 4 - ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], 4 - ceccd);

						for (int x = 0; x < 3; x++)
							cycle[tcycleIndex[otc]].turn(true);
						movesMade.push(tcycleIndex[otc], 3);

						for (int x = 0; x < ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], ceccd);

						for (int x = 0; x < 2; x++)
							cycle[tcycleIndex[otc]].turn(true);
						movesMade.push(tcycleIndex[otc], 2);

						for (int x = 0; x < 4 - ceccd; x++)
							cycle[CECCycleIndex[cecc]].turn(true);
						movesMade.push(CECCycleIndex[cecc], 4 - ceccd);

						for (int x = 0; x < 4 - tcd; x++)
							cycle[tcycleIndex[tc]].turn(true);
						movesMade.push(tcycleIndex[tc], 4 - tcd);

						if (conjugate != -1) {
							for (int x = 0; x < 4 - conjugateD; x++)
								cycle[conjugate].turn(true);
							movesMade.push(conjugate, 4 - conjugateD);
						}
						continue outerloop;

					}
				}
		}
		return true;
	}

	public static int NCEC[][][] = new int[24][2][3];
	public static Color reqColorNCEC[][] = new Color[24][2];
	public static Color actualColorNCEC[][] = new Color[24][2];
	public static int NCECCycleIndex[] = new int[6];
	public static int NCECCyclePermutation[][] = new int[6][4];
	public static int NCECTcyclePermutation[][][] = new int[6][2][4];
	public static boolean NCECPosition[] = new boolean[24];

	public static void initializeNCEC(int n) {
		int iterator = 0;
		for (int i = 0; i < 2; i++)
			for (int m = 2; m < 6; m++) {
				int t = 0, j = -1, k = -1;
				while (t < 8) {
					if (t == 0) {
						j = 0;
						k = n;
					}
					if (t == 1) {
						j = 0;
						k = size - n - 1;
					}
					if (t == 2) {
						j = size - 1;
						k = n;
					}
					if (t == 3) {
						j = size - 1;
						k = size - n - 1;
					}
					if (t == 4) {
						j = n;
						k = 0;
					}
					if (t == 5) {
						j = size - n - 1;
						k = 0;
					}
					if (t == 6) {
						j = n;
						k = size - 1;
					}
					if (t == 7) {
						j = size - n - 1;
						k = size - 1;
					}

					int t2 = 0, ia, ib;
					while (t2 < 4) {
						if (t2 == 0) {
							ia = face[i].rect[j][k].a;
							ib = face[i].rect[j][k].b;
						} else if (t2 == 1) {
							ia = face[i].rect[j][k].b;
							ib = face[i].rect[j][k].c;
						} else if (t2 == 2) {
							ia = face[i].rect[j][k].c;
							ib = face[i].rect[j][k].d;
						} else {
							ia = face[i].rect[j][k].d;
							ib = face[i].rect[j][k].a;
						}

						findRect(ia, ib);

						if ((fno[0] == m) || fno[1] == m) {

							NCEC[iterator][0][0] = i;
							NCEC[iterator][0][1] = j;
							NCEC[iterator][0][2] = k;

							int foundIn;

							if (fno[1] == m)
								foundIn = 1;
							else
								foundIn = 0;

							NCEC[iterator][1][0] = fno[foundIn];
							NCEC[iterator][1][1] = a[foundIn];
							NCEC[iterator++][1][2] = b[foundIn];
						}
						t2++;
					}
					t++;
				}
			}
		int t3 = 0;
		while (t3 < 4) {
			int i, m;
			if (t3 == 0) {
				i = 2;
				m = 4;
			} else if (t3 == 1) {
				i = 4;
				m = 3;
			} else if (t3 == 2) {
				i = 3;
				m = 5;
			} else {
				i = 5;
				m = 2;
			}

			int t = 0, j = -1, k = -1;
			while (t < 8) {
				if (t == 0) {
					j = 0;
					k = n;
				}
				if (t == 1) {
					j = 0;
					k = size - n - 1;
				}
				if (t == 2) {
					j = size - 1;
					k = n;
				}
				if (t == 3) {
					j = size - 1;
					k = size - n - 1;
				}
				if (t == 4) {
					j = n;
					k = 0;
				}
				if (t == 5) {
					j = size - n - 1;
					k = 0;
				}
				if (t == 6) {
					j = n;
					k = size - 1;
				}
				if (t == 7) {
					j = size - n - 1;
					k = size - 1;
				}

				int t2 = 0, ia, ib;
				while (t2 < 4) {
					if (t2 == 0) {
						ia = face[i].rect[j][k].a;
						ib = face[i].rect[j][k].b;
					} else if (t2 == 1) {
						ia = face[i].rect[j][k].b;
						ib = face[i].rect[j][k].c;
					} else if (t2 == 2) {
						ia = face[i].rect[j][k].c;
						ib = face[i].rect[j][k].d;
					} else {
						ia = face[i].rect[j][k].d;
						ib = face[i].rect[j][k].a;
					}

					findRect(ia, ib);

					if ((fno[0] == m) || fno[1] == m) {

						NCEC[iterator][0][0] = i;
						NCEC[iterator][0][1] = j;
						NCEC[iterator][0][2] = k;

						int foundIn;

						if (fno[1] == m)
							foundIn = 1;
						else
							foundIn = 0;

						NCEC[iterator][1][0] = fno[foundIn];
						NCEC[iterator][1][1] = a[foundIn];
						NCEC[iterator++][1][2] = b[foundIn];
					}
					t2++;
				}
				t++;
			}
			t3++;
		}

		// for (int i = 0; i < 24; i++)
		// for (int j = 0; j < 2; j++)
		// face[NCEC[i][j][0]].rect[NCEC[i][j][1]][NCEC[i][j][2]].color =
		// Color.BLACK;
	}

	public static void initializeReqColorsNCEC() {
		for (int i = 0; i < 24; i++)
			for (int j = 0; j < 2; j++)
				reqColorNCEC[i][j] = face[NCEC[i][j][0]].rect[0][0].color;
	}

	public static void initializeActualColorsNCEC() {
		for (int i = 0; i < 24; i++)
			for (int j = 0; j < 2; j++)
				actualColorNCEC[i][j] = face[NCEC[i][j][0]].rect[NCEC[i][j][1]][NCEC[i][j][2]].color;
	}

	public static void initializeNCECCycleIndices() {
		int iterator = 0;
		outerloop: for (int i = 0; i < 3 * size; i++) {
			for (int j = 0; j < 4 * size; j++) {
				for (int m = 0; m < 24; m++) {
					if ((cycle[i].rectPointer[j][0] == NCEC[m][0][0])
							&& (cycle[i].rectPointer[j][1] == NCEC[m][0][1])
							&& (cycle[i].rectPointer[j][2] == NCEC[m][0][2])
							&& (!cycle[i].terminal)) {
						NCECCycleIndex[iterator++] = i;
						continue outerloop;
					}
				}
			}
		}
	}

	public static void initializeNCECCyclePermutations() {
		int i, iterator;
		for (int itemp = 0; itemp < 6; itemp++) {
			iterator = 0;
			i = NCECCycleIndex[itemp];
			for (int j = 0; j < 4 * size; j++) {
				for (int m = 0; m < 24; m++) {
					if (((cycle[i].rectPointer[j][0] == NCEC[m][0][0])
							&& (cycle[i].rectPointer[j][1] == NCEC[m][0][1]) && (cycle[i].rectPointer[j][2] == NCEC[m][0][2]))) {
						NCECCyclePermutation[itemp][iterator++] = m;
					}

				}
			}
		}
	}

	public static void initializeNCECTcyclePermutations() {
		int i, iterator, x = 0;
		for (int itemp = 0; itemp < 6; itemp++) {
			iterator = 0;
			i = tcycleIndex[itemp];
			for (int j = 0; j < 4 * size; j++) {
				for (int m = 0; m < 24; m++) {
					if (((cycle[i].rectPointer[j][0] == NCEC[m][0][0])
							&& (cycle[i].rectPointer[j][1] == NCEC[m][0][1]) && (cycle[i].rectPointer[j][2] == NCEC[m][0][2]))
							|| ((cycle[i].rectPointer[j][0] == NCEC[m][1][0])
									&& (cycle[i].rectPointer[j][1] == NCEC[m][1][1]) && (cycle[i].rectPointer[j][2] == NCEC[m][1][2]))) {

						NCECTcyclePermutation[itemp][x][iterator] = m;
						if (x == 0)
							x = 1;
						else {
							iterator++;
							x = 0;
						}

					}

				}
			}
		}
	}

	public static int findNCECCycle(int a, int b) {
		boolean foundA = false, foundB = false;
		for (int i = 0; i < 6; i++) {
			foundA = false;
			foundB = false;
			for (int j = 0; j < 4; j++) {
				if (NCECCyclePermutation[i][j] == a)
					foundA = true;
				if (NCECCyclePermutation[i][j] == b)
					foundB = true;
			}
			if (foundA && foundB)
				return i;
		}
		return -1;
	}

	public static int findNCECCycle(int a) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				if (NCECCyclePermutation[i][j] == a)
					return i;
			}
		}
		return -1;
	}

	public static int NCECTcycleIndex, NCECTcycleIdentifier;

	public static boolean findNCECTcycle(int a, int b) {
		boolean foundA, foundB;
		for (int i = 0; i < 6; i++)
			for (int x = 0; x < 2; x++) {
				foundA = false;
				foundB = false;
				for (int j = 0; j < 4; j++) {
					if (NCECTcyclePermutation[i][x][j] == a)
						foundA = true;
					if (NCECTcyclePermutation[i][x][j] == b)
						foundB = true;
				}
				if (foundA && foundB) {
					NCECTcycleIndex = i;
					NCECTcycleIdentifier = x;
					return true;
				}
			}

		for (int i = 0; i < 6; i++) {
			foundA = false;
			foundB = false;
			for (int x = 0; x < 2; x++) {
				for (int j = 0; j < 4; j++) {
					if (NCECTcyclePermutation[i][x][j] == a)
						foundA = true;
					if (NCECTcyclePermutation[i][x][j] == b)
						foundB = true;
				}
			}
			if (foundA && foundB) {
				NCECTcycleIndex = i;
				NCECTcycleIdentifier = -1;
				return true;
			}
		}
		return false;
	}

	public static void checkNCECPositions() {
		for (int i = 0; i < 24; i++) {
			if ((reqColorNCEC[i][0] == actualColorNCEC[i][0])
					&& (reqColorNCEC[i][1] == actualColorNCEC[i][1])) {
				NCECPosition[i] = true;
			} else
				NCECPosition[i] = false;
		}
	}

	public static int findCorrectNCEC(int a) {
		for (int i = 0; i < 24; i++) {
			if (i != a)
				if (((reqColorNCEC[i][0] == actualColorNCEC[a][0]) && (reqColorNCEC[i][1] == actualColorNCEC[a][1]))
						|| ((reqColorNCEC[i][1] == actualColorNCEC[a][0]) && (reqColorNCEC[i][0] == actualColorNCEC[a][1]))) {
					if (!NCECPosition[i])
						return i;
				}
		}
		return -1;
	}

	public static int findDistanceNCEC(int a, int b, int cin, int cid,
			boolean terminal) {
		int d = 0;
		if (!terminal) {
			for (int i = 0; i < 4; i++) {
				if (NCECCyclePermutation[cin][i] == a) {
					d++;
					for (int j = i + 1;; j++) {
						j = j % 4;
						if (NCECCyclePermutation[cin][j] == b)
							return d;
						else
							d++;
						if (d > 5)
							System.out.println("b not found");
					}
				}
			}
		} else {
			for (int i = 0; i < 4; i++) {
				if (NCECTcyclePermutation[cin][cid][i] == a) {
					d++;
					for (int j = i + 1;; j++) {
						j = j % 4;
						if (NCECTcyclePermutation[cin][cid][j] == b)
							return d;
						else
							d++;

						if (d > 5)
							System.out.println("b not found");
					}
				}
			}
		}

		return -1;
	}

	public static int findDisjointTcycleNCEC(int n) {
		iloop: for (int i = 0; i < 6; i++) {
			for (int x = 0; x < 4; x++) {
				for (int y = 0; y < 4; y++) {
					if ((NCECTcyclePermutation[i][0][x] == NCECTcyclePermutation[n][0][y])
							|| (NCECTcyclePermutation[i][1][x] == NCECTcyclePermutation[n][0][y])
							|| (NCECTcyclePermutation[i][1][x] == NCECTcyclePermutation[n][1][y])
							|| (NCECTcyclePermutation[i][0][x] == NCECTcyclePermutation[n][1][y]))
						continue iloop;
				}
			}

			return i;
		}
		return -1;
	}

	public static boolean clusterElementInCycle(int j, int dcycle) {
		for (int i = 0; i < 2; i++)
			for (int x = 0; x < 4; x++) {
				if (NCECTcyclePermutation[dcycle][i][x] == j)
					return true;
			}
		return false;
	}

	public static boolean solveNCEC() {
		int cP, yin, yid, dy, c1, c3, c3d, c2, c4, c5, c6, c7, c7d, c8, c9, c9d, c10, c10d;
		Random rn = new Random();
		int r = -1;
		outerloopNCEC: for (int i = 0; i < 24; i++) {
			r = rn.nextInt(4);
			if (r == 2)
				continue;
			c1 = -1;
			c2 = -1;
			c3 = -1;
			c3d = 0;
			c4 = -1;
			c5 = -1;
			c6 = -1;
			c7 = -1;
			c7d = 0;
			c8 = -1;
			c9 = -1;
			c9d = 0;
			c10 = -1;
			c10d = 0;
			initializeActualColorsNCEC();
			checkNCECPositions();
			int count = 0, wrongC = -1;
			for (int p = 0; p < 24; p++)
				if (!NCECPosition[p]) {
					if (wrongC == -1)
						wrongC = p;
					count++;
				}
			if (count == 2) {
				int parityCorrector = findNCECCycle(wrongC);
				cycle[NCECCycleIndex[parityCorrector]].turn(true);
				movesMade.push(NCECCycleIndex[parityCorrector], 1);
			}
			if (!NCECPosition[i]) {
				cP = findCorrectNCEC(i);
				if (!findNCECTcycle(i, cP)) {
					c10loop: for (int x = 0; x < 24; x++) {
						if ((x != i) && (x != cP)) {
							if (findNCECTcycle(x, i)) {
								if (findNCECTcycle(x, cP)) {
									if (NCECTcycleIdentifier != -1) {
										c10d = findDistanceNCEC(x, cP,
												NCECTcycleIndex,
												NCECTcycleIdentifier, true);
										c10 = NCECTcycleIndex;
										cP = x;
										for (int z = 0; z < c10d; z++) {
											cycle[tcycleIndex[c10]].turn(true);
											movesMade.push(tcycleIndex[c10], 1);
											for (int z1 = 0; z1 < 2; z1++) {
												boolean dummy = NCECPosition[NCECTcyclePermutation[c10][z1][0]];
												for (int z2 = 0; z2 < 3; z2++) {
													NCECPosition[NCECTcyclePermutation[c10][z1][z2]] = NCECPosition[NCECTcyclePermutation[c10][z1][z2 + 1]];
												}
												NCECPosition[NCECTcyclePermutation[c10][z1][3]] = dummy;
											}
										}
										break c10loop;
									}
								}
							}
						}
					}
				}

				findNCECTcycle(i, cP);
				yin = NCECTcycleIndex;
				yid = NCECTcycleIdentifier;
				if (yid == -1) {
					int cPNCECCycle = findNCECCycle(i, cP);
					if (cPNCECCycle != -1) {
						c8loop: for (int x = 0; x < 6; x++) {
							if ((clusterElementInCycle(cP, x)) && (x != yin)) {
								c8 = x;
								cycle[tcycleIndex[c8]].turn(true);
								movesMade.push(tcycleIndex[c8], 1);
								for (int z1 = 0; z1 < 2; z1++) {
									boolean dummy = NCECPosition[NCECTcyclePermutation[c8][z1][0]];
									for (int z = 0; z < 3; z++) {
										NCECPosition[NCECTcyclePermutation[c8][z1][z]] = NCECPosition[NCECTcyclePermutation[c8][z1][z + 1]];
									}
									NCECPosition[NCECTcyclePermutation[c8][z1][3]] = dummy;
								}
								for (int z1 = 0; z1 < 2; z1++)
									for (int z = 0; z < 4; z++) {
										if (NCECTcyclePermutation[c8][z1][z] == cP) {
											if (z > 0) {
												cP = NCECTcyclePermutation[c8][z1][z - 1];
												break c8loop;
											} else {
												cP = NCECTcyclePermutation[c8][z1][3];
												break c8loop;
											}
										}
									}
							}
						}

						c9loop: for (int x = 0; x < 6; x++) {
							if (x != c8) {
								if (clusterElementInCycle(cP, x)) {
									c9 = x;
									while (!clusterElementInCycle(cP, yin)) {
										c9d++;
										cycle[tcycleIndex[c9]].turn(true);
										movesMade.push(tcycleIndex[c9], 1);

										for (int z1 = 0; z1 < 2; z1++) {
											boolean dummy = NCECPosition[NCECTcyclePermutation[c9][z1][0]];
											for (int z = 0; z < 3; z++) {
												NCECPosition[NCECTcyclePermutation[c9][z1][z]] = NCECPosition[NCECTcyclePermutation[c9][z1][z + 1]];
											}
											NCECPosition[NCECTcyclePermutation[c9][z1][3]] = dummy;
										}
										cPloop1: for (int z1 = 0; z1 < 2; z1++)
											for (int z = 0; z < 4; z++) {
												if (NCECTcyclePermutation[c9][z1][z] == cP) {
													if (z > 0) {
														cP = NCECTcyclePermutation[c9][z1][z - 1];
														break cPloop1;
													} else {
														cP = NCECTcyclePermutation[c9][z1][3];
														break cPloop1;
													}
												}
											}
									}
									findNCECTcycle(i, cP);
									yin = NCECTcycleIndex;
									yid = NCECTcycleIdentifier;
									break c9loop;
								}
							}
						}
					} else {
						c7 = findNCECCycle(cP);
						c7loop: for (int x = 0; x < 4; x++) {
							int newcP = NCECCyclePermutation[c7][x];
							if ((newcP != cP) && (findNCECTcycle(i, newcP))) {
								yin = NCECTcycleIndex;
								yid = NCECTcycleIdentifier;
								c7d = findDistanceNCEC(newcP, cP, c7, -1, false);
								for (int z1 = 0; z1 < c7d; z1++) {
									cycle[NCECCycleIndex[c7]].turn(true);
									movesMade.push(NCECCycleIndex[c7], 1);
									boolean dummy = NCECPosition[NCECCyclePermutation[c7][0]];
									for (int z = 0; z < 3; z++) {
										NCECPosition[NCECCyclePermutation[c7][z]] = NCECPosition[NCECCyclePermutation[c7][z + 1]];
									}

									NCECPosition[NCECCyclePermutation[c7][3]] = dummy;
								}
								cP = newcP;
								break c7loop;
							}
						}
					}
				}

				dy = findDistanceNCEC(i, cP, yin, yid, true);

				innerloopNCEC: for (int j = 0; j < 24; j++) {
					if ((j != i) && (j != cP) && (!NCECPosition[j])) {
						if (clusterElementInCycle(j, yin)) {
							c4loop: for (int w = 0; w < 6; w++) {
								if (clusterElementInCycle(j, w)) {
									if ((!clusterElementInCycle(cP, w))
											&& (!clusterElementInCycle(i, w))) {
										c4 = w;
										cycle[tcycleIndex[c4]].turn(true);
										movesMade.push(tcycleIndex[c4], 1);
										for (int x = 0; x < 4; x++) {
											if (NCECTcyclePermutation[c4][0][x] == j) {
												if (x > 0)
													j = NCECTcyclePermutation[c4][0][x - 1];
												else
													j = NCECTcyclePermutation[c4][0][3];
												break c4loop;
											}
											if (NCECTcyclePermutation[c4][1][x] == j) {
												if (x > 0)
													j = NCECTcyclePermutation[c4][1][x - 1];
												else
													j = NCECTcyclePermutation[c4][1][3];
												break c4loop;
											}
										}
									}
								}
							}
							if (c4 == -1) {
								c56loop: for (int w = 0; w < 6; w++) {
									if (w != yin) {
										if (clusterElementInCycle(j, w)) {
											c5 = w;
											cycle[tcycleIndex[c5]].turn(true);
											movesMade.push(tcycleIndex[c5], 1);
											c5loop: for (int x = 0; x < 4; x++) {
												if (NCECTcyclePermutation[c5][0][x] == j) {
													if (x > 0)
														j = NCECTcyclePermutation[c5][0][x - 1];
													else
														j = NCECTcyclePermutation[c5][0][3];
													break c5loop;
												}
												if (NCECTcyclePermutation[c5][1][x] == j) {
													if (x > 0)
														j = NCECTcyclePermutation[c5][1][x - 1];
													else
														j = NCECTcyclePermutation[c5][1][3];
													break c5loop;
												}
											}
											c6 = findNCECCycle(j);
											cycle[NCECCycleIndex[c6]]
													.turn(true);
											cycle[NCECCycleIndex[c6]]
													.turn(true);
											movesMade.push(NCECCycleIndex[c6],
													2);
											c6loop: for (int x = 0; x < 4; x++) {
												if (NCECCyclePermutation[c6][x] == j) {
													if (x > 1)
														j = NCECCyclePermutation[c6][x - 2];
													else
														j = NCECCyclePermutation[c6][x + 2];
													break c6loop;
												}
											}
											cycle[tcycleIndex[c5]].turn(true);
											cycle[tcycleIndex[c5]].turn(true);
											cycle[tcycleIndex[c5]].turn(true);
											movesMade.push(tcycleIndex[c5], 3);
											break c56loop;
										}
									}
								}
							}
						}

						int dcycle = findDisjointTcycleNCEC(yin);
						if (clusterElementInCycle(j, dcycle)) {
							boolean seperated = false;
							c3loop: while (!seperated) {
								if (findNCECTcycle(j, i)) {
									cycle[tcycleIndex[dcycle]].turn(true);
									movesMade.push(tcycleIndex[dcycle], 1);
									c3 = dcycle;
									c3d++;
									for (int x = 0; x < 4; x++) {
										if (NCECTcyclePermutation[dcycle][0][x] == j) {
											if (x > 0)
												j = NCECTcyclePermutation[dcycle][0][x - 1];
											else
												j = NCECTcyclePermutation[dcycle][0][3];

											continue c3loop;
										}
										if (NCECTcyclePermutation[dcycle][1][x] == j) {
											if (x > 0)
												j = NCECTcyclePermutation[dcycle][1][x - 1];
											else
												j = NCECTcyclePermutation[dcycle][1][3];

											continue c3loop;
										}
									}

								}
								if (findNCECTcycle(j, cP)) {
									cycle[tcycleIndex[dcycle]].turn(true);
									movesMade.push(tcycleIndex[dcycle], 1);
									c3 = dcycle;
									c3d++;
									for (int x = 0; x < 4; x++) {
										if (NCECTcyclePermutation[dcycle][0][x] == j) {
											if (x > 0)
												j = NCECTcyclePermutation[dcycle][0][x - 1];
											else
												j = NCECTcyclePermutation[dcycle][0][3];

											continue c3loop;
										}
										if (NCECTcyclePermutation[dcycle][1][x] == j) {
											if (x > 0)
												j = NCECTcyclePermutation[dcycle][1][x - 1];
											else
												j = NCECTcyclePermutation[dcycle][1][3];

											continue c3loop;
										}
									}
								}
								seperated = true;
							}
							c2loop: for (int x = 0; x < 6; x++) {
								if (x != dcycle) {
									if (clusterElementInCycle(j, x)) {
										c2 = x;
										cycle[tcycleIndex[x]].turn(true);
										movesMade.push(tcycleIndex[x], 1);
										for (int w = 0; w < 4; w++) {
											if (NCECTcyclePermutation[c2][0][w] == j) {
												if (w > 0) {
													j = NCECTcyclePermutation[c2][0][w - 1];
													break;
												} else {
													j = NCECTcyclePermutation[c2][0][3];
													break;
												}
											}
											if (NCECTcyclePermutation[c2][1][w] == j) {
												if (w > 0) {
													j = NCECTcyclePermutation[c2][1][w - 1];
													break;
												} else {
													j = NCECTcyclePermutation[c2][1][3];
													break;
												}
											}
										}
										break c2loop;
									}
								}
							}
						}

						if (findNCECTcycle(i, j)) {
							c1 = findNCECCycle(j);
							cycle[NCECCycleIndex[c1]].turn(true);
							cycle[NCECCycleIndex[c1]].turn(true);
							movesMade.push(NCECCycleIndex[c1], 2);
							for (int x = 0; x < 4; x++) {
								if (NCECCyclePermutation[c1][x] == j) {
									if (x < 2)
										j = NCECCyclePermutation[c1][x + 2];
									else
										j = NCECCyclePermutation[c1][x - 2];
									break;
								}
							}
						}

						innermostloopNCEC: for (int im = 0; im < 24; im++) {

							int x2 = findNCECCycle(j, im);
							if (x2 == -1)
								continue innermostloopNCEC;
							if (!findNCECTcycle(i, im))
								continue innermostloopNCEC;
							int x1in = NCECTcycleIndex;
							int x1id = NCECTcycleIdentifier;
							if (x1id == -1)
								continue innermostloopNCEC;
							int dx1 = findDistanceNCEC(im, i, x1in, x1id, true);
							int dx2 = findDistanceNCEC(im, j, x2, -1, false);

							for (int t = 0; t < dx1; t++)
								cycle[tcycleIndex[x1in]].turn(true);
							movesMade.push(tcycleIndex[x1in], dx1);

							for (int t = 0; t < dx2; t++)
								cycle[NCECCycleIndex[x2]].turn(true);
							movesMade.push(NCECCycleIndex[x2], dx2);

							for (int t = 0; t < 4 - dx1; t++)
								cycle[tcycleIndex[x1in]].turn(true);
							movesMade.push(tcycleIndex[x1in], 4 - dx1);

							for (int t = 0; t < dy; t++)
								cycle[tcycleIndex[yin]].turn(true);
							movesMade.push(tcycleIndex[yin], dy);

							for (int t = 0; t < dx1; t++)
								cycle[tcycleIndex[x1in]].turn(true);
							movesMade.push(tcycleIndex[x1in], dx1);

							for (int t = 0; t < 4 - dx2; t++)
								cycle[NCECCycleIndex[x2]].turn(true);
							movesMade.push(NCECCycleIndex[x2], 4 - dx2);

							for (int t = 0; t < 4 - dx1; t++)
								cycle[tcycleIndex[x1in]].turn(true);
							movesMade.push(tcycleIndex[x1in], 4 - dx1);

							for (int t = 0; t < 4 - dy; t++)
								cycle[tcycleIndex[yin]].turn(true);
							movesMade.push(tcycleIndex[yin], 4 - dy);

							// System.out.println(cP);
							// face[NCEC[i][0][0]].rect[NCEC[i][0][1]][NCEC[i][0][2]].color
							// = Color.BLACK;
							// face[NCEC[j][0][0]].rect[NCEC[j][0][1]][NCEC[j][0][2]].color
							// = Color.CYAN;
							// face[NCEC[cP][0][0]].rect[NCEC[cP][0][1]][NCEC[cP][0][2]].color
							// = Color.GRAY;
							// if (true)
							// break outerloopNCEC;

							break innermostloopNCEC;
						}
						if (c1 != -1) {
							cycle[NCECCycleIndex[c1]].turn(true);
							cycle[NCECCycleIndex[c1]].turn(true);
							movesMade.push(NCECCycleIndex[c1], 2);
						}

						if (c2 != -1) {
							cycle[tcycleIndex[c2]].turn(true);
							cycle[tcycleIndex[c2]].turn(true);
							cycle[tcycleIndex[c2]].turn(true);
							movesMade.push(tcycleIndex[c2], 3);
						}

						if (c3 != -1) {
							for (int x = 0; x < 4 - c3d; x++)
								cycle[tcycleIndex[c3]].turn(true);
							movesMade.push(tcycleIndex[c3], 4 - c3d);
						}

						if (c4 != -1) {
							cycle[tcycleIndex[c4]].turn(true);
							cycle[tcycleIndex[c4]].turn(true);
							cycle[tcycleIndex[c4]].turn(true);
							movesMade.push(tcycleIndex[c4], 3);
						}

						if (c5 != -1) {
							cycle[tcycleIndex[c5]].turn(true);
							movesMade.push(tcycleIndex[c5], 1);

							cycle[NCECCycleIndex[c6]].turn(true);
							cycle[NCECCycleIndex[c6]].turn(true);
							movesMade.push(NCECCycleIndex[c6], 2);

							cycle[tcycleIndex[c5]].turn(true);
							cycle[tcycleIndex[c5]].turn(true);
							cycle[tcycleIndex[c5]].turn(true);
							movesMade.push(tcycleIndex[c5], 3);

						}
						break innerloopNCEC;
					}
				}
				if (c7 != -1) {
					for (int z = 0; z < 4 - c7d; z++)
						cycle[NCECCycleIndex[c7]].turn(true);
					movesMade.push(NCECCycleIndex[c7], 4 - c7d);
				}

				if (c8 != -1) {
					for (int z = 0; z < 4 - c9d; z++)
						cycle[tcycleIndex[c9]].turn(true);
					movesMade.push(tcycleIndex[c9], 4 - c9d);
					for (int z = 0; z < 3; z++)
						cycle[tcycleIndex[c8]].turn(true);
					movesMade.push(tcycleIndex[c8], 3);
				}

				if (c10 != -1) {
					for (int z = 0; z < 4 - c10d; z++)
						cycle[tcycleIndex[c10]].turn(true);
					movesMade.push(tcycleIndex[c10], 4 - c10d);
				}
				break outerloopNCEC;
			}
		}
		initializeActualColorsNCEC();
		checkNCECPositions();

		for (int x = 0; x < 24; x++)
			if (!NCECPosition[x]) {
				solveNCEC();
				break;
			}
		return true;
	}

	public static int fC[][] = new int[24][3];
	public static boolean fCPosition[] = new boolean[24];
	public static int tcyclePermutationFC[][] = new int[6][4];
	public static int dFCCycleIndex[] = new int[6];
	public static int dFCCyclePermutation[][][] = new int[6][2][4];
	public static int cFCMCycleIndex[] = new int[3];
	public static int cFCNCycleIndex[] = new int[6];
	public static int cFCMCyclePermutation[][][] = new int[3][2][4];
	public static int cFCNCyclePermutation[][] = new int[6][4];
	public static int gFCCycleIndex[] = new int[12];
	public static int gFCCyclePermutation[][] = new int[12][4];
	public static int original[] = new int[2];

	public static void initializeFC(int x, int y) {
		if ((size % 2 != 0) && (y == size / 2)) {
			int counter = 0;
			original[0] = x;
			original[1] = y;
			for (int i = 0; i < 6; i++) {
				fC[counter][0] = i;
				fC[counter][1] = x;
				fC[counter++][2] = y;

				fC[counter][0] = i;
				fC[counter][1] = size - x - 1;
				fC[counter++][2] = y;

				fC[counter][0] = i;
				fC[counter][1] = y;
				fC[counter++][2] = x;

				fC[counter][0] = i;
				fC[counter][1] = y;
				fC[counter++][2] = size - x - 1;
			}
		} else if (x == y) {
			int counter = 0;
			original[0] = x;
			original[1] = y;
			for (int i = 0; i < 6; i++) {
				fC[counter][0] = i;
				fC[counter][1] = x;
				fC[counter++][2] = y;

				fC[counter][0] = i;
				fC[counter][1] = size - x - 1;
				fC[counter++][2] = y;

				fC[counter][0] = i;
				fC[counter][1] = x;
				fC[counter++][2] = size - y - 1;

				fC[counter][0] = i;
				fC[counter][1] = size - x - 1;
				fC[counter++][2] = size - y - 1;
			}
		} else {
			int counter = 0;
			float c = size / 2;
			int displace = 0;
			if (size % 2 == 0)
				displace = 1;
			original[0] = x;
			original[1] = y;
			float dx = c - x;
			float dy = c - y;

			fC[counter][0] = 0;
			fC[counter][1] = (int) (c - dx);
			fC[counter++][2] = (int) (c - dy);

			fC[counter][0] = 0;
			fC[counter][1] = (int) (c + dy - displace);
			fC[counter++][2] = (int) (c - dx);

			fC[counter][0] = 0;
			fC[counter][1] = (int) (c + dx - displace);
			fC[counter++][2] = (int) (c + dy - displace);

			fC[counter][0] = 0;
			fC[counter][1] = (int) (c - dy);
			fC[counter++][2] = (int) (c + dx - displace);

			for (int r = 0; r < 4; r++)
				for (int n = 0; n < 2; n++) {
					int cyc = face[fC[r][0]].rect[fC[r][1]][fC[r][2]].pointingCycle[n];
					boolean found = false;
					int iterator = 0;
					while (!found) {
						if ((cycle[cyc].rectPointer[iterator][0] == fC[r][0])
								&& (cycle[cyc].rectPointer[iterator][1] == fC[r][1])
								&& (cycle[cyc].rectPointer[iterator][2] == fC[r][2])) {
							found = true;
							for (int i = 0; i < 4; i++) {
								boolean placed = false;
								int distance = (iterator + i * size)
										% (4 * size);
								for (int j = 0; j < counter; j++) {
									if ((fC[j][0] == cycle[cyc].rectPointer[distance][0])
											&& (fC[j][1] == cycle[cyc].rectPointer[distance][1])
											&& (fC[j][2] == cycle[cyc].rectPointer[distance][2]))
										placed = true;
								}
								if (!placed) {
									fC[counter][0] = cycle[cyc].rectPointer[distance][0];
									fC[counter][1] = cycle[cyc].rectPointer[distance][1];
									fC[counter++][2] = cycle[cyc].rectPointer[distance][2];
								}
							}
						}
						iterator++;
					}
				}
		}
	}

	public static void checkFCPositions() {
		for (int i = 0; i < 24; i++) {
			if (face[fC[i][0]].rect[fC[i][1]][fC[i][2]].color == face[fC[i][0]].rect[0][0].color)
				fCPosition[i] = true;
			else
				fCPosition[i] = false;
		}
	}

	public static int findTcycleIndexFC(int c) {
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 4; j++)
				if (tcyclePermutationFC[i][j] == c)
					return i;
		return -1;
	}

	public static int findDistanceTcycleFC(int i, int a, int b) {
		int d = 0;
		for (int j = 0; j < 4; j++) {
			if (tcyclePermutationFC[i][j] == a) {
				d++;
				for (int k = j + 1;; k++) {
					k = k % 4;
					if (tcyclePermutationFC[i][k] == b)
						return d;
					else
						d++;
				}
			}
		}
		return -1;
	}

	public static int findCorrectPositionFC(int i) {
		for (int cP = 0; cP < 24; cP++) {
			if ((!fCPosition[cP])
					&& (face[fC[cP][0]].rect[0][0].color == face[fC[i][0]].rect[fC[i][1]][fC[i][2]].color))
				return cP;
		}
		return -1;
	}

	public static void initializetcyclePermutationFC() {
		for (int i = 0; i < 6; i++) {
			int index = tcycleIndex[i];
			int counter = 0;
			for (int j = 0; j < size / 2; j++)
				for (int k = 0; k < 4 * (size - 2 * j - 1); k++)
					for (int c = 0; c < 24; c++) {
						if ((cycle[index].facePointer[j][k][0] == fC[c][0])
								&& (cycle[index].facePointer[j][k][1] == fC[c][1])
								&& (cycle[index].facePointer[j][k][2] == fC[c][2])) {
							tcyclePermutationFC[i][counter++] = c;
						}
					}
		}
	}

	public static void initializeDFCCycle() {
		int counteri = 0;
		for (int i = 0; i < 3 * size; i++) {
			int counterp = 0;
			int counterpn = 0;
			for (int j = 0; j < 4 * size; j++)
				for (int c = 0; c < 24; c++) {
					if ((cycle[i].rectPointer[j][0] == fC[c][0])
							&& (cycle[i].rectPointer[j][1] == fC[c][1])
							&& (cycle[i].rectPointer[j][2] == fC[c][2])) {
						if (counteri != 0)
							if (dFCCycleIndex[counteri - 1] != i)
								dFCCycleIndex[counteri++] = i;
						if (counteri == 0)
							dFCCycleIndex[counteri++] = i;
						dFCCyclePermutation[counteri - 1][counterp][counterpn] = c;
						if (counterp == 0)
							counterp = 1;
						else {
							counterp = 0;
							counterpn++;
						}
					}
				}
		}
	}

	public static int dFCCycleI, dFCCycleJ;

	public static boolean findDFCCycle(int a, int b) {
		boolean afound = false, bfound = false;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 2; j++) {
				afound = false;
				bfound = false;
				for (int k = 0; k < 4; k++) {
					if (dFCCyclePermutation[i][j][k] == a)
						afound = true;
					if (dFCCyclePermutation[i][j][k] == b)
						bfound = true;
				}
				if (afound && bfound) {
					dFCCycleI = i;
					dFCCycleJ = j;
					return true;
				}
			}
		}
		for (int i = 0; i < 6; i++) {
			afound = false;
			bfound = false;
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 4; k++) {
					if (dFCCyclePermutation[i][j][k] == a)
						afound = true;
					if (dFCCyclePermutation[i][j][k] == b)
						bfound = true;
				}

			}
			if (afound && bfound) {
				dFCCycleI = i;
				dFCCycleJ = -1;
				return true;
			}
		}
		return false;
	}

	public static int findDistanceDFCCycle(int i, int j, int a, int b) {
		int d = 0;
		for (int x = 0; x < 4; x++) {
			if (dFCCyclePermutation[i][j][x] == a) {
				d++;
				for (int y = x + 1;; y++) {
					y = y % 4;
					if (dFCCyclePermutation[i][j][y] == b)
						return d;
					else
						d++;
				}
			}
		}
		return -1;

	}

	public static boolean areOppositeFC(int a, int b) {
		if (fC[a][0] == 0 && fC[b][0] == 1)
			return true;
		if (fC[a][0] == 2 && fC[b][0] == 3)
			return true;
		if (fC[a][0] == 4 && fC[b][0] == 5)
			return true;
		if (fC[b][0] == 0 && fC[a][0] == 1)
			return true;
		if (fC[b][0] == 2 && fC[a][0] == 3)
			return true;
		if (fC[b][0] == 4 && fC[a][0] == 5)
			return true;
		return false;
	}

	public static void solveDFC() {
		outerloop: for (int i = 0; i < 24; i++) {
			checkFCPositions();
			if (!fCPosition[i]) {
				int y, dy, x1i, x1j, x2i, x2j, dx1, dx2;
				int c1 = -1;
				int c1i = -1;
				int c1j = -1;
				int c2 = -1;
				int cP = findCorrectPositionFC(i);
				if (areOppositeFC(i, cP)) {
					temp: for (int x = 0; x < 6; x++) {
						for (int ty = 0; ty < 2; ty++) {
							for (int z = 0; z < 4; z++) {
								if (dFCCyclePermutation[x][ty][z] == cP) {
									c1 = x;
									c1i = ty;
									c1j = z;
									break temp;
								}
							}
						}
					}
					cycle[dFCCycleIndex[c1]].turn(true);
					movesMade.push(dFCCycleIndex[c1], 1);
					if (c1j > 0) {
						cP = dFCCyclePermutation[c1][c1i][c1j - 1];
					} else {
						cP = dFCCyclePermutation[c1][c1i][3];
					}

					c2 = findTcycleIndexFC(cP);
					cycle[tcycleIndex[c2]].turn(true);
					cycle[tcycleIndex[c2]].turn(true);
					movesMade.push(tcycleIndex[c2], 2);

					for (int x = 0; x < 4; x++) {
						if (tcyclePermutationFC[c2][x] == cP) {
							if (x > 1) {
								cP = tcyclePermutationFC[c2][x - 2];
								break;
							} else {
								cP = tcyclePermutationFC[c2][x + 2];
								break;
							}
						}
					}

					cycle[dFCCycleIndex[c1]].turn(true);
					cycle[dFCCycleIndex[c1]].turn(true);
					cycle[dFCCycleIndex[c1]].turn(true);
					movesMade.push(dFCCycleIndex[c1], 3);
				}

				innerloop: for (int j = 0; j < 24; j++) {
					if ((j != i) && (j != cP)) {
						if (findTcycleIndexFC(i) != findTcycleIndexFC(j))
							continue innerloop;

						y = findTcycleIndexFC(i);
						dy = findDistanceTcycleFC(y, j, i);

						innermostloop: for (int im = 0; im < 24; im++) {
							if ((im != i) && (im != j) && (im != cP)) {

								int tim = findTcycleIndexFC(im);
								if (tim == findTcycleIndexFC(j))
									continue innermostloop;
								if (tim == findTcycleIndexFC(cP))
									continue innermostloop;

								if (!findDFCCycle(im, cP))
									continue innermostloop;
								if (dFCCycleJ == -1)
									continue innermostloop;

								x2i = dFCCycleI;
								x2j = dFCCycleJ;
								dx2 = findDistanceDFCCycle(x2i, x2j, im, cP);

								if (!findDFCCycle(im, j))
									continue innermostloop;
								if (dFCCycleJ == -1)
									continue innermostloop;

								x1i = dFCCycleI;
								x1j = dFCCycleJ;
								dx1 = findDistanceDFCCycle(x1i, x1j, im, j);

								if (x1i == x2i)
									continue innermostloop;
								if (dx1 == 2)
									continue innermostloop;

								for (int w = 0; w < dx1; w++)
									cycle[dFCCycleIndex[x1i]].turn(true);
								movesMade.push(dFCCycleIndex[x1i], dx1);

								for (int w = 0; w < dx2; w++)
									cycle[dFCCycleIndex[x2i]].turn(true);
								movesMade.push(dFCCycleIndex[x2i], dx2);

								for (int w = 0; w < 4 - dx1; w++)
									cycle[dFCCycleIndex[x1i]].turn(true);
								movesMade.push(dFCCycleIndex[x1i], 4 - dx1);

								for (int w = 0; w < dy; w++)
									cycle[tcycleIndex[y]].turn(true);
								movesMade.push(tcycleIndex[y], dy);

								for (int w = 0; w < dx1; w++)
									cycle[dFCCycleIndex[x1i]].turn(true);
								movesMade.push(dFCCycleIndex[x1i], dx1);

								for (int w = 0; w < 4 - dx2; w++)
									cycle[dFCCycleIndex[x2i]].turn(true);
								movesMade.push(dFCCycleIndex[x2i], 4 - dx2);

								for (int w = 0; w < 4 - dx1; w++)
									cycle[dFCCycleIndex[x1i]].turn(true);
								movesMade.push(dFCCycleIndex[x1i], 4 - dx1);

								for (int w = 0; w < 4 - dy; w++)
									cycle[tcycleIndex[y]].turn(true);
								movesMade.push(tcycleIndex[y], 4 - dy);

								// face[fC[i][0]].rect[fC[i][1]][fC[i][2]].color
								// = Color.BLACK;
								// face[fC[j][0]].rect[fC[j][1]][fC[j][2]].color
								// = Color.GRAY;
								// face[fC[im][0]].rect[fC[im][1]][fC[im][2]].color
								// = Color.MAGENTA;
								// face[fC[cP][0]].rect[fC[cP][1]][fC[cP][2]].color
								// = Color.CYAN;

								// if (true)
								// break outerloop;
								break innerloop;
							}
						}
					}
				}
				if (c1 != -1) {
					cycle[dFCCycleIndex[c1]].turn(true);
					movesMade.push(dFCCycleIndex[c1], 1);

					cycle[tcycleIndex[c2]].turn(true);
					cycle[tcycleIndex[c2]].turn(true);
					movesMade.push(tcycleIndex[c2], 2);

					cycle[dFCCycleIndex[c1]].turn(true);
					cycle[dFCCycleIndex[c1]].turn(true);
					cycle[dFCCycleIndex[c1]].turn(true);
					movesMade.push(dFCCycleIndex[c1], 3);
				}
				break outerloop;
			}
		}
		checkFCPositions();
		for (int i = 0; i < 24; i++)
			if (!fCPosition[i])
				solveDFC();
	}

	public static void initializeCFCCycle() {
		int counterm = 0;
		int countern = 0;
		for (int i = 0; i < 3 * size; i++) {
			int clustersContained = 0;
			for (int j = 0; j < 4 * size; j++)
				for (int c = 0; c < 24; c++) {
					if ((cycle[i].rectPointer[j][0] == fC[c][0])
							&& (cycle[i].rectPointer[j][1] == fC[c][1])
							&& (cycle[i].rectPointer[j][2] == fC[c][2])) {
						clustersContained++;
					}
				}
			if (clustersContained == 4)
				cFCNCycleIndex[countern++] = i;
			if (clustersContained == 8)
				cFCMCycleIndex[counterm++] = i;
		}
		for (int index = 0; index < 3; index++) {
			int i = cFCMCycleIndex[index];
			int counterp = 0;
			int counterpn = 0;
			for (int j = 0; j < 4 * size; j++)
				for (int c = 0; c < 24; c++) {
					if ((cycle[i].rectPointer[j][0] == fC[c][0])
							&& (cycle[i].rectPointer[j][1] == fC[c][1])
							&& (cycle[i].rectPointer[j][2] == fC[c][2])) {
						cFCMCyclePermutation[index][counterp][counterpn] = c;
						if (counterp == 0)
							counterp = 1;
						else {
							counterp = 0;
							counterpn++;
						}
					}
				}
		}
		for (int index = 0; index < 6; index++) {
			int i = cFCNCycleIndex[index];
			int counterp = 0;
			for (int j = 0; j < 4 * size; j++)
				for (int c = 0; c < 24; c++) {
					if ((cycle[i].rectPointer[j][0] == fC[c][0])
							&& (cycle[i].rectPointer[j][1] == fC[c][1])
							&& (cycle[i].rectPointer[j][2] == fC[c][2])) {
						cFCNCyclePermutation[index][counterp++] = c;
					}
				}
		}
	}

	public static int findCFCNCycle(int a, int b) {
		boolean afound, bfound;
		for (int x = 0; x < 6; x++) {
			afound = false;
			bfound = false;
			for (int y = 0; y < 4; y++) {
				if (cFCNCyclePermutation[x][y] == a)
					afound = true;
				if (cFCNCyclePermutation[x][y] == b)
					bfound = true;
			}
			if (afound && bfound)
				return x;
		}
		return -1;
	}

	public static int cFCMCycleI = -1, cFCMCycleJ = -1;

	public static boolean findCFCMCycle(int a, int b) {
		boolean afound, bfound;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				afound = false;
				bfound = false;
				for (int k = 0; k < 4; k++) {
					if (cFCMCyclePermutation[i][j][k] == a)
						afound = true;
					if (cFCMCyclePermutation[i][j][k] == b)
						bfound = true;
				}
				if (afound && bfound) {
					cFCMCycleI = i;
					cFCMCycleJ = j;
					return true;
				}
			}
		}
		for (int i = 0; i < 3; i++) {
			afound = false;
			bfound = false;
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 4; k++) {
					if (cFCMCyclePermutation[i][j][k] == a)
						afound = true;
					if (cFCMCyclePermutation[i][j][k] == b)
						bfound = true;
				}
			}
			if (afound && bfound) {
				cFCMCycleI = i;
				cFCMCycleJ = -1;
				return true;
			}
		}
		cFCMCycleI = -1;
		cFCMCycleJ = -1;
		return false;
	}

	public static int findDistanceCFCCycle(int i, int j, int a, int b, boolean m) {
		int d = 0;
		if (!m)
			for (int x = 0; x < 4; x++) {
				if (cFCNCyclePermutation[i][x] == a) {
					d++;
					for (int y = x + 1;; y++) {
						y = y % 4;
						if (cFCNCyclePermutation[i][y] == b)
							return d;
						else
							d++;
					}
				}
			}
		else
			for (int x = 0; x < 4; x++) {
				if (cFCMCyclePermutation[i][j][x] == a) {
					d++;
					for (int y = x + 1;; y++) {
						y = y % 4;
						if (cFCMCyclePermutation[i][j][y] == b)
							return d;
						else
							d++;
					}
				}
			}
		return -1;
	}

	public static boolean areParallel(int c, int i) {
		int ci = -1;
		outer: for (int j = 0; j < 3; j++)
			for (int k = 0; k < 2; k++)
				for (int l = 0; l < 4; l++)
					if (cFCMCyclePermutation[j][k][l] == c) {
						ci = j;
						break outer;
					}
		for (int j = 0; j < 2; j++)
			for (int k = 0; k < 4; k++)
				for (int l = 0; l < 4; l++)
					if (cFCMCyclePermutation[ci][j][k] == tcyclePermutationFC[i][l])
						return false;
		return true;
	}

	public static void solveCFC() {
		int cP, y, dy, x1i, x1j, x1d, x2, x2d, c1, c2, c3;
		outerloop: for (int i = 0; i < 24; i++) {
			c1 = -1;
			c2 = -1;
			c3 = -1;
			checkFCPositions();
			if (!fCPosition[i]) {
				cP = findCorrectPositionFC(i);
				if (areOppositeFC(i, cP)) {
					c2loop: for (int w = 0; w < 6; w++)
						for (int x = 0; x < 4; x++) {
							if (cFCNCyclePermutation[w][x] == cP) {
								c2 = w;
								cycle[cFCNCycleIndex[c2]].turn(true);
								movesMade.push(cFCNCycleIndex[c2], 1);
								if (x > 0)
									cP = cFCNCyclePermutation[c2][x - 1];
								else
									cP = cFCNCyclePermutation[c2][3];
								break c2loop;
							}
						}

					c3 = findTcycleIndexFC(cP);
					cycle[tcycleIndex[c3]].turn(true);
					movesMade.push(tcycleIndex[c3], 1);
					for (int x = 0; x < 4; x++) {
						if (tcyclePermutationFC[c3][x] == cP) {
							if (x > 0) {
								cP = tcyclePermutationFC[c3][x - 1];
								break;
							} else {
								cP = tcyclePermutationFC[c3][3];
								break;
							}
						}
					}
					cycle[cFCNCycleIndex[c2]].turn(true);
					cycle[cFCNCycleIndex[c2]].turn(true);
					cycle[cFCNCycleIndex[c2]].turn(true);
					movesMade.push(cFCNCycleIndex[c2], 3);
				}

				y = findTcycleIndexFC(i);
				if (areParallel(cP, y)) {
					c1 = findTcycleIndexFC(cP);
					cycle[tcycleIndex[c1]].turn(true);
					movesMade.push(tcycleIndex[c1], 1);
					for (int x = 0; x < 4; x++) {
						if (tcyclePermutationFC[c1][x] == cP) {
							if (x > 0) {
								cP = tcyclePermutationFC[c1][x - 1];
								break;
							} else {
								cP = tcyclePermutationFC[c1][3];
								break;
							}
						}
					}
				}
				innerloop: for (int j = 0; j < 24; j++) {
					if ((j != i) && (j != cP)) {
						if (y != findTcycleIndexFC(j))
							continue innerloop;
						if (findCFCMCycle(j, cP))
							continue innerloop;
						dy = findDistanceTcycleFC(y, j, i);

						innermostloop: for (int im = 0; im < 24; im++) {
							if ((im != i) && (im != j) && (im != cP)) {
								if (!findCFCMCycle(im, j))
									continue innermostloop;
								x1i = cFCMCycleI;
								x1j = cFCMCycleJ;
								if (x1j == -1)
									continue innermostloop;

								x2 = findCFCNCycle(im, cP);
								if (x2 == -1)
									continue innermostloop;

								x1d = findDistanceCFCCycle(x1i, x1j, im, j,
										true);
								x2d = findDistanceCFCCycle(x2, -1, im, cP,
										false);

								for (int w = 0; w < x1d; w++)
									cycle[cFCMCycleIndex[x1i]].turn(true);
								movesMade.push(cFCMCycleIndex[x1i], x1d);

								for (int w = 0; w < x2d; w++)
									cycle[cFCNCycleIndex[x2]].turn(true);
								movesMade.push(cFCNCycleIndex[x2], x2d);

								for (int w = 0; w < 4 - x1d; w++)
									cycle[cFCMCycleIndex[x1i]].turn(true);
								movesMade.push(cFCMCycleIndex[x1i], 4 - x1d);

								for (int w = 0; w < dy; w++)
									cycle[tcycleIndex[y]].turn(true);
								movesMade.push(tcycleIndex[y], dy);

								for (int w = 0; w < x1d; w++)
									cycle[cFCMCycleIndex[x1i]].turn(true);
								movesMade.push(cFCMCycleIndex[x1i], x1d);

								for (int w = 0; w < 4 - x2d; w++)
									cycle[cFCNCycleIndex[x2]].turn(true);
								movesMade.push(cFCNCycleIndex[x2], 4 - x2d);

								for (int w = 0; w < 4 - x1d; w++)
									cycle[cFCMCycleIndex[x1i]].turn(true);
								movesMade.push(cFCMCycleIndex[x1i], 4 - x1d);

								for (int w = 0; w < 4 - dy; w++)
									cycle[tcycleIndex[y]].turn(true);
								movesMade.push(tcycleIndex[y], 4 - dy);

								break innerloop;
							}
						}
					}
				}
				if (c1 != -1) {
					for (int w = 0; w < 3; w++)
						cycle[tcycleIndex[c1]].turn(true);
					movesMade.push(tcycleIndex[c1], 3);
				}
				if (c2 != -1) {
					cycle[cFCNCycleIndex[c2]].turn(true);
					movesMade.push(cFCNCycleIndex[c2], 1);

					cycle[tcycleIndex[c3]].turn(true);
					cycle[tcycleIndex[c3]].turn(true);
					cycle[tcycleIndex[c3]].turn(true);
					movesMade.push(tcycleIndex[c3], 3);

					cycle[cFCNCycleIndex[c2]].turn(true);
					cycle[cFCNCycleIndex[c2]].turn(true);
					cycle[cFCNCycleIndex[c2]].turn(true);
					movesMade.push(cFCNCycleIndex[c2], 3);
				}
				break outerloop;
			}
		}
		checkFCPositions();
		for (int w = 0; w < 24; w++)
			if (!fCPosition[w])
				solveCFC();
	}

	public static void initializeGFCCycle() {
		int counteri = 0, counterp;
		for (int i = 0; i < 3 * size; i++) {
			counterp = 0;
			for (int j = 0; j < 4 * size; j++) {
				for (int c = 0; c < 24; c++) {
					if ((cycle[i].rectPointer[j][0] == fC[c][0])
							&& (cycle[i].rectPointer[j][1] == fC[c][1])
							&& (cycle[i].rectPointer[j][2] == fC[c][2])) {
						if (counteri > 0) {
							if (gFCCycleIndex[counteri - 1] != i)
								gFCCycleIndex[counteri++] = i;
						} else {
							gFCCycleIndex[counteri++] = i;
						}
						gFCCyclePermutation[counteri - 1][counterp++] = c;
					}
				}
			}
		}
	}

	public static int findGFCCycleIndex(int a, int b) {
		boolean afound, bfound;
		for (int i = 0; i < 12; i++) {
			afound = false;
			bfound = false;
			for (int j = 0; j < 4; j++) {
				if (gFCCyclePermutation[i][j] == a)
					afound = true;
				if (gFCCyclePermutation[i][j] == b)
					bfound = true;
			}
			if (afound && bfound) {
				return i;
			}
		}
		return -1;
	}

	public static int findDistanceGFCCycle(int i, int a, int b) {
		int d = 0;
		for (int x = 0; x < 4; x++) {
			if (gFCCyclePermutation[i][x] == a) {
				d++;
				for (int y = x + 1;; y++) {
					y = y % 4;
					if (gFCCyclePermutation[i][y] == b)
						return d;
					else
						d++;
				}
			}
		}
		return -1;
	}

	public static void solveGFC() {

		int cP, y, dy, x1, x2, dx1, dx2, c1, c2;
		outerloop: for (int i = 0; i < 24; i++) {
			checkFCPositions();
			if (!fCPosition[i]) {
				c1 = -1;
				c2 = -1;
				cP = findCorrectPositionFC(i);

				if (areOppositeFC(i, cP)) {
					c1loop: for (int x = 0; x < 12; x++)
						for (int z = 0; z < 4; z++)
							if (gFCCyclePermutation[x][z] == cP) {
								c1 = x;
								cycle[gFCCycleIndex[c1]].turn(true);
								movesMade.push(gFCCycleIndex[c1], 1);
								if (z > 0)
									cP = gFCCyclePermutation[c1][z - 1];
								else
									cP = gFCCyclePermutation[c1][3];
								break c1loop;
							}
					c2 = findTcycleIndexFC(cP);
					cycle[tcycleIndex[c2]].turn(true);
					cycle[tcycleIndex[c2]].turn(true);
					movesMade.push(tcycleIndex[c2], 2);

					c2loop: for (int z = 0; z < 4; z++)
						if (tcyclePermutationFC[c2][z] == cP) {
							if (z > 1)
								cP = tcyclePermutationFC[c2][z - 2];
							else
								cP = tcyclePermutationFC[c2][z + 2];
							break c2loop;
						}
					cycle[gFCCycleIndex[c1]].turn(true);
					cycle[gFCCycleIndex[c1]].turn(true);
					cycle[gFCCycleIndex[c1]].turn(true);
					movesMade.push(gFCCycleIndex[c1], 3);
				}
				y = findTcycleIndexFC(i);
				innerloop: for (int j = 0; j < 24; j++) {
					if ((j != i) && (j != cP) && (y == findTcycleIndexFC(j))) {
						dy = findDistanceTcycleFC(y, j, i);
						if (findGFCCycleIndex(j, cP) != -1)
							continue innerloop;

						innermostloop: for (int im = 0; im < 24; im++) {
							if ((im != i) && (im != j) && (im != cP)) {
								int tim = findTcycleIndexFC(im);
								if (tim == findTcycleIndexFC(cP))
									continue innermostloop;
								if (areOppositeFC(i, im))
									continue innermostloop;
								if (y == tim)
									continue innermostloop;

								x1 = findGFCCycleIndex(j, im);
								if (x1 == -1)
									continue innermostloop;
								dx1 = findDistanceGFCCycle(x1, im, j);

								x2 = findGFCCycleIndex(cP, im);
								if (x2 == -1)
									continue innermostloop;
								dx2 = findDistanceGFCCycle(x2, im, cP);

								for (int w = 0; w < dx1; w++)
									cycle[gFCCycleIndex[x1]].turn(true);
								movesMade.push(gFCCycleIndex[x1], dx1);

								for (int w = 0; w < dx2; w++)
									cycle[gFCCycleIndex[x2]].turn(true);
								movesMade.push(gFCCycleIndex[x2], dx2);

								for (int w = 0; w < 4 - dx1; w++)
									cycle[gFCCycleIndex[x1]].turn(true);
								movesMade.push(gFCCycleIndex[x1], 4 - dx1);

								for (int w = 0; w < dy; w++)
									cycle[tcycleIndex[y]].turn(true);
								movesMade.push(tcycleIndex[y], dy);

								for (int w = 0; w < dx1; w++)
									cycle[gFCCycleIndex[x1]].turn(true);
								movesMade.push(gFCCycleIndex[x1], dx1);

								for (int w = 0; w < 4 - dx2; w++)
									cycle[gFCCycleIndex[x2]].turn(true);
								movesMade.push(gFCCycleIndex[x2], 4 - dx2);

								for (int w = 0; w < 4 - dx1; w++)
									cycle[gFCCycleIndex[x1]].turn(true);
								movesMade.push(gFCCycleIndex[x1], 4 - dx1);

								for (int w = 0; w < 4 - dy; w++)
									cycle[tcycleIndex[y]].turn(true);
								movesMade.push(tcycleIndex[y], 4 - dy);

								break innerloop;
							}
						}
					}
				}
				if (c1 != -1) {
					cycle[gFCCycleIndex[c1]].turn(true);
					movesMade.push(gFCCycleIndex[c1], 1);

					cycle[tcycleIndex[c2]].turn(true);
					cycle[tcycleIndex[c2]].turn(true);
					movesMade.push(tcycleIndex[c2], 2);

					cycle[gFCCycleIndex[c1]].turn(true);
					cycle[gFCCycleIndex[c1]].turn(true);
					cycle[gFCCycleIndex[c1]].turn(true);
					movesMade.push(gFCCycleIndex[c1], 3);
				}
				break outerloop;
			}
		}
		checkFCPositions();
		for (int w = 0; w < 24; w++)
			if (!fCPosition[w])
				solveGFC();
	}

	public static boolean undoSolution() {
		int tempc, tempi, temp;
		while (movesMade.top >= 0) {
			temp = movesMade.pop();
			tempc = temp % 4;
			tempi = temp / 4;
			for (int i = 0; i < 4 - tempc; i++)
				cycle[tempi].turn(true);
			movesToMake.push(tempi, tempc);
		}
		return true;

	}

	public static void traverseSolution(boolean forward) {
		int temp, tempi, tempc;
		if (forward) {
			if (movesToMake.top >= 0) {
				temp = movesToMake.pop();
				tempc = temp % 4;
				tempi = temp / 4;
				movesMade.push(tempi, tempc);
				if (Global.useAnimation)
					animateSolution(tempi, tempc);
				for (int w = 0; w < tempc; w++)
					cycle[tempi].turn(true);
			}
		} else {
			if (movesMade.top >= 0) {
				temp = movesMade.pop();
				tempc = temp % 4;
				tempi = temp / 4;
				movesToMake.push(tempi, tempc);
				if (Global.useAnimation)
					animateSolution(tempi, 4 - tempc);
				for (int w = 0; w < 4 - tempc; w++)
					cycle[tempi].turn(true);
			}
		}
		setCPhase();
	}

	public static int cPhase = 1;
	public static int phaseLoc[] = new int[8];
	public static int phaseCount = 0;

	public static void setCPhase() {
		for (int i = 0; i < phaseCount; i++)
			if (movesMade.top <= phaseLoc[i]) {
				cPhase = i + 1;
				break;
			}
	}

	public static void jumpToPhase(boolean next) {
		int temp, tempc, tempi, cP;
		setCPhase();
		cP = cPhase;
		if (next) {
			while ((cPhase == cP) && (movesToMake.top >= 0)) {
				temp = movesToMake.pop();
				tempc = temp % 4;
				tempi = temp / 4;
				for (int w = 0; w < tempc; w++)
					cycle[tempi].turn(true);
				movesMade.push(tempi, tempc);
				setCPhase();
			}
		} else {
			while ((cPhase == cP) && (movesMade.top >= 0)) {
				temp = movesMade.pop();
				tempc = temp % 4;
				tempi = temp / 4;
				for (int w = 0; w < 4 - tempc; w++)
					cycle[tempi].turn(true);
				movesToMake.push(tempi, tempc);
				setCPhase();
			}
		}
	}

	public static void animateSolution(int cno, int d) {
		for (int i = 0; i < 4 * Global.size; i++) {
			face[cycle[cno].rectPointer[i][0]].rect[cycle[cno].rectPointer[i][1]][cycle[cno].rectPointer[i][2]].beingCycled = true;
		}

		if (cycle[cno].terminal) {
			int fn = cycle[cno].facePointer[0][0][0];
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
					face[fn].rect[i][j].beingCycled = true;
		}
		int axis = -1;
		boolean direct = true;
		if (d == 1 || d == 2)
			direct = true;
		if (d == 3)
			direct = false;
		if (d == 3)
			d = 1;

		int avgX1 = (int) (Global.baseCoordinateSpace[face[cycle[cno].rectPointer[0][0]].rect[cycle[cno].rectPointer[0][1]][cycle[cno].rectPointer[0][2]].a][0] + Global.baseCoordinateSpace[face[cycle[cno].rectPointer[0][0]].rect[cycle[cno].rectPointer[0][1]][cycle[cno].rectPointer[0][2]].c][0]);
		int avgX2 = (int) (Global.baseCoordinateSpace[face[cycle[cno].rectPointer[size][0]].rect[cycle[cno].rectPointer[size][1]][cycle[cno].rectPointer[size][2]].a][0] + Global.baseCoordinateSpace[face[cycle[cno].rectPointer[size][0]].rect[cycle[cno].rectPointer[size][1]][cycle[cno].rectPointer[size][2]].c][0]);

		int avgY1 = (int) (Global.baseCoordinateSpace[face[cycle[cno].rectPointer[0][0]].rect[cycle[cno].rectPointer[0][1]][cycle[cno].rectPointer[0][2]].a][1] + Global.baseCoordinateSpace[face[cycle[cno].rectPointer[0][0]].rect[cycle[cno].rectPointer[0][1]][cycle[cno].rectPointer[0][2]].c][1]);
		int avgY2 = (int) (Global.baseCoordinateSpace[face[cycle[cno].rectPointer[size][0]].rect[cycle[cno].rectPointer[size][1]][cycle[cno].rectPointer[size][2]].a][1] + Global.baseCoordinateSpace[face[cycle[cno].rectPointer[size][0]].rect[cycle[cno].rectPointer[size][1]][cycle[cno].rectPointer[size][2]].c][1]);

		int avgZ1 = (int) (Global.baseCoordinateSpace[face[cycle[cno].rectPointer[0][0]].rect[cycle[cno].rectPointer[0][1]][cycle[cno].rectPointer[0][2]].a][2] + Global.baseCoordinateSpace[face[cycle[cno].rectPointer[0][0]].rect[cycle[cno].rectPointer[0][1]][cycle[cno].rectPointer[0][2]].c][2]);
		int avgZ2 = (int) (Global.baseCoordinateSpace[face[cycle[cno].rectPointer[size][0]].rect[cycle[cno].rectPointer[size][1]][cycle[cno].rectPointer[size][2]].a][2] + Global.baseCoordinateSpace[face[cycle[cno].rectPointer[size][0]].rect[cycle[cno].rectPointer[size][1]][cycle[cno].rectPointer[size][2]].c][2]);

		if (avgX1 == avgX2) {
			axis = 0;
		}
		if (avgY1 == avgY2) {
			axis = 1;
		}
		if (avgZ1 == avgZ2) {
			axis = 2;
		}

		if (axis == 0) {
			while ((thetaX2Sum < d * 1.6) && (thetaX2Sum > d * -1.6)) {
				if (direct)
					thetaX2 = -0.4;
				else
					thetaX2 = 0.4;
				rotateX2();
				thetaX2Sum += thetaX2;
				for (int i = 0; i < nco; i++)
					for (int j = 0; j < 3; j++)
						animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];

				rotateY3();
				rotateX3();
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			thetaX2Sum = 0;
		}
		if (axis == 1) {
			while ((thetaY2Sum < d * 1.6) && (thetaY2Sum > d * -1.6)) {
				if (direct)
					thetaY2 = 0.4;
				else
					thetaY2 = -0.4;
				rotateY2();
				thetaY2Sum += thetaY2;
				for (int i = 0; i < nco; i++)
					for (int j = 0; j < 3; j++)
						animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];
				rotateY3();
				rotateX3();
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			thetaY2Sum = 0;
		}
		if (axis == 2) {
			while ((thetaZ2Sum < d * 1.6) && (thetaZ2Sum > d * -1.6)) {
				if (direct)
					thetaZ2 = 0.4;
				else
					thetaZ2 = -0.4;
				rotateZ2();
				thetaZ2Sum += thetaZ2;
				for (int i = 0; i < nco; i++)
					for (int j = 0; j < 3; j++)
						animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];
				rotateY3();
				rotateX3();
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			thetaZ2Sum = 0;
		}

		for (int i = 0; i < nco; i++)
			for (int j = 0; j < 3; j++) {
				rotatingCoordinateSpace[i][j] = baseCoordinateSpace[i][j];
				animatedCoordinateSpace[i][j] = rotatingCoordinateSpace[i][j];
			}
		for (int i = 0; i < 4 * Global.size; i++) {
			face[cycle[cno].rectPointer[i][0]].rect[cycle[cno].rectPointer[i][1]][cycle[cno].rectPointer[i][2]].beingCycled = false;
		}

		if (cycle[cno].terminal) {
			int fn = cycle[cno].facePointer[0][0][0];
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
					face[fn].rect[i][j].beingCycled = false;
		}
	}

	public static void cleanMoveStack() {
		int temp, tempC, tempI, preC = -1, preI = -1, counter = 0;
		while (movesToMake.top > -1) {
			temp = movesToMake.pop();
			tempC = temp % 4;
			tempI = temp / 4;
			if (tempI == preI) {
				movesMade.pop();
				movesMade.push(tempI, preC + tempC);
				for (int i = 0; i < phaseCount; i++) {
					if (phaseLoc[i] >= counter)
						phaseLoc[i]--;
				}
				preC = preC + tempC;
			} else {
				movesMade.push(tempI, tempC);
				counter++;
				preC = tempC;
				preI = tempI;
			}
		}

		while (movesMade.top > -1) {
			counter--;
			temp = movesMade.pop();
			tempC = temp % 4;
			tempI = temp / 4;
			if (tempC > 3)
				tempC = tempC % 4;
			if (tempC == 0) {
				for (int i = 0; i < phaseCount; i++) {
					if (phaseLoc[i] >= counter)
						phaseLoc[i]--;
				}
				continue;
			}
			movesToMake.push(tempI, tempC);
		}
	}
}