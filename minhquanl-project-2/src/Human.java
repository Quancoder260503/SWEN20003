import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * This class represents the human objects of the game (i.e. Driver and Passenger).
 */

public class Human extends GameMainEntity {
    private Trip currentTrip;
    private boolean isEjectFromTaxi;
    private boolean isGetInTaxi;
    private final int WALK_SPEED_X;
    private final int WALK_SPEED_Y;
    private final int SPEED_Y;
    private final int TAXI_GET_IN_RADIUS;
    private Blood bloodEffect;
    public Human(Point location, Image IMAGE, double health, int WALK_SPEED_X, int WALK_SPEED_Y, int SPEED_Y,
                              int RADIUS, int TAXI_GET_IN_RADIUS) {
        super(location, IMAGE, health, RADIUS);
        this.WALK_SPEED_X = WALK_SPEED_X;
        this.WALK_SPEED_Y = WALK_SPEED_Y;
        this.SPEED_Y = SPEED_Y;
        this.TAXI_GET_IN_RADIUS = TAXI_GET_IN_RADIUS;
        isEjectFromTaxi = false;
    }

    /**
     * Set the blood effect for human object in case it is eliminated
     * @param bloodEffect represents the blood effect of the human object.
     */
    public void setBloodEffect(Blood bloodEffect) {
        this.bloodEffect = bloodEffect;
    }
    /**
     * @return the blood effect of the human object
     */
    public Blood getBloodEffect() {
        return bloodEffect;
    }

    /**
     * @return the walk speed of the human object in x-axis
     */
    public int getWalkSpeedX() {
        return WALK_SPEED_X;
    }
    /**
     * @return the walk speed of the human object in y-axis
     */
    public int getWalkSpeedY() {
        return WALK_SPEED_Y;
    }
    /**
     * @return the distance for the human object to get in the taxi
     */
    public int getTaxiGetInRadius() {
        return TAXI_GET_IN_RADIUS;
    }

    /**
     * Set the human into the taxi if the distance is close enough.
     * @param taxi represents the taxi of the main game.
     */
    public void setIsGetInTaxi(Taxi taxi) {
        if(taxi == null) {
            isGetInTaxi = false;
        } else if(this.getLocation().distanceTo(taxi.getLocation()) <= 1) {
            isGetInTaxi = true;
        }
    }

    /**
     * @return true if the passenger has got into the taxi.
     */
    public boolean isGetInTaxi() {
        return isGetInTaxi;
    }

    /**
     * Setting the current trip for the human object
     * @param trip the current trip the human is taking.
     */
    public void setTrip(Trip trip) {
        this.currentTrip = trip;
    }
    /**
     * @return the current trip that human object is taking
     */
    public Trip getTrip() {
        return currentTrip;
    }

    /**
     * move the human object along the background image.
     */
    public void move() {
        Point newLocation = new Point(getX(), getY() + getMoveY() * SPEED_Y);
        super.setLocation(newLocation);
    }

    /**
     * If the human gets in the taxi, move them in accordance to the coordinates of the taxi
     * @param taxi represents the taxi of the game.
     */
    public void moveWithTaxi(Taxi taxi) {
        double x = taxi.getX();
        double y = taxi.getY();
        super.setLocation(new Point(x, y));
    }

    /**
     * @return true if the human is ejected from the taxi (after the taxi was damaged).
     */
    public boolean isEjectFromTaxi() {
        return isEjectFromTaxi;
    }
    /**
     * If the human has been rejected off the taxi, the state is True
     * If the human has got back to another taxi, the state if False
     * @param state represents if the human has been ejected from the taxi.
     */
    public void setEjectFromTaxi(boolean state) {
        isEjectFromTaxi = state;
    }

    /**
     * For human, if the collision occurs, then both of their x and y coordinates will move up or down by 2
     * respectively.
     */
    @Override
    public void moveCollision() {
        super.setLocation(new Point(getX() + super
                .getCollisionDirection() * 2, getY() + super.getCollisionDirection() * 2));
    }
}
