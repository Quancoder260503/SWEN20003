import java.util.ArrayList;
import java.util.Properties;

import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.*;

/**
 * This class represents the taxi driver (i.e. the player) in the game.
 */

public class Driver extends Human implements EffectApplicable {
    private Taxi currentTaxi;
    private final Trip[] TRIPS;
    private final int TAXI_EJECT_DISTANCE;
    private int tripCount;
    private Coin coinPower;
    private InvisibleStar starPower;
    private final ArrayList<Taxi> previousTaxi;
    private final Properties GAME_PROPS;
    public Driver(Point location, int maxTripCount, Properties props) {
        super(location,
                new Image(props.getProperty("gameObjects.driver.image")),
                Double.parseDouble(props.getProperty("gameObjects.driver.health")) * 100.0,
                Integer.parseInt(props.getProperty("gameObjects.driver.walkSpeedX")),
                Integer.parseInt(props.getProperty("gameObjects.driver.walkSpeedY")),
                Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                Integer.parseInt(props.getProperty("gameObjects.driver.radius")),
                Integer.parseInt(props.getProperty("gameObjects.driver.taxiGetInRadius")));
        TRIPS = new Trip[maxTripCount];
        TAXI_EJECT_DISTANCE = 50;
        previousTaxi = new ArrayList<>();
        GAME_PROPS = props;
    }

    /**
     * Check if the taxi driver is in their taxi and move with it.
     */
    public void move() {
        if(super.isGetInTaxi()) {
            super.moveWithTaxi(currentTaxi);
        }
    }
    /**
     * setting up the new taxi for the taxi driver.
     * @param taxi represents the new taxi.
     */
    public void setTaxi(Taxi taxi) {
        currentTaxi = taxi;
        setIsGetInTaxi(taxi);
    }
    /**
     * @return the current taxi of the taxi driver.
     */
    public Taxi getTaxi() {
        return currentTaxi;
    }
    /**
     * @return the last trip of the taxi driver.
     */
    public Trip getLastTrip() {
        if(tripCount == 0) {
            return null;
        }
        return TRIPS[tripCount - 1];
    }
    /**
     * Setting up the newest trip and add it to the trip collections.
     * @param trip is the newest trip.
     */
    @Override
    public void setTrip(Trip trip) {
        super.setTrip(trip);
        if(trip != null) {
            this.TRIPS[tripCount] = trip;
            tripCount++;
        }
    }
    /**
     * @return the total earning of the taxi driver in the game.
     */
    public double calculateTotalEarnings() {
        double totalEarnings = 0;
        for(Trip trip : TRIPS) {
            if (trip != null) {
                totalEarnings += trip.getFee();
            }
        }
        return totalEarnings;
    }
    /**
     * Performing collecting coin power action for both driver and their taxi.
     * @param coin represents the coin power.
     */
    public void collectPower(Coin coin) {
        coinPower = coin;
        currentTaxi.collectPower(coin);
    }

