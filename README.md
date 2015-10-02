# Rubiks-Cube
Emulator and solver algorithm for Rubik's cube of any size. The application has a UI based on parallel projection and mouse events.

How to Use -

1) The .jar file provided in the repository is runnable.

2) Twists can be made by left clicking and dragging from one sticker to next one in cycle

3) Cube can be rotated to view from a different angle by right click and drag.

4) Remaining UI elements are the solve button, menu for selecting rubik's cube size and solution traversal menu.

Note : The only part of solution generation that uses brute force is the corner solver logic. Until it is replaced by commutators in later versions, scrambling corners too much might cause the solver to take around 10-20 seconds to find the solution depending on position.

Fixed Bug - For large cubes such as 21 x 21 x 21, sometimes stickers were not initialized properly which left holes in the cube.

Known Issue - First few moves of the solution are redundent turns (this emerges from the naive depth first search for solving corners).

To work with the code, it is best to import it to eclipse.
