import bagel.*;
import bagel.util.*;
import java.util.Properties;
/*
* Class created to store the information and statistics of a trip during the game.
* */


public class TripStat {
    private final double expectedEarnings;
    private final double penalty;
    private final int priority;

    public TripStat(double expectedEarnings, int priority, double penalty) {
        this.expectedEarnings = expectedEarnings;
        this.penalty = penalty;
        this.priority = priority;
    }

    public double getExpectedEarnings() {
        return expectedEarnings;
    }
    public double getPenalty() {
        return penalty;
    }
    public int getPriority() {
        return priority;
    }
}
