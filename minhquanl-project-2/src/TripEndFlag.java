import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.*;

import java.util.Properties;

/**
 * A class representing the trip end flag in the game play.
 * Objects of this class will only move up and down based on the keyboard input. No other functionalities needed.
 */
public class TripEndFlag extends GameSmallEntity {
    private final double RADIUS;

    public TripEndFlag(Point location, Properties props) {
        super(location, Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                        new Image(props.getProperty("gameObjects.tripEndFlag.image")));
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.tripEndFlag.radius"));
    }
    /**
     * @return the radius of the trip end flag
     * */
    public double getRadius() {
        return RADIUS;
    }
}
