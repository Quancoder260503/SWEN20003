import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.*;

/**
 * Class representing invisible star in the game. Stars can be collected by either the driver or the taxi.
 * If a star power is initiated, a driver or the taxi will not take any damage from a collision.
*/

import java.util.Properties;

public class InvisibleStar extends GameEffectEntity {
    public InvisibleStar(Point location, Properties props) {
        super(location, Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                        Integer.parseInt(props.getProperty("gameObjects.invinciblePower.maxFrames")),
                        new Image(props.getProperty("gameObjects.invinciblePower.image")),
                        Double.parseDouble(props.getProperty("gameObjects.invinciblePower.radius")));
    }
    /**
     * Setting up a collision with the driver or the taxi if the star has not exceeded its use time
     * @Param taxiDriver the taxi driver of the game.
     * */
    public void collide(Driver taxiDriver) {
        if (taxiDriver.getTaxi() != null && super.hasCollidedWith(taxiDriver.getTaxi())) {
            taxiDriver.collectStar(this);
            setIsCollided();
        }
        else if(super.hasColliedWithDriver(taxiDriver)) {
            taxiDriver.collectStar(this);
            setIsCollided();
        }
    }
}
