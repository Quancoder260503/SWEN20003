import bagel.*;
import bagel.util.*;
import java.util.Properties;

/* class created to display the records currently and previously achieved by a player in the on-going game.
* */

public class DisplayPlayerStat {
    private final int GAP = 30;
    private final Font StatFont;
    private Point expFeeLocation;
    private Point priorityLocation;
    private Point penaltyLocation;
    private Point displayStatusLocation;
    private final boolean isCurrent;
    private final String Message;
    private final String Penalty;
    private final String ExpectedEarn;
    private final String Priority;

    TripStat playerStat;

    private Point parseCoordinate(String srcX, String srcY) {
        double pointX = Double.parseDouble(srcX);
        double pointY = Double.parseDouble(srcY);
        return new Point(pointX, pointY);
    }
    // Initialization
    private void parseCoordinateInfo(Properties gameProps) {
        displayStatusLocation = parseCoordinate(gameProps.getProperty("gameplay.tripInfo.x"),
                                                gameProps.getProperty("gameplay.tripInfo.y"));
        expFeeLocation   = new Point(displayStatusLocation.x, displayStatusLocation.y + GAP);
        priorityLocation = new Point(displayStatusLocation.x, displayStatusLocation.y + 2 * GAP);
        penaltyLocation  = new Point(displayStatusLocation.x, displayStatusLocation.y + 3 * GAP);
    }

    public DisplayPlayerStat(Properties gameProps, Properties messagesProps, boolean state) {
        StatFont = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gameplay.info.fontSize")));
        isCurrent = state;
        parseCoordinateInfo(gameProps);
        ExpectedEarn = messagesProps.getProperty("gamePlay.trip.expectedEarning");
        Priority = messagesProps.getProperty("gamePlay.trip.priority");
        Penalty = messagesProps.getProperty("gamePlay.trip.penalty");
        if (isCurrent) {
            Message = messagesProps.getProperty("gamePlay.onGoingTrip.title");
        } else {
            Message = messagesProps.getProperty("gamePlay.completedTrip.title");
        }
    }
    // Initializing and displaying top player's statistics.
    public void setStat(double expectedEarning, int priority, double penalty) {
        playerStat = new TripStat(expectedEarning, priority, penalty);
    }

    public void displayStat() {
        StatFont.drawString(Message, displayStatusLocation.x, displayStatusLocation.y);
        StatFont.drawString(String.format("%s %.1f", ExpectedEarn, playerStat.getExpectedEarnings()),
                                                     expFeeLocation.x, expFeeLocation.y);
        StatFont.drawString(String.format("%s %d", Priority, playerStat.getPriority()),
                                                   priorityLocation.x, priorityLocation.y);
        if(!isCurrent) {
            StatFont.drawString(String.format("%s %.2f", Penalty, playerStat.getPenalty()),
                                penaltyLocation.x, penaltyLocation.y);
        }
    }
}
