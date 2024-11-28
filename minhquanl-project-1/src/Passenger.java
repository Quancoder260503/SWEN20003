import bagel.*;
import bagel.util.*;
import java.util.Properties;

/* Class created to implement important features of Passenger entity.
 */


public class Passenger {
    private final int[] PRIORITY;
    private final double MOVE_DISTANCE = 5;
    private Point passengerStartLocation;
    private TripEndFlag tripEndFlag;
    private boolean onTaxi;
    private int priority;
    private boolean hasIncreased;
    private final double distanceY;
    private final double PICK_UP_DISTANCE;
    private boolean dropOff;
    private final DisplayPassenger displayPassenger;
    private final double ratePerY;
    private final double penaltyPerY;
    private final double WalkSpeedX;
    private final double WalkSpeedY;
    // Initialization and setting up value for a passenger.
    private void setup() {
        onTaxi = false;
        hasIncreased = false;
        dropOff = false;
    }
    public Passenger(Point passengerStartLocation, Point passengerEndLocation, int priority, Properties gameProps,
                     Properties messageProps) {
        this.passengerStartLocation = passengerStartLocation;
        this.tripEndFlag   = new TripEndFlag(passengerEndLocation, gameProps);
        this.priority = priority;
        displayPassenger = new DisplayPassenger(gameProps, messageProps);
        distanceY = passengerEndLocation.y - passengerStartLocation.y;
        PICK_UP_DISTANCE = Double.parseDouble(gameProps.getProperty("gameObjects.passenger.taxiDetectRadius"));
        PRIORITY = new int[3];
        PRIORITY[0] = Integer.parseInt(gameProps.getProperty("trip.rate.priority1"));
        PRIORITY[1] = Integer.parseInt(gameProps.getProperty("trip.rate.priority2"));
        PRIORITY[2] = Integer.parseInt(gameProps.getProperty("trip.rate.priority3"));
        ratePerY = Double.parseDouble(gameProps.getProperty("trip.rate.perY"));
        penaltyPerY = Double.parseDouble(gameProps.getProperty("trip.penalty.perY"));
        WalkSpeedX = Double.parseDouble(gameProps.getProperty("gameObjects.passenger.walkSpeedX"));
        WalkSpeedY = Double.parseDouble(gameProps.getProperty("gameObjects.passenger.walkSpeedY"));
        setup();
    }
    // find the expected earnings of a passenger.
    public double getExpectedEarnings() {
        double totalEarnings = 0;
        totalEarnings = priority * PRIORITY[priority - 1];
        totalEarnings += Math.abs(distanceY) * ratePerY;
        return totalEarnings;
    }
    // Display the image of a passenger on the screen, passenger's image will be rendered only if
    // they are off the taxi.
    void displayPassengerStat() {
        if(!onTaxi || dropOff) {
            displayPassenger.displayPassenger(this);
            if(dropOff) this.tripEndFlag.displayFlag();
        }
        else {
            this.tripEndFlag.displayFlag();
        }
    }

    // Move the passenger along the background screen.
    public void move(Input input) {
        double passengerY = this.passengerStartLocation.y;
        if(input.wasPressed(Keys.UP) || input.isDown(Keys.UP)) {
            passengerY = passengerY + MOVE_DISTANCE;
        }
        passengerStartLocation = new Point(this.passengerStartLocation.x, passengerY);
        this.tripEndFlag.move(input);
    }
    // Update the new location of the passenger in the process of moving to his destination.
    private Point moveToDest(Point p) {
        double pointX = passengerStartLocation.x;
        double pointY = passengerStartLocation.y;
        if(pointX < p.x) {
            pointX = pointX + WalkSpeedX;
        }
        else {
            pointX = pointX - WalkSpeedX;
        }
        if(pointY < p.y) {
            pointY = pointY + WalkSpeedY;
        }
        else {
            pointY = pointY - WalkSpeedY;
        }
        return new Point(pointX, pointY);
    }
    // Check if the passenger reached the taxi
    public void moveToTaxi(Taxi taxi) {
        passengerStartLocation = moveToDest(taxi.getTaxiLocation());
    }
    // Check if the passenger reached the flag
    public void moveToFlag() {
        passengerStartLocation = moveToDest(this.tripEndFlag.getFlagLocation());
    }
    // Method created to drop the passenger off the taxi
    public void isDropOff(Input input, Taxi taxi) {
        if(!taxi.getPassenger()) {
            return;
        }
        if(input.isDown(Keys.LEFT) || input.isDown(Keys.RIGHT)) {
            return;
        }
        if(taxi.getTaxiLocation().distanceTo(this.tripEndFlag.getFlagLocation()) > this.tripEndFlag.getFlagRadius() &&
                taxi.getTaxiLocation().y > this.tripEndFlag.getFlagLocation().y) {
            return;
        }
        taxi.setDropPassenger(true);
        dropOff = true;
    }
    // Check if the taxi has picked up passenger.
    public boolean pickUp(Input input, Taxi taxi, int id) {
        if(taxi.getPassenger()) {
            return false;
        }
        if(input.isDown(Keys.LEFT) || input.isDown(Keys.RIGHT)) {
            return false;
        }
        if(taxi.getTaxiLocation().distanceTo(passengerStartLocation) > PICK_UP_DISTANCE) {
            return false;
        }
        taxi.setPassenger(this, id);
        return true;
    }
    // Setting the current state of passenger (i.e. whether he is on taxi)
    public void setOnTaxi(boolean onTaxi) {
        this.onTaxi = onTaxi;
        if(this.onTaxi) {
            this.tripEndFlag.setScreen(true);
        }
    }
    // Check if the passenger has reached his trip end flag.
    public boolean reachEndFlag() {
        if(passengerStartLocation.distanceTo(this.tripEndFlag.getFlagLocation()) <= 1.0) {
            onTaxi = false;
            dropOff = false;
            return true;
        }
        return false;
    }
    // Method to update priority and location of a passenger.
    public void updatePriority() {
        if(!hasIncreased) {
            hasIncreased = true;
            priority = Math.max(priority - 1, 1);
        }
    }
    public void updateLocation(Point passengerStartLocation) {
        this.passengerStartLocation = passengerStartLocation;
    }
    // Method to get the attribute of a passenger.
    public int getPriority() {
        return priority;
    }
    public boolean getOnTaxi() {
        return onTaxi;
    }

    public Point getPosition() {
        return passengerStartLocation;
    }

    public double getTripEndFlagY() {
        return tripEndFlag.getFlagLocation().y;
    }
    public double getPenaltyPerY() { return penaltyPerY; }
}
