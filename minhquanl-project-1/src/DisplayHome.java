import bagel.*;
import bagel.util.*;
import java.util.Properties;

/*
* Class created to display home background and information
* */

public class DisplayHome {
    private final Font SHADOW_TAXI_FONT;
    private final Font PRESS_ENTER;
    private final DisplayMessage shadow;
    private final DisplayMessage press_enter;
    private final Image BACKGROUND_IMAGE;
    // Initialization
    public DisplayHome(Properties gameProps, Properties messageProps) {
        BACKGROUND_IMAGE  = new Image(gameProps.getProperty("backgroundImage.home"));
        SHADOW_TAXI_FONT = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("home.title.fontSize")));
        PRESS_ENTER      = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("home.instruction.fontSize")));
        String title            = messageProps.getProperty("home.title");
        String enter            = messageProps.getProperty("home.instruction");
        shadow      = new DisplayMessage(title, SHADOW_TAXI_FONT, gameProps.getProperty("home.title.y"), 0.0);
        press_enter = new DisplayMessage(enter, PRESS_ENTER, gameProps.getProperty("home.instruction.y"), 0.0);
    }
    // Display necessary information on screen
    public void display() {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0 , Window.getHeight() / 2.0);
        shadow.outputMessage(SHADOW_TAXI_FONT);
        press_enter.outputMessage(PRESS_ENTER);
    }
}
