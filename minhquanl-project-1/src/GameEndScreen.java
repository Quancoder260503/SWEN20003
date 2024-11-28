import bagel.*;
import bagel.util.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
/* Class created to display the verdict of the current player and the records, information of top 5 players who
* ever played the game while also giving the next instruction.
* */


public class GameEndScreen {
    private final Image BACKGROUND_IMAGE;
    private final Font GAME_END_FONT;
    private final Font GAME_END_TOP;
    private final int GAP = 40;
    private final int GAP_END = 50;
    private final int CUT_OFF = 5;
    private final String endFile;
    private int yTopLocation;
    private DisplayMessage winMessageA;
    private DisplayMessage winMessageB;
    private DisplayMessage loseMessageA;
    private DisplayMessage loseMessageB;
    private DisplayMessage topMessage;
    private DisplayMessage[] displayScore;

    // Initialization and extracting information from the game property file
    void readMessage(Properties gameProps, Properties messageProps) {
        String winMess = messageProps.getProperty("gameEnd.won");
        String loseMess = messageProps.getProperty("gameEnd.lost");
        int yLocation = Integer.parseInt(gameProps.getProperty("gameEnd.status.y"));

        int c = winMess.indexOf('\n');
        winMessageA  = new DisplayMessage(winMess.substring(0, c), GAME_END_FONT, String.valueOf(yLocation), 0.0);
        winMessageB  = new DisplayMessage(winMess.substring(c + 1), GAME_END_FONT,
                                          String.valueOf(yLocation + GAP_END), 0.0);

        c = loseMess.indexOf('\n');
        loseMessageA = new DisplayMessage(loseMess.substring(0, c), GAME_END_FONT, String.valueOf(yLocation), 0.0);
        loseMessageB = new DisplayMessage(loseMess.substring(c + 1), GAME_END_FONT,
                                          String.valueOf(yLocation + GAP_END), 0.0);

        String TopScore = messageProps.getProperty("gameEnd.highestScores");
        yTopLocation = Integer.parseInt(gameProps.getProperty("gameEnd.scores.y"));
        topMessage = new DisplayMessage(TopScore, GAME_END_TOP, String.valueOf(yTopLocation), 0.0);
    }

    public GameEndScreen(Properties gameProps, Properties messageProps) {
        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage.gameEnd"));
        GAME_END_FONT = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gameEnd.status.fontSize")));
        GAME_END_TOP = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gameEnd.scores.fontSize")));
        endFile = gameProps.getProperty("gameEnd.scoresFile");
        readMessage(gameProps, messageProps);
    }
    // Get top 5 best player determined by total score.
    private void getBestFive() {
        String[][] array = IOUtils.readHyphenSeparatedFile(this.endFile);
        PlayerScore[] arrayPlayerScore = new PlayerScore[array.length];
        for(int i = 0; i < array.length; i++) {
            String playerName = array[i][0];
            double score = Double.parseDouble(array[i][1]);
            arrayPlayerScore[i] = new PlayerScore(playerName, score);
        }
        Arrays.sort(arrayPlayerScore);
        displayScore = new DisplayMessage[Math.min(arrayPlayerScore.length, CUT_OFF)];
        for(int i = 0; i < Math.min(arrayPlayerScore.length, CUT_OFF); i++) {
            String getStat = String.format("%s %.2f",
                                           arrayPlayerScore[i].getPlayerName(), arrayPlayerScore[i].getScore());
            displayScore[i] = new DisplayMessage(getStat, GAME_END_TOP,
                                              String.valueOf(yTopLocation + GAP * (i + 1)), 0.0);
        }
    }
    // Adding the record of the current player into scores.csv
    public void addingPlayer(String playerName, double score) {

        String stat = String.format("%s - %.2f", playerName, score);
        IOUtils.writeLineToFile(endFile, stat);
        getBestFive();
    }
    // Display the verdict of the current player and top 5 best players in the game.
    public void displayEnd(boolean playerStatus) {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        topMessage.outputMessage(GAME_END_TOP);
        if(this.displayScore != null) {
            for(int i = 0; i < displayScore.length; i++) {
                displayScore[i].outputMessage(GAME_END_TOP);
            }
        }
        if(playerStatus) {
            winMessageA.outputMessage(GAME_END_FONT);
            winMessageB.outputMessage(GAME_END_FONT);
        }
        else {
            loseMessageA.outputMessage(GAME_END_FONT);
            loseMessageB.outputMessage(GAME_END_FONT);
        }
    }

}
