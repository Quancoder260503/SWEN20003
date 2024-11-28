import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.*;

/**
 * This class contains the basic implementations for the functionalities of the game main object (i.e. taxi, passenger,
 * driver, car, enemy car, other cars).
 */

public abstract  class GameMainEntity {
    private final Image IMAGE;
    private double health;
    private Point location;
    private int collisionDirection;
    private boolean isInCollision;
    private int collisionTime;
    private int damageTakenTime;
    private final double RADIUS;
    private int moveY = 0;
    private final int COLLISION_PERIOD;
    private final int COLLISION_TIMEOUT;
    public GameMainEntity(Point location, Image IMAGE, double health, double RADIUS) {
        this.location = location;
        this.IMAGE = IMAGE;
        this.health = health;
        this.RADIUS = RADIUS;
        COLLISION_TIMEOUT = 200;
        COLLISION_PERIOD  = 10;
        collisionTime = -1;
        damageTakenTime = -1;
    }
    /**
     * @ return true if an object is eliminated from the game (i.e. its health is less than or equal to 0).
     * */
    public boolean isEliminated() {
        return health <= 0;
    }

    /**
     * Adjust the movement direction in y-axis of the GameObject based on the keyboard input.
     * @param input The current mouse/keyboard input.
     */
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        } else if (input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }
    /**
     * @return the current movement direction in y-axis of the GameObject.
     */
    public int getMoveY() {
        return moveY;
    }

    /**
     * receive the damage inflicted by objects that able to raise an attack
     * @param damageValue represents the damage amount inflicted on the object.
     */
    public void receiveDamage(double damageValue) {
        health = health - damageValue;
    }

    /**
     * rendering the image of the object into the game play screen.
     */
    public void draw() {
        IMAGE.draw(location.x, location.y);
    }

    /**
     * setting the new location of the object
     * @param location the new location of the object.
     */
    public void setLocation(Point location) {
        this.location = location;
    }
    /**
     * @return the current location of the object.
     * */
    public Point getLocation() {
        return location;
    }
    /**
     * @return the current health of the object.
     * */
    public double getHealth() {
        return health;
    }
    /**
     * @return the current x-coordinate of the object's location.
     * */
    public double getX() {
        return location.x;
    }
    /**
     *  @return the current y-coordinate of the object's location.
     * */
    public double getY() {
        return location.y;
    }
    /**
     * @return the radius of the object.
     * */
    public double getRadius() {
        return RADIUS;
    }

    /**
     * set the new y-coordinate for the location
     * @param y new y-coordinate.
     */
    public void setY(int y) {
        this.location = new Point(location.x, y);
    }
    /**
     * @return the collisionDirection of the object (after a collision occurs, upper objects move forward (-1)
     * and lower objects move downward (+1)).
     */
    public int getCollisionDirection() {
        return collisionDirection;
    }
    /**
     * Setting the collision direction of the object
     * @param collisionDirection the collision direction of the object.
     * */
    public void setCollisionDirection(int collisionDirection) {
        this.collisionDirection = collisionDirection;
    }
    /**
     * @return true if the object has reached the end of the collision time out
    */
    private boolean endCollisionTime() {
        return damageTakenTime <= COLLISION_TIMEOUT;
    }
    /**
     * updating the collisionTime and damageTakenTime.
     * damageTakenTime is the time when the object will not take any damage inflicted by other objects
     * collisionTime is the time used to representing the bumping effect after a collision
     * */
    public void updateCollisionTime() {
        collisionTime++;
        damageTakenTime++;
    }
    /**
     * @return true if the object is in a collision
    */
    public boolean isInCollision() {
        return isInCollision;
    }
    /**
     * check if the object has reached the collision time out
     * */
    public void isEndCollisionTimeout() {
        if(isInCollision && !endCollisionTime()) {
            isInCollision = false;
            collisionTime = -1;
            damageTakenTime = -1;
        }
    }
    /**
     * @return true if the object has not reached the bumping end
     */
    public boolean hasNotReachBumpingEnd() {
        return collisionTime <= COLLISION_PERIOD;
    }
    /**
     * @param other another game main entity object
     * @return true if two game main entity object is collided
     * */
    protected boolean hasCollidedWith(GameMainEntity other) {
        double radiusDistance = this.getRadius() + other.getRadius();
        double currentDistance = this.getLocation().distanceTo(other.getLocation());
        return currentDistance <= radiusDistance;
    }
    /**
     * Setting the collision time
     * @param collisionTime the new collision time
    */
    public void setCollisionTime(int collisionTime) {
        this.collisionTime = collisionTime;
    }
    /**
     * Set true if the object is in collision
     */
    public void setInCollision() {
        this.isInCollision = true;
    }

    /**
     * set the new damage taken time
     * @param damageTakenTime represents the new damage taken time
     */
    public void setDamageTakenTime(int damageTakenTime) {
        this.damageTakenTime = damageTakenTime;
    }
    /**
     * representing the bumping effect
    */
    public void moveCollision() {
        location = new Point(getX(), getY() + collisionDirection);
    }
}
