import java.util.Properties;

/**
 * A class representing a travel plan, which has all the details of priority, coin power,
 * end location and expected fee calculation.
 */
public class TravelPlan {

    private final double END_X;
    private final int DISTANCE_Y;
    private final Properties PROPS;

    private double endY;
    private int currentPriority;
    private boolean coinPowerApplied;

    public TravelPlan(double endX, int distanceY, int priority, Properties props) {
        this.END_X = endX;
        this.DISTANCE_Y = distanceY;
        this.currentPriority = priority;
        this.PROPS = props;
    }
    /**
     * @ return x coordinates of the ending location
     * */
    public double getEndX() {
        return END_X;
    }

    /**
     * @ return the priority of the travel plan
     * */
    public int getPriority() {
        return currentPriority;
    }
    /**
     * @ return y coordinates of the ending location
     * */
    public double getEndY() {
        return endY;
    }
    /**
     * Set the ending point of the travel plan
     * @param startY the y coordinate of the starting location
    */
    public void setStartY(double startY) {
        this.endY = startY - DISTANCE_Y;
    }
    /**
     * Set the priority of the travel plan
     * */
    public void setPriority(int priority) {
        this.currentPriority = priority;
    }
    /**
     * Set true if the coin power has been applied
     */
    public void setCoinPowerApplied() {
        this.coinPowerApplied = true;
    }

    /**
     * @return true if the coin power has been applied to increase priority
     */
    public boolean getCoinPowerApplied() {
        return this.coinPowerApplied;
    }

    /**
     * Get the expected fee of the trip based on the travel distance and priority.
     * @return The expected fee of the trip.
     */
    public float getExpectedFee() {
        float ratePerY = Float.parseFloat(PROPS.getProperty("trip.rate.perY"));
        float travelPlanDistanceFee = ratePerY * DISTANCE_Y;
        float travelPlanPriorityFee = Float.parseFloat(
                PROPS.getProperty(String.format("trip.rate.priority%d", currentPriority)));
        return travelPlanDistanceFee + travelPlanPriorityFee;
    }
}
