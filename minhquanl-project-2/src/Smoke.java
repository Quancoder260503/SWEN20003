import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * This class representing the implementation for the Smoke effect in the game.
 * */

public class Smoke extends GameSpecialEntity {
    public Smoke(Point location, Properties props) {
        super(location,
                Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                new Image(props.getProperty("gameObjects.smoke.image")),
                Integer.parseInt(props.getProperty("gameObjects.smoke.ttl")));
    }
}
