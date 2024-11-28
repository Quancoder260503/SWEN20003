import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.*;

import java.util.Properties;

/**
 * The class representing the taxis in the game play
 */
public class Taxi extends Vehicle implements EffectApplicable {
    private final Image DAMAGE_IMAGE;
    private boolean isMovingY;
    private boolean isMovingX;
    private final Properties GAME_PROPS;
    private Coin coinPower;
    private InvisibleStar starPower;
    public Taxi(Point location, Properties props) {
        super(location,
                new Image(props.getProperty("gameObjects.taxi.image")),
                Integer.parseInt(props.getProperty("gameObjects.taxi.speedX")),
                Integer.parseInt(props.getProperty("gameObjects.taxi.speedY")),
                Double.parseDouble(props.getProperty("gameObjects.taxi.radius")),
                Double.parseDouble(props.getProperty("gameObjects.taxi.damage")) * 100.0,
                Double.parseDouble(props.getProperty("gameObjects.taxi.health")) * 100.0,
                props);
        this.GAME_PROPS = props;
        this.DAMAGE_IMAGE = new Image(props.getProperty("gameObjects.taxi.damagedImage"));
    }
    /**
     * @return true if the object is invisible against the collision.
     */
    public boolean isInvisible() {
        return (starPower != null && starPower.getIsActive());
    }
    /**
     * collecting the coin power
     * @param coin represents the power coin.
     */
    public void collectPower(Coin coin) {
        coinPower = coin;
    }
    /**
     * collecting the invisible star
     * @param starPower represents the invisible star.
     */
    public void collectStar(InvisibleStar starPower) {
        this.starPower = starPower;
    }
    /**
     * @return true if the object is moving in y-axis
     */
    public boolean isMovingY() {
        return isMovingY;
    }
    /**
     * @return true if the object is moving in x-axis
     */
    public boolean isMovingX() {
        return isMovingX;
    }
    /**
     * Check the condition of the taxi and draw the proper image.
     */
    @Override
    public void draw() {
        if(!super.getIsDamage()) {
            super.draw();
        }
        else {
            DAMAGE_IMAGE.draw(getX(), getY());
        }
    }
    /**
     * updating the taxi condition in relation to the user input with restriction.
     * @param input represents the user's input
     * @param canMove represents if the taxi is allowed to move
     */
    public void update(Input input, boolean canMove) {
        if(super.setDamage(input)) {
            return;
        }
        super.isEndCollisionTimeout();
        draw();
        if(super.isInCollision()) {
            if(super.getSmokeEffect() == null) {
                super.setSmokeEffect(new Smoke(super.getLocation(), GAME_PROPS));
            }
            super.getSmokeEffect().update(input);
            if(!super.getSmokeEffect().reachTerminatedState()) {
                super.getSmokeEffect().update(input);
            }
            super.updateCollisionTime();
            if(super.hasNotReachBumpingEnd()) {
                super.moveCollision();
            }
            else {
                if(input != null) {
                    if(canMove) {
                        adjustToInputMovement(input);
                    }
                }
            }
        }
        else {
             super.setSmokeEffect(null);
             if(input != null) {
                if(canMove) {
                    adjustToInputMovement(input);
                }
             }
        }
    }

    /**
     * Moving the taxi along the background image of the game (in case it is damaged or the driver has not reached
     * the taxi)
     * @param input represents the user input.
     */
    public void moveWithBackground(Input input) {
        int moveY = 0;
        if (input.isDown(Keys.UP)) {
            moveY = 1;
        }
        super.setLocation(new Point(getX(), getY() + moveY * getSpeedY()));
    }

    /**
     * Adjusting the location of taxi in accordance to the change in user's input.
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void adjustToInputMovement(Input input) {
        if(super.getIsDamage()) {
            moveWithBackground(input);
        }
        else {
            double x = getX();
            if (input.isDown(Keys.UP)) {
                isMovingY = true;
            }  else if(input.wasReleased(Keys.UP)) {
                isMovingY = false;
            } else if(input.isDown(Keys.LEFT)) {
                x -= super.getSpeedX();
                isMovingX = true;
            }  else if(input.isDown(Keys.RIGHT)) {
                x += super.getSpeedX();
                isMovingX =  true;
            } else if(input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
                isMovingX = false;
            }
            super.setLocation(new Point(x, getY()));
        }
    }
}
