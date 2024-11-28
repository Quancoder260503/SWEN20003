/* A class created to store the scores and information of the players extracted from CSV file
* */


public class PlayerScore implements Comparable<PlayerScore> {
    private final double score;
    private final String playerName;
    public  PlayerScore(String playerName, double score) {
        this.score = score;
        this.playerName = playerName;
    }
    public double getScore() {
        return score;
    }
    public String getPlayerName() {
        return playerName;
    }
    @Override
    public int compareTo(PlayerScore o) {
        return -Double.compare(this.score, o.score);
    }
}
