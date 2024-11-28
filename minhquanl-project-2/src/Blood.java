import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * This class representing the implementation for the Blood effect in the game.
 * */

public class Blood extends GameSpecialEntity {
    public Blood(Point location, Properties props) {
        super(location,
                Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                new Image(props.getProperty("gameObjects.blood.image")),
                Integer.parseInt(props.getProperty("gameObjects.blood.ttl")));
    }
}


