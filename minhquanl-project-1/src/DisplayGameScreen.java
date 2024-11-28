import bagel.*;
import bagel.util.*;
import java.util.Properties;

public class DisplayGameScreen {
    private final DisplayGameBackGround displayGameBackGround;
    private final DisplayGameMessage displayGameMessage;
    private final double targetScore;
    private int maxFrame;
    private Taxi taxiDriver;
    private Passenger[] passengers;
    private Coin[] coins;
    // Initialization
    public DisplayGameScreen(Properties gameProps, Properties messageProps) {
        displayGameBackGround = new DisplayGameBackGround(gameProps, messageProps);
        displayGameMessage    = new DisplayGameMessage(gameProps, messageProps);
        readObject(gameProps, messageProps);
        targetScore = Double.parseDouble(gameProps.getProperty("gamePlay.target"));
        maxFrame    = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));
    }
    // Reading, parsing value to array of entities Coin, Passenger and getting the location of the taxi from CSV file.
    private void readObject(Properties gameProps, Properties messageProps) {
        String[][] array = IOUtils.readCommaSeparatedFile(gameProps.getProperty("gamePlay.objectsFile"));
        int countPassenger = 0;
        int countCoin = 0;
        for (String[] strings : array) {
            String gameEntity = strings[0];
            if (gameEntity.equals("PASSENGER")) {
                countPassenger++;
            } else if (gameEntity.equals("COIN")) {
                countCoin++;
            }
        }
        passengers = new Passenger[countPassenger];
        coins = new Coin[countCoin];
        for (String[] strings : array) {
            String gameEntity = strings[0];
            double xLocation = Double.parseDouble(strings[1]);
            double yLocation = Double.parseDouble(strings[2]);
            if (gameEntity.equals("TAXI")) {
                taxiDriver = new Taxi(gameProps, messageProps, new Point(xLocation, yLocation),
                        countPassenger, countCoin);
                countPassenger = 0;
                countCoin = 0;
            } else if (gameEntity.equals("PASSENGER")) {
                int priority = Integer.parseInt(strings[3]);
                double xLocation1 = Double.parseDouble(strings[4]);
                double yLocation1 = yLocation - Double.parseDouble(strings[5]);
                passengers[countPassenger] = new Passenger(new Point(xLocation, yLocation),
                        new Point(xLocation1, yLocation1), priority, gameProps, messageProps);
                countPassenger++;
            } else {
                coins[countCoin] = new Coin(new Point(xLocation, yLocation), gameProps);
                countCoin++;
            }
        }
    }

    // Display passenger on the screen, canMove represent if the Passenger's image
    // can move along the background
    private void displayPassenger(Input input, boolean canMove) {
        for (Passenger passenger : passengers) {
            if (canMove) {
                passenger.move(input);
            }
            passenger.displayPassengerStat();
        }
    }
    // Display coin on the screen, canMove represent if the coin's image
    // can move along the background
    private void displayCoin(Input input, boolean canMove) {
        for (Coin coin : coins) {
            if (canMove) {
                coin.move(input);
            }
            coin.drawCoin();
        }
    }
    // Check if the taxi has reached some passengers.
    private void reachPassenger(Input input) {
        for(int i = 0; i < passengers.length; i++) {
            if (!taxiDriver.hasVisited(i) && passengers[i].pickUp(input, taxiDriver, i)) {
                break;
            }
        }
    }
    // Check if the taxi has reached some coins.
    private void reachCoin(Input input) {
        for(int i = 0; i < coins.length; i++) {
            if (!taxiDriver.usedBefore(i) && coins[i].collide(taxiDriver)) {
                coins[i].setOnScreen(false);
                taxiDriver.markedCoin(i);
                taxiDriver.setMaxFrame();
                break;
            }
        }
    }
    // check if the taxi have reached the target score
    public boolean playerState() {
        return taxiDriver.getTotalScore() >= targetScore;
    }
    // get the current score of the taxi
    public double getPlayerScore() {
        return taxiDriver.getTotalScore();
    }

    public int getFrame() {
        return maxFrame;
    }
    // Rendering all necessary information on the screen
    // if the taxi is currently picking up or dropping passenger then the game image will be delayed for a
    // short period of time (by allowing specific entity to move or not to move).
    public void displayGame(Input input) {
        maxFrame = maxFrame - 1;
        displayGameBackGround.drawBackGround();
        displayGameMessage.displayGameInfo(targetScore, maxFrame,taxiDriver);
        taxiDriver.displayTaxi();
        if(taxiDriver.move(input)) {
            displayPassenger(input, true);
            displayCoin(input ,true);
            displayGameBackGround.moveBackGround(input);
        }
        else {
            displayPassenger(input, false);
            displayCoin(input ,false);
        }
        reachPassenger(input);
        reachCoin(input);
    }
}
