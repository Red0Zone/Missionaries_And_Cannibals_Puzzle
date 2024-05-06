import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class BreadthFirstSearch {

	public State exec(State initialState) {
		if (initialState.isGoal()) {
			return initialState;
		}
		Queue<State> frontier = new LinkedList<State>();
		Set<State> explored = new HashSet<State>();
		frontier.add(initialState);
		while (!frontier.isEmpty()) {
		
			State state = frontier.poll();
			explored.add(state);
			List<State> successors = state.generateSuccessors();
			for (State child : successors) {
				if (!explored.contains(child)) {
					if (child.isGoal()) {
						return child;
					}
					frontier.add(child);
				}
			}
		}
		return null;
	}
}
