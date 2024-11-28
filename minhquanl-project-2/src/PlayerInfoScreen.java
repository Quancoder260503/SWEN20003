import bagel.*;

import java.util.Properties;

/**
 * The screen class which allows the player to enter their name.
 */
public class PlayerInfoScreen{

    private final Image BACKGROUND_IMAGE;
    private final String PI_PLAYER_NAME;
    private final String PI_START_GAME;
    private final Font PI_FONT;
    private final int PLAYER_NAME_Y;
    private final int PLAYER_NAME_INPUT_Y;
    private final int START_GAME_Y;
    private String playerName;
    private final DrawOptions FONT_STYLE;

    public PlayerInfoScreen(Properties gameProps, Properties msgProps) {

        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage.playerInfo"));

        PI_PLAYER_NAME = msgProps.getProperty("playerInfo.playerName");
        PI_START_GAME = msgProps.getProperty("playerInfo.start");
        PI_FONT = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("playerInfo.fontSize")));
        PLAYER_NAME_Y = Integer.parseInt(gameProps.getProperty("playerInfo.playerName.y"));
        PLAYER_NAME_INPUT_Y = Integer.parseInt(gameProps.getProperty("playerInfo.playerNameInput.y"));
        START_GAME_Y = Integer.parseInt(gameProps.getProperty("playerInfo.start.y"));

        FONT_STYLE = new DrawOptions();
        FONT_STYLE.setBlendColour(0.0, 0.0, 0.0);

        playerName = "";
    }

    /**
     * Show the player info screen with the input entered for the player name and the start game message.
     * @param input The current mouse/keyboard input.
     * @return true if ENTER key is pressed, false otherwise.
     */
    public boolean update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        PI_FONT.drawString(PI_PLAYER_NAME,
                Window.getWidth() / 2 - PI_FONT.getWidth(PI_PLAYER_NAME)/2, PLAYER_NAME_Y);

        PI_FONT.drawString(PI_START_GAME,
                Window.getWidth() / 2 - PI_FONT.getWidth(PI_START_GAME)/2, START_GAME_Y);

        String letter = MiscUtils.getKeyPress(input);
        if(letter != null && !(input.wasPressed(Keys.BACKSPACE) || input.wasPressed(Keys.DELETE))) {
            playerName += letter;
        } else if((input.wasPressed(Keys.BACKSPACE) || input.wasPressed(Keys.DELETE)) && !playerName.isEmpty()) {
            playerName = playerName.substring(0, playerName.length()-1);
        }

        PI_FONT.drawString(PI_PLAYER_NAME,
                Window.getWidth() / 2 - PI_FONT.getWidth(PI_PLAYER_NAME)/2, PLAYER_NAME_Y);

        PI_FONT.drawString(playerName,
                Window.getWidth() / 2 - PI_FONT.getWidth(playerName)/2, PLAYER_NAME_INPUT_Y, FONT_STYLE);

        return input.wasPressed(Keys.ENTER);
    }
    /**
     * @return the name of the player
     * */
    public String getPlayerName() {
        return playerName;
    }

}