import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Driver class requirements
 * <p/>
 * Create Dequeueing and Enqueueing Threads as well as a bounded queue of a specified size
 * <p/>
 * User should be able to specify the number of each of those things
 * <p/>
 * Enq thread should try to call enq randomly with a mean of 1000 ms between attempts Rand Gaussian distribution
 * <p/>
 * After a successful deq the thread should sleep for a number of milliseconds equal to the items value it just deq then tries to deq again
 * <p/>
 * User enter amount of time for program to run
 * <p/>
 * Options needed, numberEnq, numberDeq, queueSize, maxTime
 */
public class QueueDriver {

    //Optional Arguments pass them as vm options
    static int numberEnq; // -DnumberEnq = 1
    static int numberDeq; // -DnumberDeq = 1
    static int queueSize; // -DqueueSize = 1
    static int maxTime;   // -DmaxTime = 1 in mins

    static FileHandler handler;
    static Logger logger;


    /**
     * Configure out logger for the program
     * Will delete old log and then create a new one
     * Adds the file handler to the logger so it can write log entries to a file
     */
    public static void configureLogger() {
        //Figure out our working directory
        logger = Logger.getLogger("Log"); // "Log" is usable from all classes
        Path path = Paths.get(System.getProperty("user.dir") + "/output.log");

        try {
            //Delete the old log file if it exists
            Files.deleteIfExists(path);
            handler = new FileHandler("output.log", true);
            logger.addHandler(handler); //Logger needs file handler to write to file
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            handler.close();
        }
    }

    /**
     * Checks to see what command line options we were given
     * Assigns them according to their value or uses the default
     */
    public static void parseCommandLineOptions() {
        try {
            numberEnq = Integer.valueOf(System.getProperty("numberEnq", "10"));
            numberDeq = Integer.valueOf(System.getProperty("numberDeq", "10"));
            queueSize = Integer.valueOf(System.getProperty("queueSize", "25"));
            maxTime = Integer.valueOf(System.getProperty("maxTime", "2"));
        } catch (NumberFormatException e) {
            System.out.println("Please enter an integer formatted correctly");
        }
    }

    public static void main(String[] args) {
        //Setup methods
        parseCommandLineOptions();
        configureLogger();

        BoundedQueue boundedQueue = new BoundedQueue(queueSize);

        //Create enq and deq threads as well as bounded queue



    }


    //Output Req Both to console and to file
    //Format "Thread <nr> enqueued an item <value> at time <time>
}
