package pack;

import java.awt.Color;

/*
 * This is the class that turns the cube into a Rubik's cube.
 * 
 * It is basically a representation of permutations that are the cycles.
 * Indirect references are heavily used in it's code
 */
public class Cycle {
	
	/* rectPointer[][] works in a similar fashion to drawingRect[][] in JImagePanel class
	 * the definition is as follows,
	 * the count of rows is 4*size, since every turn switches colors of exactly four faces
	 * the count of columns is 3, refer to the definition of drawingRect[][] in JImagePanel for their purpose
	 * 
	 * now, when we know what cycle the user has chosen, 
	 * we can simply assign each sticker 'i', the color of sticker '(i+size)%(4*size)'
	 * 
	 * this is what is done in cycle.turn()
	 * 
	 * but face turns are much more complicated, as they rotate a face as well.
	 * so we need a more complicated data structure for storing those details.
	 * facePointer[][][] does exactly that.
	 * We can imagine a face turn as many cycles of color swaps being performed with various lengths from
	 * maximum cycle length of size-1 to minimum length (4 or 1) depending on if size is even or odd
	 * 
	 * thus, facePointer[][][] can be thought of as an array of rectPointer[][] like data structure.
	 * The algorithms for initializing it are to complex to be explained well in comments and I will create a
	 * separate documentation for them.
	 */
	public int rectPointer[][], facePointer[][][], terminalFace = -1;
	public boolean terminal;
	public int cRectPointer[][], cFacePointer[][];

