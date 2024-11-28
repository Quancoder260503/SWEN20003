import bagel.*;
import bagel.util.*;
import java.util.Properties;

/*
* Class created to render the trip end flag entity on the screen once the passenger has been picked up
* */

public class TripEndFlag {

    private final double MOVE_DISTANCE = 5.0;
    private final Image TRIP_END_FLAG;
    private final double radius;
    private Point flagLocation;
    private boolean onScreen;
    // Initialization.
    public TripEndFlag(Point flagLocation, Properties gameProps) {
        this.flagLocation = flagLocation;
        this.TRIP_END_FLAG = new Image(gameProps.getProperty("gameObjects.tripEndFlag.image"));
        this.radius = Double.parseDouble(gameProps.getProperty("gameObjects.tripEndFlag.radius"));
        this.onScreen = false;
    }
    // Setting the condition if the flag is allowed to be rendered on the game screen.
    public void setScreen(boolean state) {
        this.onScreen = state;
    }

    public void displayFlag() {
        if(this.onScreen) {
            TRIP_END_FLAG.draw(flagLocation.x, flagLocation.y);
        }
    }

    public Point getFlagLocation() {
        return this.flagLocation;
    }

    public double getFlagRadius() {
        return this.radius;
    }

    // Move the flag along the background image.
    public void move(Input input) {
        double flagY = this.flagLocation.y;
        if(input.wasPressed(Keys.UP) || input.isDown(Keys.UP)) {
            flagY = flagY + MOVE_DISTANCE;
        }
        flagLocation = new Point(this.flagLocation.x, flagY);
    }
}
