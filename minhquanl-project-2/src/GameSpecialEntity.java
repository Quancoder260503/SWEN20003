import bagel.Input;
import bagel.util.Point;
import bagel.Image;

/**
 * This class represents the special effect entity in the game when a Game Main Entity object is eliminated or
 * is collided (i.e. Blood, Fire, Smoke).
 */

public class GameSpecialEntity extends GameSmallEntity {
    private int framesActive = 0;
    private final int MAX_FRAME;
    public GameSpecialEntity(Point location, int movingSpeedY, Image IMAGE, int MAX_FRAME) {
        super(location, movingSpeedY, IMAGE);
        this.MAX_FRAME = MAX_FRAME;
    }
    /**
     * Updating the location and drawing object in accordance to the update of the player with a slight change as
     * the framesActive increased through updates.
     * @param input the input of the user.
     * */
    @Override
    public void update(Input input) {
        framesActive++;
        if (input != null) {
            adjustToInputMovement(input);
        }
        move();
        draw();
    }
    /**
     * @return true if the object has reached its maximum active frame.
     * */
    public boolean reachTerminatedState() {
        return framesActive >= MAX_FRAME;
    }
}
