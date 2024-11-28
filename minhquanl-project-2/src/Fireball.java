import bagel.Input;
import bagel.util.Point;
import bagel.Image;

import java.util.Properties;

/**
 * This class represents a fireball and its functionalities in the game.
 * */

public class Fireball extends GameSmallEntity implements Attackable {
    private final double DAMAGE_VALUE;
    private final double RADIUS;
    private boolean hitTarget;
    public Fireball(Point location, Properties props) {
        super(location,
                Integer.parseInt(props.getProperty("gameObjects.fireball.shootSpeedY")),
                new Image(props.getProperty("gameObjects.fireball.image")));
        this.RADIUS = Double.parseDouble(props.getProperty("gameObjects.fireball.radius"));
        this.DAMAGE_VALUE = Double.parseDouble(props.getProperty("gameObjects.fireball.damage")) * 100.0;
        this.hitTarget = false;
    }
    /***
     * @return true if the fireball has exceeded the top of the game play screen.
     */
    public boolean reachTerminatedState() {
        return getY() < 0;
    }
    /**
     * Implementing a move by adjusting the current location.
     * */
    @Override
    public void move() {
        super.setLocation(new Point(getX(), getY() - getMovingSpeedY()));
    }
    /**
     * Updating the current state of the fireball.
     * */
    @Override
    public void update(Input input) {
        if(!reachTerminatedState()) {
            move();
            draw();
        }
    }
    /**
     * @ return the radius of the fireball.
     * */
    public double getRadius() {
        return RADIUS;
    }
    /**
     * @return the damage value of the fireball.
     * */
    public double getDamageValue(){
        return DAMAGE_VALUE;
    }
    /**
     * @return true if the fireball has collided with other Game Main Entity object.
     * */
    private boolean hasCollidedWith(GameMainEntity other) {
        double radiusDistance = this.getRadius() + other.getRadius();
        double currentDistance = this.getLocation().distanceTo(other.getLocation());
        return currentDistance <= radiusDistance;
    }
    /**
     * @return true if the fireball has hit a specific Game Main Entity object.
     * */
    public boolean isHitTarget() {
        return hitTarget;
    }
    /**
     * Inflicting damage on other objects.
     * */
    public void inflictingDamage(GameMainEntity other) {
      if(hasCollidedWith(other)) {
          // check if the object can take the damage (i.e. not in collision timeout)
          if(!other.isInCollision()) {
              if(other instanceof EffectApplicable && ((EffectApplicable) other).isInvisible()) {
                  // Can not inflict damage since the object is invisible
              }
              else {
                  other.receiveDamage(getDamageValue());
                  other.setInCollision();
                  other.setDamageTakenTime(0);
                  other.setCollisionTime(0);
              }
              this.hitTarget = true;
          }
      }
    }
}
