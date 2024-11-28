import bagel.Input;
import bagel.Keys;
import bagel.util.*;
import bagel.Image;

/***
 * This class contains implementation for basic functionalities of Game Small Entity Object (i.e. Coin, Star, Fire,
 * Smoke, Blood, Fireball).
 */

public abstract class GameSmallEntity {
    private Point location;
    private final Image IMAGE;
    private final int movingSpeedY;
    private int moveY;
    public GameSmallEntity(Point location, int movingSpeedY, Image IMAGE) {
        this.IMAGE = IMAGE;
        this.location = location;
        this.movingSpeedY = movingSpeedY;
        this.moveY = 0;
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
     * @return the current location of the object.
     * */
    public Point getLocation() {
        return this.location;
    }
    /**
     * setting the new location of the object
     * @param location the new location of the object.
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * moving vertically by adjusting the coordinates of the object.
     */
    public void move() {
        this.location = new Point(location.x, location.y + movingSpeedY * moveY);
    }

    /**
     * Updating the location and drawing object in accordance to the update of the player
     * @param input the input of the user.
     * */
    public void update(Input input) {
        if(input != null) {
            adjustToInputMovement(input);
        }
        move();
        draw();
    }
    /**
     * Adjust the movement direction in y-axis of the GameObject based on the keyboard input.
     * @param input The current mouse/keyboard input.
     */
    public void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
            moveY = 0;
        }
    }
    /**
     * rendering the image of the object into the game play screen.
     */
    public void draw() {
        IMAGE.draw(location.x, location.y);
    }
    /**
     * @return the moving speed of the object
     */
    public int getMovingSpeedY() {
        return movingSpeedY;
    }
}
