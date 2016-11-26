# Rubiks-Cube
Emulator and solver algorithm for Rubik's cube of any size. The application has a UI based on parallel projection and mouse events.

Much of the code is commented well, but none of the methods used for solution generation are, as it is very hard to explain the solver in detail using text only. If you need to ask anything related to the code, feel free to contact me at adityam.rtm@gmail.com

Demo (Youtube) -

[![IMAGE](https://img.youtube.com/vi/0V4vL051V80/0.jpg)](https://www.youtube.com/watch?v=0V4vL051V80)



How to Use -

1) The .jar file provided in the repository is runnable (atleast JRE 1.7 is recommended).

2) Twists can be made by left clicking and dragging from one sticker to next one in cycle

3) Cube can be rotated to view from a different angle by right click and drag.

4) Remaining UI elements are the solve button, menu for selecting rubik's cube size and solution traversal menu.

Interface logic - 

The basic idea behind the interface is that Surface of cube has a fixed collection of vertices in the 3D space. The Coordinates of those vertices may change, but their relationship doesn't (unless the slices are twisted while animating, which is managed using 3 additional lists of coordinates). So, the program keeps a list of those coordinates, moves them as necessary, and draws the structure around them, performs parallel projection (z-coordinate is ignored), sorts them according to depth (can be replaced by backface culling when not animating the twists), and paints the polygons (parallelograms).
Twists are made if cursor is dragged from one sticker to the adjacent one. And if right button is used for dragging, All the coordinates in the list are rotated around origin accordingly.

Solver logic -

Solver is based on the idea of commutators, which originates from Group theory. The solver is a bit unique in the sense that it builds the cube from outside-in, while using corners as reference. The phases are listed as follows,

1) Corners are solved using brute force.
- 2x2x2 Cube requires only this phase and the solved corners will be used as reference to determine proper positions from the next step onwards

2) If cube is odd, it's face centers are aligned by a few twists.

3) If cube is odd, it's Cross Edge cluster is solved (permuted and aligned) using a set of conjugates and commutators.
- 3x3x3 Cube requires upto this step

4) In this step, Cubes's Non-Cross Edge clusters are iterated over and solved using a set of conjugates and commutators, plus a parity correction logic. This part differs from the rest of the solution in that it doesn't guarentee that every commutator will move a NCEC piece to it's right place. It simply keeps that piece as unsolved if the orientation turns out to be wrong and tries to move it's twin on the same place once the twin piece is iterated over. There is also randomness involved in selection of the NCEC piece to prevent non-halting behaviour (which originates from the weak decision making involved in this phase).
- no parity errors remain to be handled after this step

5) Now the cube's diagonal Face clusters are iterated over and solved using their own set of conjugates and commutators.
- 4x4x4 Cube is fully solved by this step

6) If cube is odd, The cube's Cross Face clusters are iterated over and solved using their specific functions.
- 5x5x5 Cube is fully solved by this step

7) The cube's General Face clusters(collections of interacting 1-sided stickers, which do not lie on diagonal or cross) are iterated over and solved.
- All sizes of Rubik's cube are in solved state after this step

Important -> If you are not familiar with the concept of commutators, I'd suggest learning [Ryan Heise's method](http://www.ryanheise.com/cube/) of solving the 3x3x3 and trying to expand the ideas involved(such as parity and clusters) for atleast upto 4x4x4.

Notes :

1) In the exported project, maximum size is set to 21, but simply an integer value in the code can be altered to generate and solve cube of any size limited only by UI limitations and system performance.

2) The only part of solution generation that uses brute force is the corner solver logic. Though brute force isn't needed and can be replaced by commutators and parity correctors, it reduces a lot of code. Thus, scrambling corners too much might cause the solver to take around 10-20 seconds to find the solution depending on position.

Some areas which can be improved -

1) UI can be improved a lot, as I created the interface with very little knowledge of Swing.

2) Conjugates in solver can be replaced by more commutators to remove "set-up moves".

3) Brute force solver for corner peices can be replaced by another set of commutator generation logic to improve time required to solve positions in which corners are scrambled very well.

4) The code currently makes very heavy use of commutators. This is desired when most of the cube is solved in order to preserve other stickers, But introduces unnecessary moves in the beginning stages where preserving rest of the cube is not important. So clever solution generation tricks can be used to reduce solution length by huge factors.

5) The current solution length is bounded by O(n^2) for cube of size 'n'. While it is known that O(n * n / log n) approach is possible.
