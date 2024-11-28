import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.*;
import java.util.Properties;

/**
 * A class representing the background of the game play.
 */
public class Background {

    private final int WINDOW_HEIGHT;
    private Image currentImage;
    private final int SPEED_Y;
    private Point location;
    private int moveY;
    private int startFrame;
    private int endFrame;
    public Background(Point location, Properties props) {
        this.location = location;
        this.moveY = 0;
        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));
        this.WINDOW_HEIGHT = Integer.parseInt(props.getProperty("window.height"));
        startFrame = 0;
        endFrame = 0;
    }
    /**
     * Move the background in y direction according to the keyboard input. And render the background image.
     * @param input The current mouse/keyboard input.
     */
    public void update(Input input, Background background) {
        startFrame++;
        if(input != null) {
            adjustToInputMovement(input);
        }

        move();
        draw();

        if (location.y >= WINDOW_HEIGHT * 1.5) {
            location = new Point(location.x, background.getY() - WINDOW_HEIGHT);
        }
    }
    /**
     * check if the currentFrame has reach the endFrame of a weather period
     * @return true if the weather period has reached the end.
     */
    public boolean reachEndFrame() {
        return startFrame == endFrame;
    }
    /**
     * @return the y coordinates of the location
     */
    public double getY() {
        return location.y;
    }
    /**
     * set the background image corresponding to current weather condition
     * @param newImage current background image
    */
    public void setImage(Image newImage) {
        this.currentImage = newImage;
    }
    /**
     * set the starting frame of a weather period
     * @param startFrame starting frame of a weather period
     * */
    public void setStartFrame(int startFrame) {
        this.startFrame = startFrame;
    }
    /**
     * set the ending frame of a weather period
     * @param endFrame ending frame of a weather period
     * */
    public void setEndFrame(int endFrame) {
        this.endFrame = endFrame;
    }
    /**
     * Move the GameObject object in the y-direction based on the speedY attribute.
     */
    public void move() {
        location = new Point(location.x, location.y + SPEED_Y * moveY);
    }

    /**
     * Draw the GameObject object into the screen.
     */
    public void draw() {
        currentImage.draw(location.x, location.y);
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
}
