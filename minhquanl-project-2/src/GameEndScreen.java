import bagel.*;

import java.util.*;

/**
 * A class that represents the game end screen for a win/loss.
 */
public class GameEndScreen {

    private final Image BACKGROUND_IMAGE;

    private final String GAME_WON_TXT;
    private final String GAME_LOST_TXT;
    private final String HIGHEST_SCORE_TXT;

    private final Font STATUS_FONT;
    private final Font SCORES_FONT;

    private final int STATUS_Y;
    private final int SCORES_Y;

    private final Score[] TOP_SCORES;
    private Score[] scores;

    private boolean isWon;


    public GameEndScreen(Properties gameProps, Properties msgProps) {

        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage.gameEnd"));

        GAME_WON_TXT = msgProps.getProperty("gameEnd.won");
        GAME_LOST_TXT = msgProps.getProperty("gameEnd.lost");
        HIGHEST_SCORE_TXT = msgProps.getProperty("gameEnd.highestScores");

        STATUS_Y = Integer.parseInt(gameProps.getProperty("gameEnd.status.y"));
        SCORES_Y = Integer.parseInt(gameProps.getProperty("gameEnd.scores.y"));

        String fontFile = gameProps.getProperty("font");
        STATUS_FONT = new Font(fontFile, Integer.parseInt(gameProps.getProperty("gameEnd.status.fontSize")));
        SCORES_FONT = new Font(fontFile, Integer.parseInt(gameProps.getProperty("gameEnd.scores.fontSize")));

        //get top 5 scores
        populateScores(gameProps.getProperty("gameEnd.scoresFile"));
        TOP_SCORES = getTopScores();
    }
    /**
     * Set the state of the game (i.e. whether the player is win)
     * @param isWon represent the state of the game
     */
    public void setIsWon(boolean isWon) {
        this.isWon = isWon;
    }

    /**
     * Show whether the game is won or lost and the top 5 scores.
     * @param input The current mouse/keyboard input.
     * @return true if SPACE key is pressed, false otherwise.
     */
    public boolean update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if(isWon) {
            STATUS_FONT.drawString(GAME_WON_TXT,
                    Window.getWidth() / 2 - STATUS_FONT.getWidth(GAME_WON_TXT)/2, STATUS_Y);
        } else {
            STATUS_FONT.drawString(GAME_LOST_TXT,
                    Window.getWidth() / 2 - STATUS_FONT.getWidth(GAME_LOST_TXT)/2, STATUS_Y);
        }

        if (input.wasPressed(Keys.SPACE)) {
            return true;
        }

        SCORES_FONT.drawString(HIGHEST_SCORE_TXT, Window.getWidth() / 2 - SCORES_FONT.getWidth(HIGHEST_SCORE_TXT)/2,
                SCORES_Y);

        int scoreIdx = 0;
        for(Score score : TOP_SCORES) {
            if(score != null) {
                String text = score.getPlayerName() + " - " + String.format("%.02f", score.getScore());
                double x = Window.getWidth() / 2 - SCORES_FONT.getWidth(text)/2;
                double y = SCORES_Y + 40 * (scoreIdx + 1);
                SCORES_FONT.drawString(text, x, y);
            }

            scoreIdx++;
        }

        return false;

    }

    /**
     * Populate the scores from a file. The file used to store the scores of previous game plays.
     * @param filename The name of the file to read the scores from.
     */
    public void populateScores(String filename) {
        ArrayList<String[]> data = IOUtils.readCommaSeperatedFile(filename);
        scores = new Score[data.size()];

        int scoreIdx = 0;
        for (String[] line : data) {
            String username = line[0];
            double score = Double.parseDouble(line[1]);
            scores[scoreIdx] = new Score(username, score);
            scoreIdx++;
        }
    }

    /**
     * Sort the score in descending order and return the top 5 scores.
     * @return list of Score objects representing the top 5 scores.
     */
    public Score[] getTopScores() {
        Arrays.sort(scores, (a, b) -> Double.compare(b.getScore(), a.getScore()));
        return Arrays.copyOfRange(scores, 0, 5);
    }
}