	public Cycle(int f, int i, int j, boolean hnotv, boolean t, int adf,
			int cycleNo) {
		rectPointer = new int[4 * Global.size][3];
		rectPointer[0][0] = f;
		rectPointer[0][1] = i;
		rectPointer[0][2] = j;
		if (Global.face[rectPointer[0][0]].rect[rectPointer[0][1]][rectPointer[0][2]].pointingCycle[0] == -1)
			Global.face[rectPointer[0][0]].rect[rectPointer[0][1]][rectPointer[0][2]].pointingCycle[0] = cycleNo;
		else
			Global.face[rectPointer[0][0]].rect[rectPointer[0][1]][rectPointer[0][2]].pointingCycle[1] = cycleNo;

		terminal = t;
		int c1, c2, prec1;

		if (hnotv) {
			c1 = Global.face[rectPointer[0][0]].rect[rectPointer[0][1]][rectPointer[0][2]].b;
			c2 = Global.face[rectPointer[0][0]].rect[rectPointer[0][1]][rectPointer[0][2]].c;
		} else {
			c1 = Global.face[rectPointer[0][0]].rect[rectPointer[0][1]][rectPointer[0][2]].c;
			c2 = Global.face[rectPointer[0][0]].rect[rectPointer[0][1]][rectPointer[0][2]].d;
		}
		for (int a = 1; a < 4 * Global.size; a++) {

			Global.findRect(c1, c2);
			for (int l = 0; l < 2; l++) {
				if ((Global.fno[l] != rectPointer[a - 1][0])
						|| (Global.a[l] != rectPointer[a - 1][1])
						|| (Global.b[l] != rectPointer[a - 1][2])) {
					rectPointer[a][0] = Global.fno[l];
					rectPointer[a][1] = Global.a[l];
					rectPointer[a][2] = Global.b[l];
					if (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].pointingCycle[0] == -1)
						Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].pointingCycle[0] = cycleNo;
					else
						Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].pointingCycle[1] = cycleNo;

				}
			}
			prec1 = c1;

			if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].a != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].a != c2))
				c1 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].a;
			else if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].b != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].b != c2))
				c1 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].b;
			else if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].c != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].c != c2))
				c1 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].c;
			else if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].d != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].d != c2))
				c1 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].d;

			if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].a != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].a != c2)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].a != c1))
				c2 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].a;
			else if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].b != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].b != c2)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].b != c1))
				c2 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].b;
			else if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].c != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].c != c2)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].c != c1))
				c2 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].c;
			else if ((Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].d != prec1)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].d != c2)
					&& (Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].d != prec1))
				c2 = Global.face[rectPointer[a][0]].rect[rectPointer[a][1]][rectPointer[a][2]].d;

		}

		if (terminal) {
			terminalFace = adf;
			int sa = 0;
			facePointer = new int[Global.size / 2][4 * (Global.size - 1)][3];
			for (int o = 0; i < Global.size / 2; i++)
				for (int p = 0; p < 4 * (Global.size - 1); p++)
					for (int q = 0; q < 3; q++)
						facePointer[o][p][q] = -1;
			for (int m = 0; m < 4 * Global.size; m++) {
				if (Global.findRect(rectPointer[m][0], rectPointer[m][1],
						rectPointer[m][2], adf) == true) {

					facePointer[0][sa][0] = Global.affno;
					facePointer[0][sa][1] = Global.afa;
					facePointer[0][sa][2] = Global.afb;
					Global.face[Global.affno].rect[Global.afa][Global.afb].markedInFaceCycle = true;

					sa++;
				}
			}

			for (int n = 1; n < Global.size / 2; n++) {
				sa = 0;
				for (int m = 0; m < 4 * (Global.size - 2 * n + 1); m++) {
					if (Global.findRect(facePointer[n - 1][m][0],
							facePointer[n - 1][m][1], facePointer[n - 1][m][2],
							adf) == true) {

						facePointer[n][sa][0] = Global.affno;
						facePointer[n][sa][1] = Global.afa;
						facePointer[n][sa][2] = Global.afb;
						Global.face[Global.affno].rect[Global.afa][Global.afb].markedInFaceCycle = true;

						sa++;
					}
				}

			}

		}

	}
	
	// swaps colors of stickers in cycle, forward flag signifies direction of turn
	public void turn(boolean forward) {

		if (forward) {
			Color tempC[] = new Color[Global.size];
			for (int i = 0; i < Global.size; i++) {
				tempC[i] = Global.face[rectPointer[i][0]].rect[rectPointer[i][1]][rectPointer[i][2]].color;
			}
			for (int i = 0; i < 3 * Global.size; i++) {
				Global.face[rectPointer[i][0]].rect[rectPointer[i][1]][rectPointer[i][2]].color = Global.face[rectPointer[i
						+ Global.size][0]].rect[rectPointer[i + Global.size][1]][rectPointer[i
						+ Global.size][2]].color;
			}
			for (int i = 0; i < Global.size; i++) {
				Global.face[rectPointer[3 * Global.size + i][0]].rect[rectPointer[3
						* Global.size + i][1]][rectPointer[3 * Global.size + i][2]].color = tempC[i];
			}

			if (terminal) {

				for (int i = 0; i < Global.size / 2; i++) {
					tempC = new Color[Global.size - 1];

					for (int j = 0; j < (Global.size - 2 * i - 1); j++) {
						tempC[j] = Global.face[facePointer[i][j][0]].rect[facePointer[i][j][1]][facePointer[i][j][2]].color;

					}
					for (int j = 0; j < 3 * (Global.size - 2 * i - 1); j++) {
						Global.face[facePointer[i][j][0]].rect[facePointer[i][j][1]][facePointer[i][j][2]].color = Global.face[facePointer[i][j
								+ Global.size - 2 * i - 1][0]].rect[facePointer[i][j
								+ Global.size - 2 * i - 1][1]][facePointer[i][j
								+ Global.size - 2 * i - 1][2]].color;
					}
					for (int j = 3 * (Global.size - 2 * i - 1); j < 4 * (Global.size
							- 2 * i - 1); j++) {

						Global.face[facePointer[i][j][0]].rect[facePointer[i][j][1]][facePointer[i][j][2]].color = tempC[j
								- 3 * (Global.size - 2 * i - 1)];
					}
				}
			}
		} else {

			// a backward turn is same as 3-forward turns, and most modern systems are powerful enough to handle it this way
			this.turn(true);
			this.turn(true);
			this.turn(true);

		}
	}

	// the remaining code is used for optimizing brute-force search for first phase of solution in large cubes.
	// it just gives the ability to apply turns to just the corners without affecting rest of the cubes.
	// not intended for use anywhere else except the first phase of generating solution
	public void initializeCornerPointers() {
		cRectPointer = new int[8][3];
		cFacePointer = new int[4][3];
		int counter = 0;
		for (int i = 0; i < 4 * Global.size; i++) {
			if (((rectPointer[i][1] == 0) || (rectPointer[i][1] == Global.size - 1))
					&& ((rectPointer[i][2] == 0) || (rectPointer[i][2] == Global.size - 1))) {
				cRectPointer[counter][0] = rectPointer[i][0];
				cRectPointer[counter][1] = rectPointer[i][1];
				cRectPointer[counter++][2] = rectPointer[i][2];
			}
		}
		counter = 0;
		for (int i = 0; i < 4 * (Global.size - 1); i++) {
			if (((facePointer[0][i][1] == 0) || (facePointer[0][i][1] == Global.size - 1))
					&& ((facePointer[0][i][2] == 0) || (facePointer[0][i][2] == Global.size - 1))) {
				cFacePointer[counter][0] = facePointer[0][i][0];
				cFacePointer[counter][1] = facePointer[0][i][1];
				cFacePointer[counter++][2] = facePointer[0][i][2];
			}
		}
	}

	public void turnCorners() {
		Color temp[] = new Color[2];
		for (int i = 0; i < 2; i++)
			temp[i] = Global.face[cRectPointer[i][0]].rect[cRectPointer[i][1]][cRectPointer[i][2]].color;
		for (int i = 0; i < 6; i++)
			Global.face[cRectPointer[i][0]].rect[cRectPointer[i][1]][cRectPointer[i][2]].color = Global.face[cRectPointer[i + 2][0]].rect[cRectPointer[i + 2][1]][cRectPointer[i + 2][2]].color;
		for (int i = 6; i < 8; i++)
			Global.face[cRectPointer[i][0]].rect[cRectPointer[i][1]][cRectPointer[i][2]].color = temp[i - 6];

		temp[0] = Global.face[cFacePointer[0][0]].rect[cFacePointer[0][1]][cFacePointer[0][2]].color;
		for (int i = 0; i < 3; i++)
			Global.face[cFacePointer[i][0]].rect[cFacePointer[i][1]][cFacePointer[i][2]].color = Global.face[cFacePointer[i + 1][0]].rect[cFacePointer[i + 1][1]][cFacePointer[i + 1][2]].color;
		Global.face[cFacePointer[3][0]].rect[cFacePointer[3][1]][cFacePointer[3][2]].color = temp[0];
	}
}
