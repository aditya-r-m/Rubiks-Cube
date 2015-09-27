package pack;

import java.util.ArrayList;

// The class uses ArrayList data structure to emulate stacks for storing solution found

public class MoveStack {
	// stack top
	public int top;

	// any move in the solution is represented by two numbers, the unique
	// identifier(index in Global.cycle[] array) of slice
	// and the count (1-3) of how many times the slice has to be turned
	public ArrayList<Float> index, count;

	public MoveStack() {
		index = new ArrayList<Float>();
		count = new ArrayList<Float>();
		top = -1;
	}

	public void push(int i, int c) {
		top++;
		index.add((float) i);
		count.add((float) c);
	}

	public int pop() {
		int tempcn = (int) (float) count.remove(count.size() - 1);
		int tempid = (int) (float) index.remove(index.size() - 1);
		top--;
		// return both id of cycle and number of turns by one return statement
		return (4 * tempid) + tempcn;
		// valid since tempcn will always be in range 1-3
	}
}
