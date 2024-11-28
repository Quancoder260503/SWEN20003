import bagel.*;
import bagel.util.*;
import java.util.Properties;

/*
* Class coin with method to render Coin entity on the screen
* */

public class Coin {
    private final double MOVE_DISTANCE = 5.0;
    private final Image COIN;
    private final double radius;
    private Point coinLocation;
    private boolean onScreen;

    // Initialization
    public Coin(Point coinLocation,  Properties gameProps) {
        this.coinLocation = coinLocation;
        this.COIN = new Image((gameProps.getProperty("gameObjects.coin.image")));
        this.radius = Double.parseDouble(gameProps.getProperty("gameObjects.coin.radius"));
        this.onScreen = true;
    }
    // Only draw coin if it is not used by the Taxi
    public void drawCoin() {
        if(onScreen) {
            COIN.draw(coinLocation.x, coinLocation.y);
        }
    }
    // Move coin along the game image background
    public void move(Input input) {
        double flagY = this.coinLocation.y;
        if(input.wasPressed(Keys.UP) || input.isDown(Keys.UP)) {
            flagY = flagY + MOVE_DISTANCE;
        }
        this.coinLocation= new Point(this.coinLocation.x, flagY);
    }
    // Check if the coin has collied with the taxi
    public boolean collide(Taxi taxi) {
        return this.coinLocation.distanceTo(taxi.getTaxiLocation()) < radius + taxi.getRadius();
    }

    public void setOnScreen(boolean state) {
        onScreen = state;
    }

}
