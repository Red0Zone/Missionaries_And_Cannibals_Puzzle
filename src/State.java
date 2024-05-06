import java.util.ArrayList;
import java.util.List;

public class State {

    private int cannibalLeft;
    private int missionaryLeft;
    private int cannibalRight;
    private int missionaryRight;
    private boolean isLeft;
    private int depth;
    private State parentState;

    public State(int cannibalLeft, int missionaryLeft, boolean isLeft, int cannibalRight, int missionaryRight) {
        this.cannibalLeft = cannibalLeft;
        this.missionaryLeft = missionaryLeft;
        this.isLeft = isLeft;
        this.cannibalRight = cannibalRight;
        this.missionaryRight = missionaryRight;
    }

    public boolean isGoal() {
        return cannibalLeft == 0 && missionaryLeft == 0;
    }

    public boolean isValid() {
        return missionaryLeft >= 0 && missionaryRight >= 0 && cannibalLeft >= 0 && cannibalRight >= 0
                && (missionaryLeft == 0 || missionaryLeft >= cannibalLeft)
                && (missionaryRight == 0 || missionaryRight >= cannibalRight);
    }

    public List<State> generateSuccessors() {
        List<State> successors = new ArrayList<>();
        int moveDirection = (isLeft) ? 1 : -1;

        testAndAdd(successors, new State(cannibalLeft, missionaryLeft - 2 * moveDirection, !isLeft,
                cannibalRight, missionaryRight + 2 * moveDirection)); // Two missionaries cross
        testAndAdd(successors, new State(cannibalLeft - 2 * moveDirection, missionaryLeft, !isLeft,
                cannibalRight + 2 * moveDirection, missionaryRight)); // Two cannibals cross
        testAndAdd(successors, new State(cannibalLeft - moveDirection, missionaryLeft - moveDirection, !isLeft,
                cannibalRight + moveDirection, missionaryRight + moveDirection)); // One missionary and one cannibal cross
        testAndAdd(successors, new State(cannibalLeft, missionaryLeft - moveDirection, !isLeft,
                cannibalRight, missionaryRight + moveDirection)); // One missionary crosses
        testAndAdd(successors, new State(cannibalLeft - moveDirection, missionaryLeft, !isLeft,
                cannibalRight + moveDirection, missionaryRight)); // One cannibal crosses

        return successors;
    }

    private void testAndAdd(List<State> successors, State newState) {
        if (newState.isValid()) {
            newState.setParentState(this);
            successors.add(newState);
        }
    }

    public void goToLeft() {
        isLeft = true;
    }

    public void goToRight() {
        isLeft = false;
    }

    public boolean isOnLeft() {
        return isLeft;
    }

    public boolean isOnRight() {
        return !isLeft;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public State getParentState() {
        return parentState;
    }

    public void setParentState(State parentState) {
        this.parentState = parentState;
    }

    @Override
    public String toString() {
        return "(" + cannibalLeft + "," + missionaryLeft + "," + (isLeft ? "L" : "R") + ","
                + cannibalRight + "," + missionaryRight + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State)) {
            return false;
        }
        State s = (State) obj;
        return s.cannibalLeft == cannibalLeft && s.missionaryLeft == missionaryLeft &&
               s.isLeft == isLeft && s.cannibalRight == cannibalRight && s.missionaryRight == missionaryRight;
    }

	public int getCannibalLeft() {
		return cannibalLeft;
	}

	public int getMissionaryLeft() {
		return missionaryLeft;
	}

	public int getCannibalRight() {
		return cannibalRight;
	}

	public int getMissionaryRight() {
		return missionaryRight;
	}

	public boolean isLeft() {
		return isLeft;
	}
}
