import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.util.*;

import java.util.Properties;

/**
 * This class represent the Passenger in the game and implements its functionalities.
 */

public class Passenger extends Human {
    private final Properties GAME_PROPS;
    private final int TAXI_DETECT_RADIUS;
    private final Properties PROPS;
    private final TravelPlan TRAVEL_PLAN;
    private final int PRIORITY_OFFSET;
    private final int EXPECTED_FEE_OFFSET;
    private int walkDirectionX;
    private int walkDirectionY;
    private final boolean HAS_UMBRELLA;
    private boolean reachedFlag;
    private final int TAXI_EJECT_DISTANCE;
    private int priorityStore = -1;

    public Passenger(Point location, int priority, int endX, int distanceY, boolean HAS_UMBRELLA,
                     Properties props) {
        super(location,
                new Image(props.getProperty("gameObjects.passenger.image")),
                Double.parseDouble(props.getProperty("gameObjects.passenger.health")) * 100,
                Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedX")),
                Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedY")),
                Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                Integer.parseInt(props.getProperty("gameObjects.passenger.radius")),
                Integer.parseInt(props.getProperty("gameObjects.passenger.taxiGetInRadius")));

        this.PROPS = props;
        this.TRAVEL_PLAN = new TravelPlan(endX, distanceY, priority, props);
        this.TAXI_DETECT_RADIUS = Integer.parseInt(props.getProperty("gameObjects.passenger.taxiDetectRadius"));

        this.PRIORITY_OFFSET = 30;
        this.EXPECTED_FEE_OFFSET = 100;
        this.TAXI_EJECT_DISTANCE = 100;
        this.GAME_PROPS = props;
        this.HAS_UMBRELLA = HAS_UMBRELLA;
    }

    /**
     * @return true if the passenger has an umbrella.
     */
    public boolean hasUmbrella() {
        return this.HAS_UMBRELLA;
    }
    /**
     * Updating the priority of the passenger considering the weather condition
     * @param state represents whether we can update the priority of the passenger if the weather is raining.
     */
    public void setUmbrellaCondition(boolean state) {
        if(state) {
            this.priorityStore = this.TRAVEL_PLAN.getPriority();
            this.TRAVEL_PLAN.setPriority(1);
        }
        else {
            this.TRAVEL_PLAN.setPriority(this.priorityStore);
            this.priorityStore = -1;
        }
    }
    /**
     * @return the temporary priority store of the passenger.
     */
    public int getPriorityStore() {
        return priorityStore;
    }
    /**
     * @return the travel plan of the passenger.
     */
    public TravelPlan getTravelPlan() {
        return TRAVEL_PLAN;
    }

    /**
     * Activating an explosion effect.
     */
    public void explosionEffect() {
        super.setLocation(new Point(getX() - TAXI_EJECT_DISTANCE, getY()));
    }

    /**
     * Update the condition of the passengers in relation to the status of the taxi driver
     * @param input represents the input of the user
     * @param taxiDriver represents the taxi driver of the game.
     */
    public void updateWithTaxi(Input input, Driver taxiDriver) {
        if(super.isEliminated()) {
            // The passenger has been eliminated from the game.
            if(super.getBloodEffect() == null) {
                // Setting up blood effect for passenger
                super.setBloodEffect(new Blood(getLocation(), GAME_PROPS));
            }
            // Rendering the blood effect on the game screen
            super.getBloodEffect().update(input);
            return;
        }
        if(super.isEjectFromTaxi()) {
            // The passenger has been ejected from the taxi
            if(super.isInCollision()) {
                // The passenger has been in the collision, then update the related attributes.
                if (super.hasNotReachBumpingEnd()) {
                    super.moveCollision();
                }
                super.updateCollisionTime();
            }
            draw();
            return;
        }
        // if the passenger is not in the taxi or the trip is completed, update the passenger status based on keyboard
        // input. This means the passenger is go down when taxi moves up.
        if(!super.isGetInTaxi() || (super.getTrip() != null && super.getTrip().isComplete())) {
            if(super.isInCollision()) {
                if (super.hasNotReachBumpingEnd()) {
                    super.moveCollision();
                }
                else {
                    if(input != null) {
                        adjustToInputMovement(input);
                    }
                    move();
                }
                super.updateCollisionTime();
            }
            else {
                if(input != null) {
                    adjustToInputMovement(input);
                }
                move();
            }
            draw();
        }

        // if the passenger is not in the taxi and there's no trip initiated, draw the priority number on the passenger.
        if(!super.isGetInTaxi() && super.getTrip() == null) {
            drawPriority();
        }

        if(adjacentToObject(taxiDriver) && !super.isGetInTaxi() && super.getTrip() == null) {
            // if the passenger has not started the trip yet,
            // Taxi must be stopped in passenger's vicinity and not having another trip.
            setIsGetInTaxi(taxiDriver.getTaxi());
            move(taxiDriver.getTaxi());
        } else if(super.isGetInTaxi()) {
            // if the passenger is in the taxi, initiate the trip and move the passenger along with the taxi.
            if(super.getTrip() == null) {
                //Create new trip
                getTravelPlan().setStartY(getY());
                Trip newTrip = new Trip(this, taxiDriver, PROPS);
                super.setTrip(newTrip);
                taxiDriver.setTrip(newTrip);
            }

            move(taxiDriver.getTaxi());
            draw();

        } else if(!super.isGetInTaxi() && super.getTrip() != null && super.getTrip().isComplete()) {
            move(taxiDriver.getTaxi());
            draw();
        }
    }

