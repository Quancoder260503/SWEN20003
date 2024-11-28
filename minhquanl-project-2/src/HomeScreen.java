import bagel.*;

import java.util.Properties;

/**
 * A class representing the home screen of the game.
 * */
public class HomeScreen {

    private final Image BACKGROUND_IMAGE;
    private final String TITLE;
    private final String INSTRUCTION;
    private final Font TITLE_FONT;
    private final Font INSTRUCTION_FONT;
    private final int TITLE_Y;
    private final int INSTRUCTION_Y;

    public HomeScreen(Properties gameProps, Properties msgProps) {
        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage.home"));

        TITLE = msgProps.getProperty("home.title");
        TITLE_FONT = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("home.title.fontSize")));
        TITLE_Y = Integer.parseInt(gameProps.getProperty("home.title.y"));

        INSTRUCTION = msgProps.getProperty("home.instruction");
        INSTRUCTION_FONT = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("home.instruction.fontSize")));
        INSTRUCTION_Y = Integer.parseInt(gameProps.getProperty("home.instruction.y"));
    }

    /**
     * Show the home screen with the title and the background.
     * @param input The current mouse/keyboard input.
     * @return true if ENTER key is pressed, false otherwise.
     */
    public boolean update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        TITLE_FONT.drawString(TITLE, Window.getWidth() / 2 - TITLE_FONT.getWidth(TITLE)/2, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION, Window.getWidth() / 2 - INSTRUCTION_FONT.getWidth(TITLE)/2,
                INSTRUCTION_Y);

        return input.wasPressed(Keys.ENTER);
    }
}
