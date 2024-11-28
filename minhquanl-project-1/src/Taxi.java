import bagel.*;
import bagel.util.*;
import java.util.Properties;

/*
* Class created to represent Taxi entity on the game screen.
* */


public class Taxi {

    private final Properties GAME_PROPS;
    private final Properties MESS_PROPS;
    private final double ENTER_DISTANCE = 1;
    private final double MOVE_DISTANCE;
    private final Font TAXI_FONT;
    private final Image TAXI;
    private final double radius;
    private Point taxiLocation;
    private double  totalScore;
    private int coinFrameStreak;
    private  boolean dropPassenger;
    private Passenger currentPassenger;
    private boolean[] visitedPassenger;
    private boolean[] usedCoins;
    private DisplayPlayerStat currentTrip;
    private DisplayPlayerStat lastTrip;
    private final Point coinInfoLocation;
    private final int MAX_COIN_FRAME;
    // Initialization
    public Taxi(Properties gameProps, Properties messageProps, Point taxiLocation, int numPassengers, int numCoins) {
        this.GAME_PROPS = gameProps;
        this.MESS_PROPS = messageProps;
        this.taxiLocation = taxiLocation;
        radius = Double.parseDouble(gameProps.getProperty("gameObjects.taxi.radius"));
        TAXI = new Image(gameProps.getProperty("gameObjects.taxi.image"));
        TAXI_FONT = new Font(gameProps.getProperty("font"),
                             Integer.parseInt(gameProps.getProperty("gameplay.info.fontSize")));
        coinInfoLocation = new Point(Double.parseDouble(gameProps.getProperty("gameplay.coin.x")),
                                     Double.parseDouble(gameProps.getProperty("gameplay.coin.y")));
        MAX_COIN_FRAME = Integer.parseInt(gameProps.getProperty("gameObjects.coin.maxFrames"));
        MOVE_DISTANCE  = Double.parseDouble(gameProps.getProperty("gameObjects.taxi.speedX"));
        visitedPassenger = new boolean[numPassengers];
        usedCoins = new boolean[numCoins];
        totalScore = 0;
        coinFrameStreak = -1;
        dropPassenger = false;
    }
   // display information about the taxi of the current player.
    public void displayTaxi() {
        if(currentTrip != null) {
            currentTrip.displayStat();
        }
        if(lastTrip != null) {
            lastTrip.displayStat();
        }
        if(coinFrameStreak != -1) {
            TAXI_FONT.drawString(String.format("%d", coinFrameStreak), coinInfoLocation.x, coinInfoLocation.y);
        }
        TAXI.draw(taxiLocation.x, taxiLocation.y);
    }
    // Method used to set up display information of the last trip
    private void setLastTrip(double expEarn, double penalty) {
        lastTrip = new DisplayPlayerStat(this.GAME_PROPS, this.MESS_PROPS, false);
        lastTrip.setStat(expEarn, currentPassenger.getPriority(),penalty);
        currentTrip = null;
    }
    // Method used to set up display information of the current trip
    private void setCurrentTrip() {
        lastTrip = null;
        currentTrip = new DisplayPlayerStat(this.GAME_PROPS, this.MESS_PROPS, true);
        currentTrip.setStat(currentPassenger.getExpectedEarnings(), currentPassenger.getPriority(), 0.0);
    }
    // Calculate the expected point gained by the taxi at the end of the trip.
    public void passengerLeave() {
        if(this.currentPassenger.reachEndFlag()) {
            double distanceY = taxiLocation.y - currentPassenger.getTripEndFlagY();
            double penalty =  currentPassenger.getPenaltyPerY() * Math.abs(distanceY);
            double expEarn =  currentPassenger.getExpectedEarnings();
            totalScore += Math.max(0.0, expEarn - penalty);
            setLastTrip(expEarn, penalty);
            dropPassenger = false;
            currentPassenger = null;
        }
    }
    // method to update the status of the frame streak triggered by using coins
    // coinFrameStreak = -1 then currently there is no streak
    // coinFrameStreak > MAX_COIN_FRAME (i.e. 500) then the period of that coin is over, set to -1 again.
    // otherwise keep padding the value by 1.
    private void updateCoinStreak() {
        if(coinFrameStreak == -1) return;
        coinFrameStreak = coinFrameStreak + 1;
        if(coinFrameStreak > MAX_COIN_FRAME) {
            coinFrameStreak = -1;
        }
    }
    // Move the taxi entity on the game screen
    public boolean move(Input input) {
        // update the current status of the coin streak
        updateCoinStreak();
        // If there is a passenger on the taxi and the coin Frame Streak is still going on then
        // try to update passenger's priority
        if(this.getPassenger() && coinFrameStreak > 0) {
            currentPassenger.updatePriority();
        }
        // check if the passenger has got into taxi
        if(this.getPassenger() && !currentPassenger.getOnTaxi()) {
            this.currentPassenger.moveToTaxi(this);
            this.passengerInside();
            return false;
        }
        else if(this.getPassenger() && dropPassenger) {
            // check if the current passenger has moved to flag.
             this.currentPassenger.moveToFlag();
             this.passengerLeave();
             return false;
        }
        else {
            double taxiX = this.taxiLocation.x;
            double taxiY = this.taxiLocation.y;
            if(input.wasPressed(Keys.RIGHT) || input.isDown(Keys.RIGHT)) {
                taxiX += MOVE_DISTANCE;
            }
            if(input.wasPressed(Keys.LEFT) || input.isDown(Keys.LEFT)) {
                taxiX -= MOVE_DISTANCE;
            }
            this.taxiLocation = new Point(taxiX, taxiY);
            if(this.getPassenger()) {
                // updating passenger location and check if he has reached the end point.
                currentPassenger.updateLocation(new Point(taxiX, taxiY));
                currentPassenger.isDropOff(input, this);
                setCurrentTrip();
            }
        }
        return true;
    }
    // Check if passenger has been closed enough to enter the car.
    public void passengerInside() {
        if(currentPassenger.getPosition().distanceTo(taxiLocation) <= ENTER_DISTANCE) {
            currentPassenger.setOnTaxi(true);
        }
    }
    // Set the beginning frame of the frame streak which is triggered by colliding a coin
    public void setMaxFrame() {
        this.coinFrameStreak = 0;
    }
    // Get the current passenger of taxi's object.
    public Passenger getCurrentPassenger() {
        return currentPassenger;
    }
    // Get Taxi current Location
    public Point getTaxiLocation() {
        return taxiLocation;
    }
    // Check if the taxi is currently carrying a passenger.
    public boolean getPassenger() {
        return (currentPassenger != null);
    }
    // Set the current passenger of the Taxi.
    public void setPassenger(Passenger passenger, int id) {
        visitedPassenger[id] = true;
        currentPassenger = passenger;
        setCurrentTrip();
    }
    // Set true if Taxi has dropped the passenger, false otherwise.
    public void setDropPassenger(boolean state) {
        dropPassenger = state;
    }
    // Check if a passenger has visited taxi.
    public boolean hasVisited(int id) {
        return visitedPassenger[id];
    }
    // Get the radius of taxi
    public double getRadius() {
        return radius;
    }
    // Check if the taxi has used the coin before.
    public boolean usedBefore(int id) { return usedCoins[id]; }
    // Marked if the taxi used this coin
    public void markedCoin(int id) {
        usedCoins[id] = true;
    }
    // Get the current total score of Taxi
    public double getTotalScore() {
        return totalScore;
    }

}
