import bagel.*;
import bagel.util.*;
import java.util.Properties;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2024
 * Author : MINH QUAN LE
 * STUDENT ID : 1450342
 */

/*
* The game were break into different stage.
* At stage 0, the home screen will be displayed
* At stage 1, the player information screen will be displayed for player's to fill up personal details and gives
* instruction to them.
* At stage 2, the player will play games.
* At stage 3, the game is finished with verdict of player being displayed (Win / Lose) and the top 5 best scorers of
* the game. At this stage, the game will be upon the player to continue (back to stage 0) or finish (press escaped).
* */

public class ShadowTaxi extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private String currentPlayerName;
    private boolean playerState;
    private int Stage;
    private final DisplayHome displayHome;
    private final DisplayPlayerInfo displayPlayerInfo;
    private DisplayGameScreen displayGameScreen;
    private final GameEndScreen displayEndScreen;

    public ShadowTaxi(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS    = gameProps;
        this.MESSAGE_PROPS = messageProps;
        displayHome       = new DisplayHome(gameProps, messageProps);
        displayPlayerInfo = new DisplayPlayerInfo(gameProps, messageProps);
        displayGameScreen = new DisplayGameScreen(gameProps, messageProps);
        displayEndScreen  = new GameEndScreen(gameProps, messageProps);
        currentPlayerName = "";
    }

    /**
     * Render the relevant screens and game objects based on the keyboard input
     * given by the user and the status of the game play.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        if (this.Stage == 0) {
            this.getDisplayHome(input);
        }
        else if (this.Stage == 1) {
            this.displayPlayerInfo(input);
        }
        else if (this.Stage == 2) {
            this.executeGame(input);
        }
        else {
            this.executeEnd(input);
        }
    }

    private void executeEnd(Input input) {
        this.displayEndScreen.displayEnd(this.playerState);
        if(input.wasPressed(Keys.SPACE)) {
            this.Stage = 0;
        }
    }

    private void executeGame(Input input) {
        this.displayGameScreen.displayGame(input);
        if(this.displayGameScreen.getFrame() == 0 || this.displayGameScreen.playerState()) {
            this.Stage = 3;
            this.playerState = this.displayGameScreen.playerState();
            this.displayEndScreen.addingPlayer(currentPlayerName, this.displayGameScreen.getPlayerScore());
            this.displayGameScreen = new DisplayGameScreen(this.GAME_PROPS, this.MESSAGE_PROPS);
            this.currentPlayerName = "";
        }
    }

    private void displayPlayerInfo(Input input) {
        displayPlayerInfo.displayInfoScreen();
        this.manipulateName(input);
        displayPlayerInfo.getName(this.currentPlayerName);
        displayPlayerInfo.displayPlayerName();
        displayPlayerInfo.displayInstruction();
    }

    private void getDisplayHome(Input input) {
        this.displayHome.display();
        if(input.wasPressed(Keys.ENTER)) {
            this.Stage = 1;
        }
    }

    private void manipulateName(Input input) {
        if(input.wasPressed(Keys.BACKSPACE) || input.wasPressed(Keys.DELETE)) {
            if(!this.currentPlayerName.isEmpty()) {
                this.currentPlayerName = this.currentPlayerName.substring(0, this.currentPlayerName.length() - 1);
            }
        }
        else if(MiscUtils.getKeyPress(input) != null) {
            this.currentPlayerName = this.currentPlayerName + MiscUtils.getKeyPress(input);
        }
        if(input.wasPressed(Keys.ENTER)) {
            this.Stage = 2;
        }
    }


    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowTaxi game = new ShadowTaxi(game_props, message_props);
        game.run();
    }
}
