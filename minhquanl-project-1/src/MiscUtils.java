import bagel.Input;
import bagel.Keys;

/**
 * This class contains miscellaneous utility methods.
 */
public class MiscUtils {

    /**
     * Determine the key pressed by the user.
     * @param input The current mouse/keyboard input.
     * @return The key pressed by the user.
     */
    public static String getKeyPress(Input input) {
        String key = null;
        if(input.wasPressed(Keys.A)) {
            key = "A";
        } else if (input.wasPressed(Keys.B)) {
            key = "B";
        } else if (input.wasPressed(Keys.C)) {
            key = "C";
        } else if (input.wasPressed(Keys.D)) {
            key = "D";
        } else if (input.wasPressed(Keys.E)) {
            key = "E";
        } else if (input.wasPressed(Keys.F)) {
            key = "F";
        } else if (input.wasPressed(Keys.G)) {
            key = "G";
        } else if (input.wasPressed(Keys.H)) {
            key = "H";
        } else if (input.wasPressed(Keys.I)) {
            key = "I";
        } else if (input.wasPressed(Keys.J)) {
            key = "J";
        } else if (input.wasPressed(Keys.K)) {
            key = "K";
        } else if (input.wasPressed(Keys.L)) {
            key = "L";
        } else if (input.wasPressed(Keys.M)) {
            key = "M";
        } else if (input.wasPressed(Keys.N)) {
            key = "N";
        } else if (input.wasPressed(Keys.O)) {
            key = "O";
        } else if (input.wasPressed(Keys.P)) {
            key = "P";
        } else if (input.wasPressed(Keys.Q)) {
            key = "Q";
        } else if (input.wasPressed(Keys.R)) {
            key = "R";
        } else if (input.wasPressed(Keys.S)) {
            key = "S";
        } else if (input.wasPressed(Keys.T)) {
            key = "T";
        } else if (input.wasPressed(Keys.U)) {
            key = "U";
        } else if (input.wasPressed(Keys.V)) {
            key = "V";
        } else if (input.wasPressed(Keys.W)) {
            key = "W";
        } else if (input.wasPressed(Keys.X)) {
            key = "X";
        } else if (input.wasPressed(Keys.Y)) {
            key = "Y";
        } else if (input.wasPressed(Keys.Z)) {
            key = "Z";
        }

        return key;
    }

}
