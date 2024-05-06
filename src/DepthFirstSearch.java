	import java.util.HashSet;
	import java.util.Set;
	import java.util.Stack;
	import java.util.ArrayList;
	import java.util.List;
	
	public class DepthFirstSearch {
	
	    private static final int MAX_DEPTH = 50; // Set a maximum depth for the search
	
	    public State exec(State initialState) {
	        Set<State> explored = new HashSet<>();
	        Stack<State> stack = new Stack<>();
	        Set<State> currentPath = new HashSet<>();
	        List<State> visitedStates = new ArrayList<>();
	
	        stack.push(initialState);
	
	        while (!stack.isEmpty()) {
	            State currentState = stack.pop();
	
	            if (currentState.isGoal()) {
	                currentPath.remove(currentState); // Remove from path before returning
	                return currentState;
	            }
	
	            if (!explored.contains(currentState) && !currentPath.contains(currentState) && currentState.getDepth() < MAX_DEPTH) {
	                visitedStates.add(currentState);
	                explored.add(currentState);
	                currentPath.add(currentState);
	
	                List<State> successors = currentState.generateSuccessors();
	
	                for (State child : successors) {
	                    if (!explored.contains(child) && !currentPath.contains(child) && !stack.contains(child)) {
	                        child.setDepth(currentState.getDepth() + 1);
	                        stack.push(child);
	                    }
	                }
	
	                currentPath.remove(currentState);
	            }
	        }
	
	        return null;
	    }
	}
