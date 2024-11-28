import bagel.Input;
import bagel.Keys;
import bagel.util.*;
import bagel.Image;

import java.util.Properties;

/**
 * This class represents the GameEffectEntity (i.e. Invisible Star and Coins) which is the object that can give an
 * advantage to the class which the effect is applicable (i.e. Taxi and TaxiDriver)
 * */

public class GameEffectEntity extends GameSmallEntity {
    private final int MAX_FRAMES;
    private int framesActive = 0;
    private boolean isCollided;
    private final double RADIUS;
    public GameEffectEntity(Point Location, int speedY, int MAX_FRAMES, Image IMAGE, double RADIUS) {
        super(Location, speedY, IMAGE);
        this.MAX_FRAMES = MAX_FRAMES;
        this.isCollided = false;
        this.RADIUS = RADIUS;
    }
    /**
     * @param taxi the taxi of the game
     * @return true if the object has collided with the taxi
     * */
    public boolean hasCollidedWith(Taxi taxi) {
        // if the distance between the two objects is less than the sum of their radius, they are collided
        double collisionDistance = RADIUS + taxi.getRadius();
        double currDistance = getLocation().distanceTo(taxi.getLocation());
        return currDistance <= collisionDistance;
    }
    /**
     * @param driver the taxi driver of the game
     * @return true if the object has collided with the taxi driver
     * */
    public boolean hasColliedWithDriver(Driver driver) {
        // if the distance between the two objects is less than the sum of their radius, they are collided
        double collisionDistance = RADIUS + driver.getRadius();
        double currDistance = getLocation().distanceTo(driver.getLocation());
        return currDistance <= collisionDistance;
    }
    /**
     * Updating the location and drawing object in accordance to the update of the player with a slight change as
     * the framesActive increased through updates.
     * @param input the input of the user.
     * */
    @Override
    public void update(Input input) {
        if (isCollided) {
            framesActive++;
        } else {
            if (input != null) {
                super.adjustToInputMovement(input);
            }
            super.move();
            super.draw();
        }
    }
    /**
     * Setting true if the object has collided with a taxi or a taxi driver.
     * */
    public void setIsCollided() {
        this.isCollided = true;
    }

    /**
     * @return true if the object is active (i.e. get collided and does not exceed its maximum existing frame)
     */
    public boolean getIsActive() {
        return isCollided && framesActive <= MAX_FRAMES && framesActive >= 0;
    }
    /**
     * @return the number of active frames of the object.
     * */
    public int getFramesActive() {
        return framesActive;
    }
    /**
     * @return the maximum frame that object an exist in the game.
     * */
    public int getMaxFrames(){
        return MAX_FRAMES;
    }
}
