import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * This class representing the implementation for the Fire effect in the game.
 * */

public class Fire extends GameSpecialEntity {
    public Fire(Point location, Properties props) {
        super(location,
                Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                new Image(props.getProperty("gameObjects.fire.image")),
                Integer.parseInt(props.getProperty("gameObjects.fire.ttl")));
    }
}
