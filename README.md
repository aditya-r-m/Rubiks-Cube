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

Future work -

1) UI can be improved a lot, as I created the interface with very little knowledge of Swing.

2) Conjugates in solver can be replaced by more commutators to remove "set-up moves".

3) Brute force solver for corner peices can be replaced by another set of commutator generation logic to improve time required to solve positions in which corners are scrambled very well.

4) The code currently makes very heavy use of commutators. This is desired when most of the cube is solved in order to preserve other stickers, But introduces unnecessary moves in the beginning stages where preserving rest of the cube is not important. So more clever solution generation algorithms can be used to reduce solution length by huge factors.

5) The current solution length is bounded by O(n^2) where n is the size of cube. While it is known that O(n * log n) approach is possible. But this implementation has to be deferrd until other points are implemented.
