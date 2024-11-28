import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.util.*;

import java.util.*;

/**
 * Represents the gameplay screen in the game.
 */
public class GamePlayScreen{
    private final Properties GAME_PROPS;
    private final Properties MSG_PROPS;

    // keep track of earning and coin timeout
    private double totalEarnings;
    private double coinFramesActive;

    private int currFrame = 0;
    private int weatherStateIterator = 0;
    private final int BOTTOM_SCREEN;

    // game objects
    private final ArrayList<Vehicle> otherVehicles;
    private Driver taxiDriver;
    private Passenger[] passengers;
    private Coin[] coins;
    private InvisibleStar[] stars;
    private Background background1;
    private Background background2;
    private final float TARGET;
    private final int MAX_FRAMES;
    private String[] weatherState;
    private int[] weatherStartFrame;
    private int[] weatherEndFrame;
    // vars for save score into the file
    private final String PLAYER_NAME;
    private boolean savedData;
    private double minHealthSoFar;
    private boolean isRaining;

    // display text vars
    private final Font INFO_FONT;
    private final int EARNINGS_Y;
    private final int EARNINGS_X;
    private final int COIN_X;
    private final int COIN_Y;
    private final int TARGET_X;
    private final int TARGET_Y;
    private final int MAX_FRAMES_X;
    private final int MAX_FRAMES_Y;
    private final int TRIP_INFO_X;
    private final int TRIP_INFO_Y;
    private final int TRIP_INFO_OFFSET_1;
    private final int TRIP_INFO_OFFSET_2;
    private final int TRIP_INFO_OFFSET_3;
    private final int HEALTH_TAXI_X;
    private final int HEALTH_TAXI_Y;
    private final int HEALTH_PASSENGER_X;
    private final int HEALTH_PASSENGER_Y;
    private final int HEALTH_DRIVER_X;
    private final int HEALTH_DRIVER_Y;

    // Spawn Constant
    private final int INV_RATE_OTHER_CAR;
    private final int INV_RATE_ENEMY_CAR;
    private final int MAX_VEHICLE_Y;
    private final int MIN_VEHICLE_Y;


    public GamePlayScreen(Properties gameProps, Properties msgProps, String playerName) {
        this.GAME_PROPS = gameProps;
        this.MSG_PROPS = msgProps;

        // read game objects from file and weather file and populate the game objects and weather conditions
        ArrayList<String[]> lines = IOUtils.readCommaSeperatedFile(gameProps.getProperty("gamePlay.objectsFile"));
        populateGameObjects(lines);

        this.TARGET = Float.parseFloat(gameProps.getProperty("gamePlay.target"));
        this.MAX_FRAMES = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames"));

        // display text vars
        INFO_FONT = new Font(gameProps.getProperty("font"), Integer.parseInt(
                gameProps.getProperty("gamePlay.info.fontSize")));
        EARNINGS_Y = Integer.parseInt(gameProps.getProperty("gamePlay.earnings.y"));
        EARNINGS_X = Integer.parseInt(gameProps.getProperty("gamePlay.earnings.x"));
        COIN_X = Integer.parseInt(gameProps.getProperty("gameplay.coin.x"));
        COIN_Y = Integer.parseInt(gameProps.getProperty("gameplay.coin.y"));
        TARGET_X = Integer.parseInt(gameProps.getProperty("gamePlay.target.x"));
        TARGET_Y = Integer.parseInt(gameProps.getProperty("gamePlay.target.y"));
        MAX_FRAMES_X = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames.x"));
        MAX_FRAMES_Y = Integer.parseInt(gameProps.getProperty("gamePlay.maxFrames.y"));
        HEALTH_DRIVER_X = Integer.parseInt(gameProps.getProperty("gamePlay.driverHealth.x"));
        HEALTH_DRIVER_Y = Integer.parseInt(gameProps.getProperty("gamePlay.driverHealth.y"));
        HEALTH_PASSENGER_X = Integer.parseInt(gameProps.getProperty("gamePlay.passengerHealth.x"));
        HEALTH_PASSENGER_Y = Integer.parseInt(gameProps.getProperty("gamePlay.passengerHealth.y"));
        HEALTH_TAXI_X = Integer.parseInt(gameProps.getProperty("gamePlay.taxiHealth.x"));
        HEALTH_TAXI_Y = Integer.parseInt(gameProps.getProperty("gamePlay.taxiHealth.y"));
        // current trip info vars
        TRIP_INFO_X = Integer.parseInt(gameProps.getProperty("gamePlay.tripInfo.x"));
        TRIP_INFO_Y = Integer.parseInt(gameProps.getProperty("gamePlay.tripInfo.y"));
        TRIP_INFO_OFFSET_1 = 30;
        TRIP_INFO_OFFSET_2 = 60;
        TRIP_INFO_OFFSET_3 = 90;

        this.PLAYER_NAME = playerName;

        otherVehicles = new ArrayList<>();
        INV_RATE_OTHER_CAR = 200;
        INV_RATE_ENEMY_CAR = 400;
        MAX_VEHICLE_Y = 768;
        MIN_VEHICLE_Y = -50;
        minHealthSoFar = 100.0;
        BOTTOM_SCREEN = 768;
    }

