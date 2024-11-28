import bagel.Input;
import bagel.Keys;

import java.util.Random;

/**
 * This class contains miscellaneous utility methods.
 */
public class MiscUtils {

    /**
     * Check if the game should spawn a new invader.
     * @param invRate The rate of invader spawn.
     * @return true if the game should spawn a new invader, false otherwise.
     */
    public static boolean canSpawn(int invRate) {
        int rnd = new Random().nextInt(1000) + 1; // Random delay between 1 and 10 seconds
        return rnd % invRate == 0;
    }

    /**
     * Get a random integer between min and max.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return A random integer between min and max.
     */
    public static int getRandomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * Select a value randomly between val1 and val2.
     * @param val1 The first value.
     * @param val2 The second value.
     * @return A random value between val1 and val2.
     */
    public static int selectAValue(int val1, int val2) {
        return new Random().nextBoolean() ? val1 : val2;
    }


    /**
     * Select a value randomly between val1, val2 and val3
     * @param val1 The first value
     * @param val2 The second value
     * @param val3 The third value
     * @return A random value between val1, val2, and val3
     */
    public static int selectAValue(int val1, int val2, int val3) {
        int rnd = new Random().nextInt(3) + 1;
        int [] arr = new int[3];
        arr[0] = val1;
        arr[1] = val2;
        arr[2] = val3;
        return arr[rnd - 1];
    }

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