import bagel.*;
import bagel.util.*;

/*
* Classed created to print Message centered on the screen.
* */

public class DisplayMessage {
    private final String message;
    private final Point location;
    // Initialization
    public DisplayMessage(String message, Font typeFont, String yCoordinate, double Gap) {
        this.message = message;
        double messageX = getCenter(Window.getWidth(), typeFont.getWidth(message));
        double messageY = Double.parseDouble(yCoordinate) + Gap;
        this.location = new Point(messageX, messageY);
    }
    public DisplayMessage(String message, double xLocation, double yLocation) {
        this.message = message;
        location = new Point(xLocation, yLocation);
    }
    // get center position of the text rendered on the screen.
    private double getCenter(double totalWidth, double stringWidth) {
        return (totalWidth - stringWidth) / 2;
    }
    // write message on screen.
    public void outputMessage(Font typeFont) {
        typeFont.drawString(message, location.x, location.y);
    }
    // write message with specific colour.
    public void outputMessageColour(Font typeFont, DrawOptions drawColour) {
        typeFont.drawString(message, location.x, location.y, drawColour);
    }
    // write information including integer.
    public void displayIntInfo(Font typeFont, int stat) {
        String s = String.format("%s %d", message, stat);
        typeFont.drawString(s, location.x, location.y);
    }
    // write information including double.
    public void displayDoubleInfo(Font typeFont, double stat) {
        String s = String.format("%s %.2f", message, stat);
        typeFont.drawString(s, location.x, location.y);
    }
}
