import java.io.*;
import java.util.Properties;

/**
 * A utility class that provides methods to read and write files.
 */
public class IOUtils {

    /***
     * Read a file and return a list of String arrays
     * @param file: the path to the CSV file
     * @return: String[][]. Each String[] array represents elements in a single line in the CSV file
     */
    public static String[][] readCommaSeparatedFile(String file) {
        try {
            // checking number of lines in file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int numLines = 0;

            while (reader.readLine() != null) {
                numLines++;
            }
            reader.close();

            reader = new BufferedReader(new FileReader(file));
            String[][] lines = new String[numLines][];
            String textRead;
            int lineIndex = 0;

            while ((textRead = reader.readLine()) != null) {
                String[] splitText = textRead.split(",");
                lines[lineIndex++] = splitText;
            }
            return lines;

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return null;
    }
    /***
     * Read a file and return a list of String arrays
     * @param file: the path to the CSV file
     * @return: String[][]. Each String[] array represents elements in a single line in the CSV file
     */
    public static String[][] readHyphenSeparatedFile(String file) {
        try {
            // checking number of lines in file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int numLines = 0;

            while (reader.readLine() != null) {
                numLines++;
            }
            reader.close();

            reader = new BufferedReader(new FileReader(file));
            String[][] lines = new String[numLines][];
            String textRead;
            int lineIndex = 0;

            while ((textRead = reader.readLine()) != null) {
                String[] splitText = textRead.split("-");
                splitText[0] = splitText[0].substring(0, Math.max(0, splitText[0].length() - 1));
                splitText[1] = splitText[1].substring(1);
                lines[lineIndex++] = splitText;
            }
            return lines;

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return null;
    }


    /***
     * Read a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return: Properties object
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return appProps;
    }

    /**
     * Write a line to a file
     * @param filename The name of the file
     * @param line The line (player name & the score) to be written
     */
    public static void writeLineToFile(String filename, String line) {
        try(FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}