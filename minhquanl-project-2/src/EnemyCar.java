import bagel.Image;
import bagel.util.Point;
import bagel.Input;

import java.util.ArrayList;
import java.util.Properties;

/**
 * This class represents the Enemy Car in the game.
 */

public class EnemyCar extends Vehicle {
    private final ArrayList<Fireball> fireBalls;
    private final int INV_RATE_FIREBALL;
    private final Properties GAME_PROPS;
    public EnemyCar(Point location, Image IMAGE, int SPEED_Y, Properties props) {
        super(  location,
                IMAGE,
                Integer.parseInt(props.getProperty("gameObjects.enemyCar.speedX")),
                SPEED_Y,
                Double.parseDouble(props.getProperty("gameObjects.enemyCar.radius")),
                Double.parseDouble(props.getProperty("gameObjects.enemyCar.damage")) * 100.0,
                Double.parseDouble(props.getProperty("gameObjects.enemyCar.health")) * 100.0,
                props);
        fireBalls = new ArrayList<>();
        INV_RATE_FIREBALL = 300;
        GAME_PROPS = props;
    }

    /**
     * Generating the fireball.
     */
    public void generateFireball() {
        if(MiscUtils.canSpawn(INV_RATE_FIREBALL)) {
            fireBalls.add(new Fireball(super.getLocation(), GAME_PROPS));
        }
    }
    /**
     * Update the details of the enemy car in accordance to the change in user's input
     * @param input represents the input of the users.
     */
    @Override
    public void update(Input input) {
        super.update(input);
        // Removing any fireball that is unusable.
        ArrayList<Fireball> terminatedFireball = new ArrayList<>();
        for(Fireball fireBall : fireBalls) {
            if(fireBall.reachTerminatedState() || fireBall.isHitTarget()) {
                terminatedFireball.add(fireBall);
            }
        }
        for(Fireball fireball : terminatedFireball) {
            fireBalls.remove(fireball);
        }
        for(Fireball fireball : fireBalls) {
            fireball.update(input);
        }
    }

    /**
     * Firing a fireball at other main entity object in the game
     * @param other represents another game main entity object.
     */
    public void getFireballHit(GameMainEntity other) {
        for(Fireball fireBall : fireBalls) {
            if(fireBall.reachTerminatedState() || fireBall.isHitTarget()) {
                continue;
            }
            fireBall.inflictingDamage(other);
        }
    }
}
