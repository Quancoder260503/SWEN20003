import bagel.*;

import java.util.Properties;

public class ShadowTaxi extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;

    private final HomeScreen homeScreen;
    private GamePlayScreen gamePlayScreen;
    private PlayerInfoScreen playerInfoScreen;
    private GameEndScreen gameEndScreen;

    public ShadowTaxi(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));

        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;

        homeScreen = new HomeScreen(GAME_PROPS, MESSAGE_PROPS);
    }

    /**
     * Render the relevant screen based on the keyboard input given by the user and the status of the game play.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // render the home screen
        if (gamePlayScreen == null && playerInfoScreen == null && gameEndScreen == null) {
            // if the user click ENTER button when in the Home Screen, generate the player info screen
            if(homeScreen.update(input)) {
                playerInfoScreen = new PlayerInfoScreen(GAME_PROPS, MESSAGE_PROPS);
            }
        } else if(playerInfoScreen != null && gamePlayScreen == null && gameEndScreen == null) {
            // if the user selects to start the game, generate a new game play screen
            if(playerInfoScreen.update(input)) {
                gamePlayScreen = new GamePlayScreen(GAME_PROPS, MESSAGE_PROPS, playerInfoScreen.getPlayerName());
                playerInfoScreen = null;
            }
        } else if (playerInfoScreen == null && gamePlayScreen != null && gameEndScreen == null){
            // if the game is over or the level is completed, generate new game end screen
            if(gamePlayScreen.update(input)) {
                boolean isWon = gamePlayScreen.isLevelCompleted();

                gameEndScreen = new GameEndScreen(GAME_PROPS, MESSAGE_PROPS);
                gameEndScreen.setIsWon(isWon);

                gamePlayScreen = null;
            }
        } else if(playerInfoScreen == null && gamePlayScreen == null && gameEndScreen != null) {
            if(gameEndScreen.update(input)) {
                gamePlayScreen = null;
                playerInfoScreen = null;
                gameEndScreen = null;
            }
        }
    }

    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowTaxi game = new ShadowTaxi(game_props, message_props);
        game.run();
    }
}