    /**
     * Performing collecting star power action for both driver and their taxi.
     * @param star represents the star power.
     */
    public void collectStar(InvisibleStar star) {
        starPower = star;
        currentTaxi.collectStar(star);
    }
    /**
     * Setting up the ejecting effect if the taxi is damaged.
     */
    public void explosionEffect() {
        super.setLocation(new Point(getX() - TAXI_EJECT_DISTANCE, getY()));
    }
    /**
     * @return true if the driver is invisible against collision.
     */
    public boolean isInvisible() {
        return (starPower != null && starPower.getIsActive());
    }
    /**
     * @return the new taxi for the driver in the game if the current one is damaged.
     */
    private Taxi generateTaxi() {
        double x = MiscUtils.selectAValue(Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter1")),
                                          Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter3")));
        double y = MiscUtils.selectAValue(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.nextSpawnMinY")),
                                          Integer.parseInt(GAME_PROPS.getProperty("gameObjects.taxi.nextSpawnMaxY")));
        return new Taxi(new Point(x, y), GAME_PROPS);
    }

    /**
     * @param taxi represents the current taxi
     * @return true if the driver has gotten into the taxi.
     */
    private boolean getInTaxi(Taxi taxi) {
        double currentDistance = this.getLocation().distanceTo(taxi.getLocation());
        return currentDistance <= super.getTaxiGetInRadius();
    }
    /**
     * Updating the attributes of the driver in accordance to the user's input.
     * @param input represents the input of the users.
     */
    public void update(Input input) {
        // Check if the taxi driver is eliminated.
        if(super.isEliminated()) {
            // Setting up the blood effect if the driver is eliminated
            if(super.getBloodEffect() == null) {
                super.setBloodEffect(new Blood(getLocation(), GAME_PROPS));
            }
            super.getBloodEffect().update(input);
            return;
        }
        // check if the current taxi is eliminated
        if(currentTaxi.isEliminated()) {
            // Setting up the new taxi if the current one is eliminated
            previousTaxi.add(currentTaxi);
            currentTaxi = generateTaxi();
            currentTaxi.collectStar(starPower);
            currentTaxi.collectPower(coinPower);
            // Performing the explosion effect to the driver and the passenger (if there is any trip occurs).
            if(!super.isEjectFromTaxi()) {
                explosionEffect();
            }
            if(this.getTrip() != null) {
                if(!super.isEjectFromTaxi()) {
                    this.getTrip().getPassenger().explosionEffect();
                }
                this.getTrip().getPassenger().setEjectFromTaxi(true);
            }
            super.setEjectFromTaxi(true);
        }
        // Updating the new priority for the passenger if the driver is holding the coin.
        if (super.getTrip() != null && coinPower != null) {
            TravelPlan tp = super.getTrip().getPassenger().getTravelPlan();
            int newPriority = tp.getPriority();
            if(!tp.getCoinPowerApplied()) {
                newPriority = coinPower.applyEffect(tp.getPriority());
            }
            if(newPriority < tp.getPriority()) {
                tp.setCoinPowerApplied();
            }
            tp.setPriority(newPriority);
        }
        if(super.isEjectFromTaxi()) {
            // Checking and performing the collision logic for the passenger.
            super.isEndCollisionTimeout();
            if(super.isInCollision()) {
                if(super.hasNotReachBumpingEnd()) {
                    super.moveCollision();
                }
                else {
                    if(input != null) {
                        adjustToInputMovement(input);
                    }
                }
                super.updateCollisionTime();
            }
            else {
                if(input != null) {
                    adjustToInputMovement(input);
                }
            }
            draw();
            // Update the attributes of the current taxi and check if the driver has reached the taxi.
            if(input != null) {
                currentTaxi.moveWithBackground(input);
            }
            currentTaxi.update(input, false);
            currentTaxi.draw();
            if(getInTaxi(currentTaxi)) {
                this.setEjectFromTaxi(false);
                if(this.getTrip() != null) {
                    this.getTrip().getPassenger().setEjectFromTaxi(false);
                }
            }
        }
        else {
            // Otherwise perform normal moving logic for driver and taxi.
            if(currentTaxi != null) {
                currentTaxi.update(input, true);
            }
            move();
            if(!super.isGetInTaxi()) {
                draw();
            }
            if(super.getTrip() != null && super.getTrip().hasReachedEnd()) {
                getTrip().end();
            }
            // the flag of the current trip renders to the screen
            if(tripCount > 0) {
                Trip lastTrip = TRIPS[tripCount - 1];
                if(!lastTrip.getPassenger().hasReachedFlag()) {
                    lastTrip.getTripEndFlag().update(input);
                }
            }
        }
        // Updating the attributes of the previous taxi.
        for(Taxi taxi : previousTaxi) {
            taxi.update(input, true);
        }
    }

    /**
     * Update the taxi driver  and passengers (if any trip occurs) locations in accordance with the user's input
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void adjustToInputMovement(Input input) {
        double directionX = 0, directionY = 0;
        if (input.wasPressed(Keys.UP)) {
            directionY = -super.getWalkSpeedY();
        } else if (input.isDown(Keys.DOWN)) {
            directionY = super.getWalkSpeedY();
        } else if (input.isDown(Keys.LEFT)) {
            directionX = - super.getWalkSpeedX();
        } else if(input.isDown(Keys.RIGHT)) {
            directionX = super.getWalkSpeedX();
        }
        super.setLocation(new Point(getX() + directionX, getY() + directionY));
        if(this.getTrip() != null && this.getTrip().getPassenger() != null) {
            Passenger currentPassenger = this.getTrip().getPassenger();
            currentPassenger.setLocation(new Point(currentPassenger.getX() + directionX,
                                                   currentPassenger.getY() + directionY));
        }
    }
}