    /**
     * Populate the game objects from the lines read from the game objects file.
     * @param lines list of lines read from the game objects file. lines are processed into String arrays using comma as
     *             delimiter.
     */
    private void populateGameObjects(ArrayList<String[]> lines) {
        setUpBackground();
        int passengerCount = 0;
        int coinCount = 0;
        int starCount = 0;
        for(String[] lineElement: lines) {
            if(lineElement[0].equals(GameObjectType.PASSENGER.name())) {
                passengerCount++;
            } else if(lineElement[0].equals(GameObjectType.COIN.name())) {
                coinCount++;
            } else if(lineElement[0].equals(GameObjectType.INVINCIBLE_POWER.name())) {
                starCount++;
            }
        }
        for(String[] lineElement : lines) {
            if(lineElement[0].equals(GameObjectType.DRIVER.name())) {
                int x = Integer.parseInt(lineElement[1]);
                int y = Integer.parseInt(lineElement[2]);
                taxiDriver = new Driver(new Point(x, y), passengerCount, GAME_PROPS);
                break;
            }
        }
        passengers = new Passenger[passengerCount];
        coins = new Coin[coinCount];
        stars = new InvisibleStar[starCount];
        // process each line in the file
        int passenger_idx = 0;
        int coin_idx = 0;
        int star_idx = 0;
        for(String[] lineElement: lines) {
            int x = Integer.parseInt(lineElement[1]);
            int y = Integer.parseInt(lineElement[2]);
            if(lineElement[0].equals(GameObjectType.TAXI.name())) {
                taxiDriver.setTaxi(new Taxi(new Point(x, y), this.GAME_PROPS));
            } else if(lineElement[0].equals(GameObjectType.PASSENGER.name())) {
                int priority = Integer.parseInt(lineElement[3]);
                int travelEndX = Integer.parseInt(lineElement[4]);
                int travelEndY = Integer.parseInt(lineElement[5]);
                boolean hasUmbrella = (Integer.parseInt(lineElement[6]) == 1);
                Passenger passenger = new Passenger(new Point(x, y), priority,
                                                    travelEndX, travelEndY, hasUmbrella, GAME_PROPS);
                passengers[passenger_idx] = passenger;
                passenger_idx++;

            } else if(lineElement[0].equals(GameObjectType.COIN.name())) {
                Coin coinPower = new Coin(new Point(x, y), this.GAME_PROPS);
                coins[coin_idx] = coinPower;
                coin_idx++;
            } else if(lineElement[0].equals(GameObjectType.INVINCIBLE_POWER.name())) {
                InvisibleStar starPower = new InvisibleStar(new Point(x, y), this.GAME_PROPS);
                stars[star_idx] = starPower;
                star_idx++;
            }
        }
    }
    /**
     * Setting up the background and the weather period information.
     */
    private void setUpBackground() {
        // two background images stacked in y-axis are used to create a scrolling effect
        background1 = new Background(
                new Point(Double.parseDouble(GAME_PROPS.getProperty("window.width")) / 2.0,
                        Double.parseDouble(GAME_PROPS.getProperty("window.height")) / 2.0),
                GAME_PROPS);
        background2 = new Background(
                new Point(Double.parseDouble(GAME_PROPS.getProperty("window.width")) / 2.0,
                        -1 * Double.parseDouble(GAME_PROPS.getProperty("window.height")) / 2.0),
                GAME_PROPS);
        ArrayList<String[]> weatherCondition = IOUtils.readCommaSeperatedFile(GAME_PROPS.getProperty("gamePlay.weatherFile"));
        weatherState = new String[weatherCondition.size()];
        weatherStartFrame = new int[weatherCondition.size()];
        weatherEndFrame = new int[weatherCondition.size()];
        for(int i = 0; i < weatherCondition.size(); i++) {
            weatherState[i] = weatherCondition.get(i)[0];
            weatherStartFrame[i] = Integer.parseInt(weatherCondition.get(i)[1]);
            weatherEndFrame[i] = Integer.parseInt(weatherCondition.get(i)[2]);
        }
    }
    /**
     * Update the states of the game objects based on the keyboard input.
     * Handle the spawning of other cars in random intervals
     * Change the background image and change priorities based on the weather condition
     * Handle collision between game objects
     * Spawn new taxi if the active taxi is destroyed
     * @param input
     * @return true if the game is finished, false otherwise
     */
    public boolean update(Input input) {
        updateBackground(input);
        currFrame++;
        updatePassenger(input);
        taxiDriver.update(input);
        updateVehicle(input);
        checkCollision();
        totalEarnings = taxiDriver.calculateTotalEarnings();
        updateEffectEntities(input);
        displayInfo();
        generateCars();
        return isGameOver() || isLevelCompleted();
    }


