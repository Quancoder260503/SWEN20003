import bagel.*;
import bagel.util.*;
import java.util.Properties;

/*
* Class DisplayGameMessage created to players scores and targets on the current game play.
* */

public class DisplayGameMessage {
    private final Font GAME_FONT;
    private Point maxFrameDisplayLocation;
    private Point targetDisplayLocation;
    private Point earningDisplayLocation;
    private DisplayMessage targetMessage;
    private DisplayMessage maxFrameMessage;
    private DisplayMessage earningMessage;

    // Method created to parse the coordinates from the property files.
    private Point parseCoordinate(Properties gameProps, String srcA, String srcB) {
        double pointX = Double.parseDouble(gameProps.getProperty(srcA));
        double pointY = Double.parseDouble(gameProps.getProperty(srcB));
        return new Point(pointX, pointY);
    }
    // Method created to read the location of each message.
    private void readCoordinate(Properties gameProps) {
        maxFrameDisplayLocation = parseCoordinate(gameProps, "gameplay.maxFrames.x", "gameplay.maxFrames.y");
        targetDisplayLocation   = parseCoordinate(gameProps, "gameplay.target.x", "gameplay.target.y");
        earningDisplayLocation  = parseCoordinate(gameProps, "gameplay.earnings.x", "gameplay.earnings.y");
    }

    // Method created to read the message that will be rendered on screen.
    private void readMessage(Properties messageProps) {
        targetMessage   = new DisplayMessage(messageProps.getProperty("gamePlay.target"),
                targetDisplayLocation.x, targetDisplayLocation.y);
        maxFrameMessage = new DisplayMessage(messageProps.getProperty("gamePlay.remFrames"),
                maxFrameDisplayLocation.x, maxFrameDisplayLocation.y);
        earningMessage  = new DisplayMessage(messageProps.getProperty("gamePlay.earnings"),
                earningDisplayLocation.x, earningDisplayLocation.y);
    }
    // Initialization
    public DisplayGameMessage(Properties gameProps, Properties messageProps) {
        GAME_FONT = new Font(gameProps.getProperty("font"),
                Integer.parseInt(gameProps.getProperty("gameplay.info.fontSize")));
        readCoordinate(gameProps);
        readMessage(messageProps);
    }
    // Method used to display player current scores, remanining frames and target scores on the game screen.
    public void displayGameInfo(double targetScore, int maxFrame, Taxi taxiDriver) {
        targetMessage.displayDoubleInfo(GAME_FONT, targetScore);
        earningMessage.displayDoubleInfo(GAME_FONT, taxiDriver.getTotalScore());
        maxFrameMessage.displayIntInfo(GAME_FONT, maxFrame);
    }

}
