import bagel.util.*;
import bagel.Image;
import bagel.Input;

import java.util.Properties;

/**
 * This class contains the implementation for the functionalities of the vehicle in the game
 * (i.e. Other car, Enemy Car and Taxi).
 */

public class Vehicle extends GameMainEntity implements Attackable {
    private final int MOVING_SPEED_X;
    private int MOVING_SPEED_Y;
    private final double DAMAGE_VALUE;
    private boolean isDamaged;
    private Smoke smokeEffect;
    private Fire fireEffect;
    private final Properties GAME_PROPS;

    public Vehicle(Point location, Image IMAGE, int MOVING_SPEED_X,
                   int MOVING_SPEED_Y, double RADIUS, double DAMAGE_VALUE, double health, Properties GAME_PROPS) {
        super(location, IMAGE, health, RADIUS);
        this.MOVING_SPEED_X = MOVING_SPEED_X;
        this.MOVING_SPEED_Y = MOVING_SPEED_Y;
        this.DAMAGE_VALUE = DAMAGE_VALUE;
        this.GAME_PROPS = GAME_PROPS;
        isDamaged = false;
    }

    /**
     * Moving automatically along the game background screen.
     */
    public void move() {
        setLocation(new Point(getX(), getY() - MOVING_SPEED_Y));
    }
    /**
     * @return the moving speed in x-axis of the object.
     */
    public double getSpeedX() {
        return MOVING_SPEED_X;
    }
    /**
     * @return the moving speed in y-axis of the object.
     */
    public double getSpeedY() {
        return MOVING_SPEED_Y;
    }
    /**
     * @return true if the vehicle object is damaged.
     */
    public boolean getIsDamage() {
        return isDamaged;
    }
    /**
     * Set true if the vehicle object is damaged.
     */
    protected void setIsDamage() {
        isDamaged = true;
    }

    /**
     * Setting up the damage effect for the vehicle object.
     * @param input represents the input of the user.
     */
    public boolean setDamage(Input input) {
        if(super.isEliminated()) {
            // if the vehicle health is less than 0, then it is considered damaged.
            setIsDamage();
        }
        if(isDamaged) {
            // Setting up the fire effect.
            if(fireEffect == null) {
                fireEffect = new Fire(super.getLocation(), GAME_PROPS);
            }
            /* If the fire effect has not reached its terminated state (i.e. 20 frames) then
             keep rendering it */
            if(!fireEffect.reachTerminatedState()) {
                fireEffect.update(input);
            }
            return true;
        }
        return false;
    }
    /**
     * Updating the attributes of the object in accordance to the user's input.
     * @param input represents the input of the users.
     */
    public void update(Input input) {
        if(setDamage(input)) {
            return;
        }
        /* Check if the vehicle is in a collision and have it has finished its collision timeout */
        super.isEndCollisionTimeout();
        draw();
        if(super.isInCollision()) {
            /* If the vehicle is in a collision, then setting up the smoke effect */
            if(smokeEffect == null) {
                /* For Other car and Enemy car, their speed is adjusted after a collision */
                changeSpeedY(MiscUtils.getRandomInt(
                        Integer.parseInt(GAME_PROPS.getProperty("gameObjects.otherCar.minSpeedY")),
                        Integer.parseInt(GAME_PROPS.getProperty("gameObjects.otherCar.maxSpeedY"))));
                smokeEffect = new Smoke(super.getLocation(), GAME_PROPS);
            }
            /* If the smoke effect has not reached its terminated state (i.e. 20 frames) then
             keep rendering it */
            if(!smokeEffect.reachTerminatedState()) {
                smokeEffect.update(input);
            }
            /* Updating the frames */
            super.updateCollisionTime();
            if(super.hasNotReachBumpingEnd()) {
                /*If the vehicle has not exceeded its bumping effect period (i.e. the first 10
                10 frames of 200 collision time out frames) then displaying the bumping effect */
                super.moveCollision();
            }
            else {
                // Otherwise move normally
                move();
            }
        }
        else {
            // If it is not in any collision, set the smoke effect to be null and move normally.
            smokeEffect = null;
            move();
        }
    }
    /**
     * Changing the speed of the vehicle in y-axis
     * @param newSpeed represents the new speed of y.
     */
    protected void changeSpeedY(int newSpeed) {
        this.MOVING_SPEED_Y = newSpeed;
    }

    /**
     * Setting up the smoke effect for the objects
     * @param smokeEffect represents the smoke effect.
     */
    protected void setSmokeEffect(Smoke smokeEffect) {
        this.smokeEffect = smokeEffect;
    }

    /**
     * @return the smoke effect of the object.
     */
    protected Smoke getSmokeEffect() {
        return smokeEffect;
    }
    /**
     * inflicting damage on other objects.
     * @param other represents other Game Main Entity Object.
     */
    public void inflictingDamage(GameMainEntity other) {
        other.receiveDamage(DAMAGE_VALUE);
    }

    /**
     * Setting up a collision between this vehicle and other game main entity object
     * @param other represents other Game Main Entity Object.
     */
    public void setUpCollision(GameMainEntity other) {
        // Check if two objects collided with each other
        if(super.hasCollidedWith(other)) {
            // Check if other objects can receive damage (i.e. not in its collision time out).
            if(!other.isInCollision()) {
                // Inflicting damage on other object.
                if(other instanceof EffectApplicable && ((EffectApplicable) other).isInvisible()) {
                    // Do nothing as the other object has an active invisible star.
                }
                else {
                    // Setting up the damage to other object.
                    this.inflictingDamage(other);
                    other.setInCollision();
                    other.setDamageTakenTime(0);
                }
            }
            // Check if this object can receive damage (i.e. not in its collision timeout)
            if(!this.isInCollision()) {
                if(other instanceof Vehicle) {
                    if(this instanceof EffectApplicable && ((EffectApplicable) this).isInvisible()) {
                        // Do nothing as this object has an active invisible star.
                    }
                    else {
                        // Setting up the damage to this object.
                        ((Vehicle) other).inflictingDamage(this);
                        this.setInCollision();
                        this.setDamageTakenTime(0);
                    }
                }
            }
            // Setting up the normal collision logic for both vehicles.
            this.setCollisionTime(0);
            other.setCollisionTime(0);
            if(this.getY() < other.getY()) {
                this.setCollisionDirection(-1);
                other.setCollisionDirection(1);
            }
            else {
                this.setCollisionDirection(1);
                other.setCollisionDirection(-1);
            }
        }
    }
}