    /**
     * Draw the priority number on the passenger.
     */
    private void drawPriority() {
        Font font = new Font(PROPS.getProperty("font"),
                Integer.parseInt(PROPS.getProperty("gameObjects.passenger.fontSize")));
        font.drawString(String.valueOf(TRAVEL_PLAN.getPriority()), getX() - PRIORITY_OFFSET, getY());
        font.drawString(String.valueOf(TRAVEL_PLAN.getExpectedFee()), getX() - EXPECTED_FEE_OFFSET, getY());
    }

    /**
     * Move in relevant to the taxi and passenger's status.
     * @param taxi active taxi
     */
    private void move(Taxi taxi) {
        if (super.isGetInTaxi()) {
            // if the passenger is in the taxi, move the passenger along with the taxi.
            super.moveWithTaxi(taxi);
        } else if(!super.isGetInTaxi() && super.getTrip() != null && super.getTrip().isComplete()) {
            //walk towards end flag if the trip is completed and not in the taxi.
            if(!hasReachedFlag()) {
                TripEndFlag tef = super.getTrip().getTripEndFlag();
                walkXDirectionObj(tef.getX());
                walkYDirectionObj(tef.getY());
                walk();
            }
        } else {
            // Walk towards the taxi if other conditions are not met.
            // (That is when taxi is stopped with not having a trip and adjacent to the passenger and the passenger
            // hasn't initiated the trip yet.)
            walkXDirectionObj(taxi.getX());
            walkYDirectionObj(taxi.getY());
            walk();
        }
    }

    /**
     * Performs a normal walks and update the location accordingly.
     */
    private void walk() {
        double x = super.getX();
        double y = super.getY();
        x += + super.getWalkSpeedX() * walkDirectionX;
        y += + super.getWalkSpeedY() * walkDirectionY;
        setLocation(new Point(x, y));
    }

    /**
     * Determine the walk direction in x-axis of the passenger based on the x direction of the object.
     */
    private void walkXDirectionObj(double otherX) {
        if (otherX > getX()) {
            walkDirectionX = 1;
        } else if (otherX < getX()) {
            walkDirectionX = -1;
        } else {
            walkDirectionX = 0;
        }
    }

    /**
     * Determine the walk direction in y-axis of the passenger based on the x direction of the object.
     */
    private void walkYDirectionObj(double otherY) {
        if (otherY > getY()) {
            walkDirectionY = 1;
        } else if (otherY < getY()) {
            walkDirectionY = -1;
        } else {
            walkDirectionY = 0;
        }
    }

    /**
     * Check if the passenger has reached the end flag of the trip.
     * @return a boolean value indicating if the passenger has reached the end flag.
     */
    public boolean hasReachedFlag() {
        if(super.getTrip() != null) {
            TripEndFlag tef = super.getTrip().getTripEndFlag();
            if(tef.getX() == this.getX() && tef.getY() == this.getY()) {
                reachedFlag = true;
            }
            return reachedFlag;
        }
        return false;
    }

    /**
     * @param taxiDriver represents the taxi driver
     * @return true if all conditions are satisfied for a passenger to get into a taxi.
     */
    private boolean adjacentToObject(Driver taxiDriver) {
        // Check if Taxi is stopped and health > 0
        boolean taxiStopped = !taxiDriver.getTaxi().isMovingX() && !taxiDriver.getTaxi().isMovingY();
        // Check if Taxi is in the passenger's detect radius
        double currDistance = this.getLocation().distanceTo(taxiDriver.getTaxi().getLocation());
        // Check if Taxi is not having another trip
        boolean isHavingAnotherTrip = taxiDriver.getTrip() != null && taxiDriver.getTrip().getPassenger() != this;

        return currDistance <= TAXI_DETECT_RADIUS && taxiStopped && !isHavingAnotherTrip;
    }

}
