import bagel.*;
import bagel.util.*;
import java.util.Properties;

/* Class created to display, update information of the current player on the screen and
* giving the instruction messages.
* */


public class DisplayPlayerInfo {

    private final Image BACKGROUND_PLAYER_INFO;
    private final Font PLAYER_INFO_FONT;
    private DisplayMessage displayEnterName;
    private DisplayMessage startMessage;
    private DisplayMessage instruction;
    private DisplayMessage playerNameInput;
    private final DrawOptions drawColour;
    private final String inputNameY;
    // Getting the message from the game's property file.
    private void getMessage(Properties gameProps, Properties messageProps) {
        String enterName = messageProps.getProperty("playerInfo.playerName");
        String startMess = messageProps.getProperty("playerInfo.start");
        String instructionMess = messageProps.getProperty("playerInfo.instruction");
        playerNameInput = new DisplayMessage("", PLAYER_INFO_FONT, inputNameY, 0.0);
        displayEnterName = new DisplayMessage(enterName, PLAYER_INFO_FONT,
                                              gameProps.getProperty("playerInfo.playerName.y"), 0.0);
        startMessage = new DisplayMessage(startMess, PLAYER_INFO_FONT,
                                          gameProps.getProperty("playerInfo.start.y"), 0.0);
        instruction = new DisplayMessage(instructionMess, PLAYER_INFO_FONT,
                                         gameProps.getProperty("playerInfo.start.y"), 50.0);
    }
    // Initialization
    public DisplayPlayerInfo(Properties gameProps, Properties messageProps) {
        BACKGROUND_PLAYER_INFO = new Image(gameProps.getProperty("backgroundImage.playerInfo"));
        PLAYER_INFO_FONT = new Font(gameProps.getProperty("font"),
                                    Integer.parseInt(gameProps.getProperty("playerInfo.fontSize")));
        inputNameY = gameProps.getProperty("playerInfo.playerNameInput.y");
        getMessage(gameProps, messageProps);
        drawColour = new DrawOptions();
        drawColour.setBlendColour(0.0, 0.0, 0.0);
    }
    // Methods used to display the Player's information filled up and instruction stage of the game.
    void getName(String name) {
      playerNameInput = new DisplayMessage(name, PLAYER_INFO_FONT, inputNameY, 0.0);
    }
    public void displayInfoScreen() {
        BACKGROUND_PLAYER_INFO.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        displayEnterName.outputMessage(PLAYER_INFO_FONT);
    }
    public void displayPlayerName() {
        playerNameInput.outputMessageColour(PLAYER_INFO_FONT, drawColour);
    }

    public void displayInstruction() {
        startMessage.outputMessage(PLAYER_INFO_FONT);
        instruction.outputMessage(PLAYER_INFO_FONT);
    }
}
