package pack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

// The class that contains event handlers, initialization logic and main method.
public class StartingClass implements MouseInputListener {

	// The frame which will contains imagepanel
	static JFrame frame = new JFrame("Rubik's cube emulator by Aditya");
	// The panel which is an object of JImagePanel and thus contains overriden
	// paintComponent for drawing cube
	static JImagePanel imagepanel = new JImagePanel();
	// The panel for selecting cube size
	static JPanel menupanel = new JPanel();
	// Variable used to communicate right-click dragged event details from event
	// handler to another thread so as to modify rotaion of cube's view
	static MouseEvent e;

	// boolean flags for various states of application
	public static boolean solved, solutionPrepared = false,
			stopPaintCycle = false, paintStopped = false;

	// The following thread keeps updating screen whether application is in menu
	// or not
	static Thread threadpaint = new Thread() {
		public void run() {

			while (true) {
				if (!stopPaintCycle) {
					Graphics g1 = frame.getGraphics();
					if (Global.inMenu) {
						frame.revalidate();
						frame.repaint();
					} else if (g1 != null) {
						imagepanel.paintComponent(g1);
					}
					if (paintStopped)
						paintStopped = false;
				} else {
					paintStopped = true;
				}
				try {
					Thread.sleep(34);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	// The thread which changes state of cube according to user input
	static Thread threadupdate = new Thread() {
		public void run() {
			int signX = 1, signY = 1;
			while (true) {
				/*
				 * The thread catches the event e set by mouseDragged handler
				 * and extracts information to change the thetaXSum and
				 * thetaYSum values to changes how cube is rotated before
				 * drawing on screen This logic therefore binds the right-click
				 * drag to the rotation of the cube's body
				 */
				while (Global.grabbed) {
					try {
						if (e != null)
							Global.newCX = e.getX();
						if (e != null)
							Global.newCY = e.getY();
					} catch (Exception e) {
						continue;
					}
					// signX and signY decide whether to add or subtract from
					// thetaXSum and thetaYSum
					if (Global.newCX != Global.oldCX) {
						if (Global.newCX > Global.oldCX)
							signX = -1;
						else
							signX = 1;

						Global.oldCX = Global.newCX;
						Global.rotateY = true;
					}

					if (Global.newCY != Global.oldCY) {
						if (Global.newCY > Global.oldCY)
							signY = 1;
						else
							signY = -1;
						Global.oldCY = Global.newCY;
						Global.rotateX = true;
					}

					if (Global.rotateX) {
						Global.thetaXSum += (signY * Global.yIncrement);
						Global.rotateX = false;
					}
					if (Global.rotateY) {
						Global.thetaYSum += (signX * Global.xIncrement);
						Global.rotateY = false;
					}
				}
			}
		}
	};

	// initiation logic
	public static void initiate() {
		// setting up the flags
		solved = true;
		solutionPrepared = false;
		stopPaintCycle = false;
		// finding suitable length of the edge of sticker
		Global.length = 200 / Global.size;

		// nco is the total number of coordinates we are interested in i.e. only
		// the ones on the surface of the cube, not inside
		// another way to formulate the following equation is
		// (size+1)^3-(size-1)^3
		Global.nco = (int) (2 * Math.pow((Global.size + 1), 2) + 4 * (Global.size * (Global.size - 1)));

		/*
		 * initializing the coordinatespace arrays for each of the following
		 * arrays, a value at [i][0] is x-coordinate of ith vertex, the value at
		 * [i][0] is x-coordinate of ith vertex,the value at [i][1] is
		 * y-coordinate of ith vertex, and the value at [i][2] is z-coordinate
		 * of ith vertex,
		 */

		Global.coordinateSpace = new double[Global.nco][3];
		Global.baseCoordinateSpace = new double[Global.nco][3];
		Global.rotatingCoordinateSpace = new double[Global.nco][3];
		Global.animatedCoordinateSpace = new double[Global.nco][3];

		// loops to fill up details of all coordinates on the surface of the
		// cube
		int count = 0;
		for (int i = 0; i <= Global.size; i++)
			for (int j = 0; j <= Global.size; j++)
				for (int k = 0; k <= Global.size; k++) {
					if ((i == 0 || i == Global.size)
							|| (j == 0 || j == Global.size)
							|| (k == 0 || k == Global.size)) {
						Global.coordinateSpace[count][0] = k * Global.length
								- ((Global.size * Global.length) / 2);
						Global.coordinateSpace[count][1] = j * Global.length
								- ((Global.size * Global.length) / 2);
						Global.coordinateSpace[count][2] = i * Global.length
								- ((Global.size * Global.length) / 2);

						Global.baseCoordinateSpace[count][0] = Global.coordinateSpace[count][0];
						Global.baseCoordinateSpace[count][1] = Global.coordinateSpace[count][1];
						Global.baseCoordinateSpace[count][2] = Global.coordinateSpace[count][2];

						Global.rotatingCoordinateSpace[count][0] = Global.coordinateSpace[count][0];
						Global.rotatingCoordinateSpace[count][1] = Global.coordinateSpace[count][1];
						Global.rotatingCoordinateSpace[count][2] = Global.coordinateSpace[count][2];

						Global.animatedCoordinateSpace[count][0] = Global.coordinateSpace[count][0];
						Global.animatedCoordinateSpace[count][1] = Global.coordinateSpace[count][1];
						Global.animatedCoordinateSpace[count][2] = Global.coordinateSpace[count][2];

						count++;
					}
				}
		// helper methods from Global class
		Global.createFace();
		Global.createCycle();
		if (Global.isCubeSolved())
			solved = true;
		// initializing drawing data structure
		JImagePanel.drawingRect = new int[6 * (Global.size * Global.size) + 4][3];
		int counter = 0;
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < Global.size; j++)
				for (int k = 0; k < Global.size; k++) {
					JImagePanel.drawingRect[counter][0] = i;
					JImagePanel.drawingRect[counter][1] = j;
					JImagePanel.drawingRect[counter][2] = k;
					counter++;
				}
		Global.inMenu = false;
		frame.setContentPane(imagepanel);
		imagepanel.initializeDoubleBufferingComponents();
	}

	public static void main(String[] args) {

		Global.size = 2;
		Global.length = 200 / Global.size;

		// not the best way to add event listener, but worked as needed
		StartingClass s = new StartingClass();
		frame.addMouseListener(s);
		frame.addMouseMotionListener(s);

		frame.setContentPane(menupanel);

		// adding stuff and behaviour to menu
		JLabel menulabel1 = new JLabel("Select the size of cube   ");
		JLabel menulabel2 = new JLabel("  and click  ");

		JButton button = new JButton("go");
		Integer[] choices = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
				16, 17, 18, 19, 20, 21 };

		JComboBox<Integer> cb = new JComboBox<Integer>(choices);
		menupanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		menupanel.add(menulabel1, gbc);
		menupanel.add(cb, gbc);
		menupanel.add(menulabel2, gbc);
		menupanel.add(button, gbc);
		cb.setVisible(true);
		cb.setBounds(159, 81, 189, 41);
		Global.inMenu = true;
		frame.setSize(700, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.size = (int) ((JComboBox<Integer>) e.getSource())
						.getSelectedItem();
			}
		});

		// event handler for start button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				stopPaintCycle = true;
				while (!paintStopped)
					try {
						Thread.sleep(10);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				frame.remove(menupanel);
				frame.setContentPane(imagepanel);
				StartingClass.initiate();
				if (Global.size > 5)
					Global.useAnimation = false;
				else
					Global.useAnimation = true;
				stopPaintCycle = false;
				Global.inMenu = false;
			}
		});

		imagepanel.setBackground(Color.BLACK);
		threadpaint.start();
		threadupdate.start();

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		switch (arg0.getButton()) {

		case MouseEvent.BUTTON1:
			if (!Global.inMenu) {
				if (!solved && arg0.getX() > 540 && arg0.getY() > 70
						&& arg0.getX() < 630 && arg0.getY() < 110) {

					// if solution is requested, perform the following stuff
					stopPaintCycle = true;
					int phaseCounter = 0;

					for (int i = 0; i < 8; i++)
						Global.phaseLoc[i] = -1;
					while (!paintStopped)
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}

					// The following are calls to the most complex algorithms of
					// the project for creating
					// solution according to the size of the rubik's cube and
					// whether it is even or odd, much logic is same
					Global.movesMade = new MoveStack();
					Global.movesToMake = new MoveStack();
					Global.createTerminalIndices();
					Global.solveCorners(11);
					Global.applyCornerSolution();
					if (Global.movesMade.top > -1)
						Global.phaseLoc[phaseCounter++] = Global.movesMade.top;

					if (Global.size % 2 != 0) {
						Global.solveCenters();
						if (Global.movesMade.top > Global.phaseLoc[phaseCounter - 1])
							Global.phaseLoc[phaseCounter++] = Global.movesMade.top;

						Global.setReqColorsCEC();
						Global.initializeCEC();
						Global.setActualColorCEC();
						Global.initializeCECCycleIndices();
						Global.initializeCyclePermutations();
						Global.checkCECPositions();

						Global.solveCEC();
						if (Global.movesMade.top > Global.phaseLoc[phaseCounter - 1])
							Global.phaseLoc[phaseCounter++] = Global.movesMade.top;

						Global.OrientCEC();
						if (Global.movesMade.top > Global.phaseLoc[phaseCounter - 1])
							Global.phaseLoc[phaseCounter++] = Global.movesMade.top;
					}
					if (Global.size > 3) {
						for (int e = 1; e < Global.size / 2; e++) {
							Global.initializeNCEC(e);
							Global.initializeActualColorsNCEC();
							Global.initializeReqColorsNCEC();
							Global.initializeNCECCycleIndices();
							Global.initializeNCECCyclePermutations();
							Global.initializeNCECTcyclePermutations();
							Global.solveNCEC();
						}
						if (Global.movesMade.top > Global.phaseLoc[phaseCounter - 1])
							Global.phaseLoc[phaseCounter++] = Global.movesMade.top;

						for (int c = 1; c < Global.size / 2; c++) {
							Global.initializeFC(c, c);
							Global.initializetcyclePermutationFC();
							Global.initializeDFCCycle();
							Global.checkFCPositions();
							Global.solveDFC();
						}
						if (Global.movesMade.top > Global.phaseLoc[phaseCounter - 1])
							Global.phaseLoc[phaseCounter++] = Global.movesMade.top;

						if (Global.size % 2 != 0)
							for (int c = 1; c < Global.size / 2; c++) {
								Global.initializeFC(Global.size / 2, c);
								Global.initializetcyclePermutationFC();
								Global.initializeCFCCycle();
								Global.solveCFC();
							}
						if (Global.movesMade.top > Global.phaseLoc[phaseCounter - 1])
							Global.phaseLoc[phaseCounter++] = Global.movesMade.top;

						for (int cx = 1; cx < Global.size / 2; cx++)
							for (int cy = 1; cy < Global.size / 2; cy++)
								if (cx != cy) {
									Global.initializeFC(cx, cy);
									Global.initializetcyclePermutationFC();
									Global.initializeGFCCycle();
									Global.solveGFC();
								}
						if (Global.movesMade.top > Global.phaseLoc[phaseCounter - 1])
							Global.phaseLoc[phaseCounter++] = Global.movesMade.top;
					}
					Global.phaseCount = phaseCounter;
					Global.undoSolution();
					// Global.cleanMoveStack(); <-- needs debugging to remove
					// redundant moves from solution
					stopPaintCycle = false;
					solved = true;
					solutionPrepared = true;
					paintStopped = false;
					Global.setCPhase();
				}
				// logic to traverse solution
				if (solutionPrepared) {
					if (arg0.getX() > 444 && arg0.getX() < 475
							&& arg0.getY() > 575 && arg0.getY() < 600)
						Global.traverseSolution(true);
					if (arg0.getX() > 244 && arg0.getX() < 275
							&& arg0.getY() > 575 && arg0.getY() < 600)
						Global.traverseSolution(false);
					if (arg0.getX() > 545 && arg0.getX() < 585
							&& arg0.getY() > 575 && arg0.getY() < 610)
						Global.jumpToPhase(true);
					if (arg0.getX() > 145 && arg0.getX() < 185
							&& arg0.getY() > 575 && arg0.getY() < 610)
						Global.jumpToPhase(false);
				}
			}
			break;
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		if (!Global.inMenu) {
			switch (arg0.getButton()) {
			case MouseEvent.BUTTON1:
				// find what slice is user trying to turn
				if (Global.identifyRect(arg0.getX() - JImagePanel.offset,
						arg0.getY() - JImagePanel.offset)) {
					Global.initrectf = Global.idf;
					Global.initrecti = Global.idi;
					Global.initrectj = Global.idj;
					Global.twisting = true;
				} else {
					Global.initrectf = -1;
					Global.initrecti = -1;
					Global.initrectj = -1;
				}

				break;
			case MouseEvent.BUTTON3:
				// variables to store how user is rotating the cube body by right-click-drag
				Global.oldCX = arg0.getX();
				Global.oldCY = arg0.getY();
				Global.newCX = Global.oldCX;
				Global.newCY = Global.oldCY;
				Global.grabbed = true;

				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// user just released the cube
		switch (arg0.getButton()) {
		case MouseEvent.BUTTON3:
			Global.grabbed = false;
			e = null;
			break;
		case MouseEvent.BUTTON1:
			Global.twisting = false;
			break;
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// grabbed event will be dealth with in update thread above
		if (Global.grabbed)
			e = arg0;
		
		// identify the cycle according to the first and second sticker and turn the cycle
		// note that stickers are identified with three integers, face number, i and j coordinates in that face's rect array
		if (Global.twisting) {
			if (Global.identifyRect(arg0.getX() - JImagePanel.offset,
					arg0.getY() - JImagePanel.offset)) {
				if (((Global.initrectf != Global.idf)
						|| (Global.initrecti != Global.idi) || (Global.initrectj != Global.idj))
						&& (Global.initrectf != -1)
						&& (Global.initrecti != -1)
						&& (Global.initrectj != -1)) {
					Global.finalrectf = Global.idf;
					Global.finalrecti = Global.idi;
					Global.finalrectj = Global.idj;
					if (Global.identifyCycle()) {
						solutionPrepared = false;
						if (!Global.isCubeSolved())
							solved = false;
						else
							solved = true;
					}
					Global.initrectf = Global.finalrectf;
					Global.initrecti = Global.finalrecti;
					Global.initrectj = Global.finalrectj;
				}
			} else {
				Global.initrectf = -1;
				Global.initrecti = -1;
				Global.initrectj = -1;
				Global.finalrectf = -1;
				Global.finalrecti = -1;
				Global.finalrectj = -1;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
}