    /**
     * updating the passenger attributes.
     * @param input represents the user's input.
     */
    private void updatePassenger(Input input) {
        for(Passenger passenger: passengers) {
            passenger.updateWithTaxi(input, taxiDriver);
            if(isRaining) {
                if (!passenger.hasUmbrella() && passenger.getPriorityStore() == -1) {
                    passenger.setUmbrellaCondition(true);
                }
            }
            else {
                if(!passenger.hasUmbrella() && passenger.getPriorityStore() != -1) {
                    passenger.setUmbrellaCondition(false);
                }
            }
        }
    }
    /**
     * @param object represents the game main entity
     * @return true if the vehicle can take a collision (still surviving and is not in any bumping effect).
     */
    private boolean canNotCollide(GameMainEntity object) {
        return (object.isEliminated() || (object.isInCollision() && object.hasNotReachBumpingEnd()));
    }
    /**
     * Check and performing if any collision occurs between any main game entity objects and attackable objects..
     */
    private void checkCollision() {
        if(taxiDriver.isEjectFromTaxi()) {
            // In the case the driver and passenger (if any trip occurs) are ejected from the taxi,
            // then they can be directly collided by other main entity objects.
            for(Vehicle vehicle : otherVehicles) {
                if (canNotCollide(vehicle) || canNotCollide(taxiDriver)) continue;
                vehicle.setUpCollision(taxiDriver);
                if(taxiDriver.getTrip() != null && !canNotCollide(taxiDriver.getTrip().getPassenger())) {
                    vehicle.setUpCollision(taxiDriver.getTrip().getPassenger());
                }
                if(vehicle instanceof EnemyCar) {
                    ((EnemyCar) vehicle).getFireballHit(taxiDriver);
                    if(taxiDriver.getTrip() != null && !canNotCollide(taxiDriver.getTrip().getPassenger())) {
                        ((EnemyCar) vehicle).getFireballHit(taxiDriver.getTrip().getPassenger());
                    }
                }
            }
        }
        for(Vehicle vehicle : otherVehicles) {
            // Check if any collision occurs between the taxi and other vehicles.
            if (canNotCollide(taxiDriver.getTaxi()) || canNotCollide(vehicle)) {
                continue;
            }
            taxiDriver.getTaxi().setUpCollision(vehicle);
            if (vehicle instanceof EnemyCar) {
                ((EnemyCar) vehicle).getFireballHit(taxiDriver);
            }
        }
        // Check if any collisions occur to passengers (in the case that they are walking toward the trip end flag).
        for(Vehicle vehicle : otherVehicles) {
            if(canNotCollide(vehicle)) {
                continue;
            }
            for(Passenger passenger : passengers) {
                if(passenger.getTrip() != null && passenger.getTrip().isComplete() && !canNotCollide(passenger)) {
                    vehicle.setUpCollision(passenger);
                    if(vehicle instanceof EnemyCar) {
                        ((EnemyCar) vehicle).getFireballHit(passenger);
                    }
                }
            }
        }
        // Check if there is any collision between other vehicles.
        for(int i = 0; i < otherVehicles.size(); i++) {
            if(canNotCollide(otherVehicles.get(i))) {
                continue;
            }
            for(int j = i + 1; j < otherVehicles.size(); j++) {
                if(canNotCollide(otherVehicles.get(j))) {
                    continue;
                }
                otherVehicles.get(i).setUpCollision(otherVehicles.get(j));
                if(otherVehicles.get(i) instanceof EnemyCar) {
                    ((EnemyCar) otherVehicles.get(i)).getFireballHit(otherVehicles.get(j));
                }
            }
        }
    }
    /**
     * Updating the coin power and star power object.
     * @param input represent the user's input.
     */
    private void updateEffectEntities(Input input) {
        if(coins.length > 0) {
            int minFramesActive = coins[0].getMaxFrames();
            for (Coin coinPower : coins) {
                coinPower.update(input);
                coinPower.collide(taxiDriver);

                // check if there's active coin and finding the coin with maximum ttl
                int framesActive = coinPower.getFramesActive();
                if (coinPower.getIsActive() && minFramesActive > framesActive) {
                    minFramesActive = framesActive;
                }
            }
            coinFramesActive = minFramesActive;
        }
        for (InvisibleStar starPower : stars) {
            starPower.update(input);
            starPower.collide(taxiDriver);
        }
    }
    /**
     * Updating the background image of the game.
     * @param input represents user's input.
     */
    private void updateBackground(Input input) {
        if(background1.reachEndFrame() && background2.reachEndFrame()) {
            assert(weatherStateIterator < weatherState.length);
             isRaining = (weatherState[weatherStateIterator].equals("RAINING"));
            if(!isRaining) {
                background1.setImage(new Image(GAME_PROPS.getProperty("backgroundImage.sunny")));
                background2.setImage(new Image(GAME_PROPS.getProperty("backgroundImage.sunny")));
            }
            else {
                background1.setImage(new Image(GAME_PROPS.getProperty("backgroundImage.raining")));
                background2.setImage(new Image(GAME_PROPS.getProperty("backgroundImage.raining")));
            }
            background1.setStartFrame(weatherStartFrame[weatherStateIterator]);
            background1.setEndFrame(weatherEndFrame[weatherStateIterator]);
            background2.setStartFrame(weatherStartFrame[weatherStateIterator]);
            background2.setEndFrame(weatherEndFrame[weatherStateIterator]);
            weatherStateIterator++;
        }
        background1.update(input, background2);
        background2.update(input, background1);
    }

