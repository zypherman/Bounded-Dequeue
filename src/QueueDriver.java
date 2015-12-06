import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.FileHandler;

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
    static int numberEnq;   // -DnumberEnq = 1
    static int numberDeq;   // -DnumberDeq = 1
    static int queueSize;   // -DqueueSize = 1
    static int maxTime;     // -DmaxTime = 1 in mins
    static boolean dequeue; // -Ddequeue = true

    static LinkedBlockingQueue<String> log;
    static FileHandler handler;
    static Instant startTime;

    /**
     * Configure our logger for the program
     * Will delete old log and then create a new one
     */
    public static void configureLogger() {
        //Figure out our working directory
        Path path = Paths.get(System.getProperty("user.dir") + "/output.txt");
        try {
            //Delete the old log file if it exists
            Files.deleteIfExists(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to see what command line options we were given
     * Assigns them according to their value or uses the default
     */
    public static void parseCommandLineOptions() {
        try {
            numberEnq = Integer.valueOf(System.getProperty("numberEnq", "1"));
            numberDeq = Integer.valueOf(System.getProperty("numberDeq", "1"));
            queueSize = Integer.valueOf(System.getProperty("queueSize", "25"));
            maxTime = Integer.valueOf(System.getProperty("maxTime", "100"));
            dequeue = Boolean.valueOf(System.getProperty("maxTime", "100"));
        } catch (NumberFormatException e) {
            System.out.println("Please enter an integer formatted correctly");
        }
    }

    static int counter = 0;

    /**
     * Checks if the time elapsed as surpassed the max time given
     *
     * @return boolean time
     */
    public static boolean checkTime() {
        return counter++ > maxTime;
//        return Duration.between(startTime, Instant.now()).toMinutes() > maxTime;
    }

    public static void main(String[] args) {
        //Setup methods
        parseCommandLineOptions();
        configureLogger();

        List<QueueThread> runningThreads = new ArrayList<QueueThread>();
        BoundedQueue boundedQueue = new BoundedQueue(queueSize);
        BoundedDequeue boundedDequeue = new BoundedDequeue(queueSize);
        startTime = Instant.now();

        try {
            //Start fileIO thread give it the log and turn on console
            Thread fileIO = new Thread(new FileIO(log, true, maxTime));
            fileIO.start();

            //Create all the enq threads
            for (int i = 0; i < numberEnq; i++) {
                QueueThread thread = dequeue ? new EnqThread(boundedDequeue, log) : new EnqThread(boundedQueue, log);
                thread.start();
                runningThreads.add(thread);
            }

            //Create all the deq threads
            for (int i = 0; i < numberDeq; i++) {
                QueueThread thread = dequeue ? new DeqThread(boundedDequeue, log) : new DeqThread(boundedQueue, log);
                thread.start();
                runningThreads.add(thread);
            }

            //Check for elapsed time
            while (!checkTime()) {
                //Spin while we wait for max time to be reached
            }

            // Tell all threads that time is up
            for (QueueThread thread : runningThreads) {
                thread.interrupt();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            handler.close();
        }

    }
}
