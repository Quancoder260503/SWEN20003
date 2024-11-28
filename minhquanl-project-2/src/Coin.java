import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.*;

import java.util.Properties;

/**
 * Class representing coins in the game. Coins can be collected by either the driver or the taxi.
 * It will set one level higher priority for the passengers that are waiting to get-in or already in the taxi.
 */
public class Coin extends GameEffectEntity {
    public Coin(Point location, Properties props) {
        super(location, Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                        Integer.parseInt(props.getProperty("gameObjects.coin.maxFrames")),
                        new Image(props.getProperty("gameObjects.coin.image")),
                        Double.parseDouble(props.getProperty("gameObjects.coin.radius")));
    }
    /**
     * Setting up a power for taxi driver or taxi if two objects collide
     * @param  taxiDriver the taxi driver of the game.
    */
    public void collide(Driver taxiDriver) {
        if (taxiDriver.getTaxi() != null && super.hasCollidedWith(taxiDriver.getTaxi())) {
            taxiDriver.collectPower(this);
            setIsCollided();
        }
        else if(super.hasColliedWithDriver(taxiDriver)) {
            taxiDriver.collectPower(this);
            setIsCollided();
        }
    }
    /**
     * applying the power of the coin to the specific objects (i.e. reducing the priority of a passenger)
     * @param priority the current priority of a passenger
     * @return the new priority if the coin power is applicable
    */
    public Integer applyEffect(Integer priority) {
        if (super.getFramesActive() <= super.getMaxFrames() && priority > 1) {
            priority -= 1;
        }
        return priority;
    }
}
