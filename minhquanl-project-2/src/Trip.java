import java.util.Properties;
import bagel.util.*;

/**
 * A class representing the trip in the game play.
 * It contains the passenger, driver, taxi, trip end flag and other relevant details.
 * It will calculate the fee of the trip and update the status of the trip.
 */
public class Trip {
    private final Passenger PASSENGER;
    private final Properties PROPS;
    private final TripEndFlag TRIP_END_FLAG;
    private final Driver TAXI_DRIVER;

    private boolean isComplete;
    private double fee;
    private double penalty;

    public Trip(Passenger passenger, Driver TAXI_DRIVER, Properties props) {
        this.PASSENGER = passenger;
        this.TAXI_DRIVER = TAXI_DRIVER;
        this.TRIP_END_FLAG = new TripEndFlag(new Point(passenger.getTravelPlan().getEndX(),
                                                       passenger.getTravelPlan().getEndY()), props);
        this.PROPS = props;
    }
    /**
     * @ return the passenger of this trip
     * */
    public Passenger getPassenger() {
        return PASSENGER;
    }
    /**
     * @return true if the trip is Complete
     */
    public boolean isComplete() {
        return isComplete;
    }
    /**
     * @return the trip end flag of the trip
     */
    public TripEndFlag getTripEndFlag() {
        return TRIP_END_FLAG;
    }

    /**
     * @return the fee of the trip
     */
    public double getFee() {
        return fee;
    }
    /**
     * @return the penalty of the trip
     */
    public double getPenalty() {
        return penalty;
    }
    /**
     * Check if the trip has reached the end point based on several criteria.
     * @return true if the trip has reached the end point, false otherwise.
     */
    public boolean hasReachedEnd() {
        // Taxi is stopped when it is not moving in any direction and has health > 0.
        boolean isTaxiStopped = !TAXI_DRIVER.getTaxi().isMovingY() && !TAXI_DRIVER.getTaxi().isMovingX();
        float currDistance = getCurrentDistance();
        boolean passedDropOff = hasPassedDropOff();

        // The trip is considered as reached end if the taxi is stopped and the distance between the passenger
        // and the drop-off point is less than the radius of the drop-off point.
        // Or if the passenger has passed the drop-off point and the taxi is stopped.
        return (currDistance <= TRIP_END_FLAG.getRadius() && isTaxiStopped) || (passedDropOff && isTaxiStopped);
    }

    /**
     * Check if the passenger has passed the drop-off point.
     * @return true if the passenger has passed the drop-off point, false otherwise.
     */
    private boolean hasPassedDropOff() {
        return PASSENGER.getY() < TRIP_END_FLAG.getY() && getCurrentDistance() > TRIP_END_FLAG.getRadius();
    }

    /**
     * Calculate the current distance (Euclidean) between the passenger and the drop-off point.
     * @return The current distance between the passenger and the drop-off point.
     */
    private float getCurrentDistance() {
        return (float) Math.sqrt(Math.pow(TRIP_END_FLAG.getX() - PASSENGER.getX(), 2) +
                Math.pow(TRIP_END_FLAG.getY() - PASSENGER.getY(), 2));
    }

    /**
     * End the trip (update relevant status) and calculate the fee.
     */
    public void end() {
        isComplete = true;
        PASSENGER.setIsGetInTaxi(null);
        TAXI_DRIVER.setTrip(null);
        calculateFee();
    }

    /**
     * Calculate the fee of the trip based on the travel plan details, rate and penalty if applicable.
     */
    private void calculateFee() {
        double initialFee = PASSENGER.getTravelPlan().getExpectedFee();
        // If the passenger has passed the drop-off point, a penalty will be applied to the fee.
        if (hasPassedDropOff()) {
            double penalty = Double.parseDouble(PROPS.getProperty("trip.penalty.perY")) *
                    (TRIP_END_FLAG.getY() - PASSENGER.getY());
            initialFee -= penalty;
            this.penalty = penalty;
        }

        // Fee cannot be negative.
        if(fee < 0) {
            initialFee = 0;
        }
        this.fee = initialFee;
    }
}
