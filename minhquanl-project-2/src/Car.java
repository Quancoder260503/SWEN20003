import bagel.Image;
import bagel.util.*;

import java.util.Properties;

/**
 * This class represents the other Car object in the game.
 * */

public class Car extends Vehicle {
   public Car(Point location, Image IMAGE, int SPEED_Y, Properties props) {
       super(  location,
               IMAGE,
               Integer.parseInt(props.getProperty("gameObjects.otherCar.speedX")),
               SPEED_Y,
               Double.parseDouble(props.getProperty("gameObjects.otherCar.radius")),
               Double.parseDouble(props.getProperty("gameObjects.otherCar.damage")) * 100.0,
               Double.parseDouble(props.getProperty("gameObjects.otherCar.health")) * 100.0,
               props);
   }
}
