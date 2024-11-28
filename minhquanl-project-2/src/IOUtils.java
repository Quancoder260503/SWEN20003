import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * A utility class that provides methods to read and write files.
 */
public class IOUtils {

    /***
     * Read a file and return a list of String arrays
     * @param file: the path to the CSV file
     * @return: ArrayList<String[]>. Each String[] array represents elements in a single line in the CSV file
     */
    public static ArrayList<String[]> readCommaSeperatedFile(String file) {
        ArrayList<String[]> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String textRead;
            while ((textRead = reader.readLine()) != null) {
                String[] splitText = textRead.split(",");
                lines.add(splitText);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return lines;
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