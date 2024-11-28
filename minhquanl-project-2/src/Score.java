/**
 * Score class that stores the player's name and score.
 */
public class Score {
    private final String PLAYER_NAME;
    private final double SCORE;

    public Score(String playerName, double score) {
        this.PLAYER_NAME = playerName;
        this.SCORE = score;
    }
    /**
     * @return the name of the player
     * */
    public String getPlayerName() {
        return PLAYER_NAME;
    }
    /**
     * @return the score of the player
     * */
    public double getScore() {
        return SCORE;
    }
}
