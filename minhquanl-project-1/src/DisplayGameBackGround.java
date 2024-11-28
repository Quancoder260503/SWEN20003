import bagel.*;
import bagel.util.*;
import java.util.Properties;


/*
* Class DisplayGameBackGround created to display the 'running' background in
* the game.
* */

public class DisplayGameBackGround {
    private final int MOVE_DISTANCE = 5;
    private final int THRESHOLD  = 1152;
    private final Image BACKGROUND_GAME_IMAGE_1;
    private final Image BACKGROUND_GAME_IMAGE_2;
    private Point Location1;
    private Point Location2;

    //Initialization
    public DisplayGameBackGround(Properties gameProps, Properties messageProps) {
        BACKGROUND_GAME_IMAGE_1 = new Image(gameProps.getProperty("backgroundImage"));
        BACKGROUND_GAME_IMAGE_2 = new Image(gameProps.getProperty("backgroundImage"));
        Location1 = new Point(Window.getWidth() / 2.0, -Window.getHeight() / 2.0);
        Location2 = new Point(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }
    // Render background of the game play on the screen
    public void drawBackGround() {
        BACKGROUND_GAME_IMAGE_1.draw(Location1.x, Location1.y);
        BACKGROUND_GAME_IMAGE_2.draw(Location2.x, Location2.y);
    }
    // Move background by adjusting the coordinates in accordance to the rules given.
    public void moveBackGround(Input input) {
        double starting_Y1 = Location1.y;
        double starting_Y2 = Location2.y;
        if(input.isDown(Keys.UP)) {
            starting_Y1 += MOVE_DISTANCE;
            starting_Y2 += MOVE_DISTANCE;
            if(starting_Y1 >= THRESHOLD) {
                starting_Y1 = starting_Y2 - Window.getHeight();
            }
            if(starting_Y2 >= THRESHOLD) {
                starting_Y2 = starting_Y1 - Window.getHeight();
            }
        }
        Location1 = new Point(Location1.x, starting_Y1);
        Location2 = new Point(Location2.x, starting_Y2);
    }
}
