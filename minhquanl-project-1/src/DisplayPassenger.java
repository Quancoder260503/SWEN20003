import bagel.*;
import bagel.util.*;
import java.util.Properties;
/*
* Class created to display passenger attributes (i.e. current passenger priority, expected earning) and passenger's
* image on the screen.
* */


public class DisplayPassenger {
    private final Image PASSENGER_IMAGE;
    private final Font  PASSENGER_FONT;
    private DisplayMessage expectedDisplay;
    private DisplayMessage priorityDisplay;
    private final double GAP_PRIORITY = 30.0;
    private final double GAP_EARNING  = 100.0;

    // Initialization
    public DisplayPassenger(Properties gameProps, Properties messageProps) {
        PASSENGER_IMAGE = new Image(gameProps.getProperty("gameObjects.passenger.image"));
        PASSENGER_FONT  = new Font(gameProps.getProperty("font"),
                                   Integer.parseInt(gameProps.getProperty("gameObjects.passenger.fontSize")));
    }
    // Display the passenger's image and attributes on the screen.
    public void displayPassenger(Passenger passenger) {
        Point passengerLocation = passenger.getPosition();
        priorityDisplay = new DisplayMessage(String.valueOf(passenger.getPriority()),
                passengerLocation.x - GAP_PRIORITY, passengerLocation.y);
        expectedDisplay = new DisplayMessage(String.format("%.1f", passenger.getExpectedEarnings()),
                passengerLocation.x - GAP_EARNING, passengerLocation.y);
        priorityDisplay.outputMessage(PASSENGER_FONT);
        expectedDisplay.outputMessage(PASSENGER_FONT);
        PASSENGER_IMAGE.draw(passengerLocation.x, passengerLocation.y);
    }
}