    /**
     * Display the game information on the screen.
     */
    public void displayInfo() {
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.earnings") +
                                   getTotalEarnings(), EARNINGS_X, EARNINGS_Y);
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.target") + String.format("%.02f", TARGET), TARGET_X,
                TARGET_Y);
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.remFrames") + (MAX_FRAMES - currFrame), MAX_FRAMES_X,
                MAX_FRAMES_Y);

        if(coins.length > 0 && coins[0].getMaxFrames() != coinFramesActive) {
            INFO_FONT.drawString(String.valueOf(Math.round(coinFramesActive)), COIN_X, COIN_Y);
        }
/*
        if(stars.length > 0 && stars[0].getMaxFrames() != starFrameActive) {
            INFO_FONT.drawString(String.valueOf(Math.round(starFrameActive)), COIN_X, COIN_Y);
        }
*/
        Trip lastTrip = taxiDriver.getLastTrip();
        if(lastTrip != null) {
            if(lastTrip.isComplete()) {
                INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.completedTrip.title"), TRIP_INFO_X, TRIP_INFO_Y);
            } else {
                INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.onGoingTrip.title"), TRIP_INFO_X, TRIP_INFO_Y);
            }
            INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.trip.expectedEarning")
                    + lastTrip.getPassenger().getTravelPlan().getExpectedFee(), TRIP_INFO_X, TRIP_INFO_Y
                    + TRIP_INFO_OFFSET_1);
            INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.trip.priority")
                    + lastTrip.getPassenger().getTravelPlan().getPriority(), TRIP_INFO_X, TRIP_INFO_Y
                    + TRIP_INFO_OFFSET_2);
            if(lastTrip.isComplete()) {
                INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.trip.penalty") + String.format("%.02f",
                        lastTrip.getPenalty()), TRIP_INFO_X, TRIP_INFO_Y + TRIP_INFO_OFFSET_3);
            }
        }
        displayDriver();
        displayPassengerHealth();
        displayTaxiHealth();
    }

    /**
     * @return the string represents the total earning of the player.
     */
    public String getTotalEarnings() {
        return String.format("%.02f", totalEarnings);
    }
    /**
     * @return a random point with restricted range of coordinates.
     */
    private Point generatePoint() {
        double x = MiscUtils.selectAValue(Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter1")),
                                          Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter2")),
                                          Integer.parseInt(GAME_PROPS.getProperty("roadLaneCenter3")));
        double y = MiscUtils.selectAValue(MAX_VEHICLE_Y, MIN_VEHICLE_Y);
        return new Point(x, y);
    }

    /**
     * Generating other cars and enemy cars.
     */
    private void generateCars() {
        if(MiscUtils.canSpawn(INV_RATE_OTHER_CAR)) {
            int MAX_SPEED_Y = MiscUtils.getRandomInt(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.otherCar.minSpeedY")),
                                                     Integer.parseInt(GAME_PROPS.getProperty("gameObjects.otherCar.maxSpeedY")));
            Point carLocation = generatePoint();
            int carType = MiscUtils.selectAValue(0, 1);
            Image IMAGE;
            if(carType == 0) {
                IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.otherCar.image1"));
            }
            else {
                IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.otherCar.image2"));
            }
            otherVehicles.add(new Car(carLocation, IMAGE, MAX_SPEED_Y, GAME_PROPS));
        }
        if(MiscUtils.canSpawn(INV_RATE_ENEMY_CAR)) {
            int MAX_SPEED_Y = MiscUtils.getRandomInt(Integer.parseInt(GAME_PROPS.getProperty("gameObjects.enemyCar.minSpeedY")),
                                                     Integer.parseInt(GAME_PROPS.getProperty("gameObjects.enemyCar.maxSpeedY")));
            Point carLocation = generatePoint();
            Image IMAGE = new Image(GAME_PROPS.getProperty("gameObjects.enemyCar.image"));
            otherVehicles.add(new EnemyCar(carLocation, IMAGE, MAX_SPEED_Y, GAME_PROPS));
        }
    }

    /**
     * Updating other vehicles (enemy car and other cars).
     * @param input represents the user input
     */
    private void updateVehicle(Input input) {
        for(Vehicle vehicle : otherVehicles) {
            if(vehicle instanceof EnemyCar) {
                if(!vehicle.isEliminated()) {
                    ((EnemyCar) vehicle).generateFireball();
                }
            }
            vehicle.update(input);
        }
    }
    /**
     * Check if the game is over. If the game is over and not saved the score, save the score.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        /* Game is over in three scenarios :
         1. if the current frame is greater than the max frames.
         2. either a passenger or a driver is eliminated during the games.
         3. the driver coordinate is not align with the coordinates of the taxi (in this case the driver can not
            get into the taxi forever).
        */
        boolean isGameOver = currFrame >= MAX_FRAMES;
        if(taxiDriver.isEjectFromTaxi()) {
            if(taxiDriver.getY() < taxiDriver.getTaxi().getY() && taxiDriver.getY() >= BOTTOM_SCREEN) {
                isGameOver = true;
            }
        }
        if(checkHumanElimination(taxiDriver)) {
            isGameOver = true;
        }

        if(taxiDriver.getTrip() != null && taxiDriver.getTrip().getPassenger() != null
                && checkHumanElimination(taxiDriver.getTrip().getPassenger())) {
            isGameOver = true;
        }
        if(isGameOver && !savedData) {
            savedData = true;
            IOUtils.writeLineToFile(GAME_PROPS.getProperty("gameEnd.scoresFile"),
                               PLAYER_NAME + "," + totalEarnings);
        }
        return isGameOver;
    }

    /**
     * @param human represents a taxi driver or a passenger
     * @return true if the human object is eliminated and has its blood effect reach the terminated state.
     */
    private boolean checkHumanElimination(Human human) {
        return (human.getBloodEffect() != null && human.getBloodEffect().reachTerminatedState());
    }

    /**
     * Check if the level is completed. If the level is completed and not saved the score, save the score.
     * @return true if the level is completed, false otherwise.
     */
    public boolean isLevelCompleted() {
        // Level is completed if the total earnings is greater than or equal to the target earnings
        boolean isLevelCompleted = totalEarnings >= TARGET;
        if(isLevelCompleted && !savedData) {
            savedData = true;
            IOUtils.writeLineToFile(GAME_PROPS.getProperty("gameEnd.scoresFile"),
                               PLAYER_NAME + "," + totalEarnings);
        }
        return isLevelCompleted;
    }

    /**
     * displaying the health of the taxi.
     */
    public void displayTaxiHealth() {
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.taxiHealth") +
                        String.format("%.02f",taxiDriver.getTaxi().getHealth()),
                HEALTH_TAXI_X, HEALTH_TAXI_Y);
    }

    /**
     * Display the health of the current passenger.
     */
    public void displayPassengerHealth() {
        if(taxiDriver.getTrip() == null) {
            INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.passengerHealth") +
                            String.format("%.02f", minHealthSoFar),
                    HEALTH_PASSENGER_X, HEALTH_PASSENGER_Y);
        }
        else {
            minHealthSoFar = Math.min(minHealthSoFar, taxiDriver.getTrip().getPassenger().getHealth());
            INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.passengerHealth") +
                            String.format("%.02f", taxiDriver.getTaxi().getHealth()),
                    HEALTH_PASSENGER_X, HEALTH_PASSENGER_Y);
        }
    }

    /**
     * Display the health of the driver.
     */
    public void displayDriver() {
        INFO_FONT.drawString(MSG_PROPS.getProperty("gamePlay.driverHealth") +
                        String.format("%.02f", taxiDriver.getHealth()),
                HEALTH_DRIVER_X, HEALTH_DRIVER_Y);
    }

}
