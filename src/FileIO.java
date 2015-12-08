import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * FileIO class to handle writing to our output file
 */
public class FileIO implements Runnable {

    //Hold the file
    String outputFileName = "output.txt";
    private boolean consoleNoise;
    private Instant startTime;
    long logInterval = 50;
    //System independent new line character
    String newLineChar = System.getProperty("line.separator");
    private LinkedBlockingQueue<String> log;
    boolean stop;

    public FileIO(LinkedBlockingQueue<String> log, boolean consoleNoise) {
        this.log = log;
        this.startTime = Instant.now();
        this.consoleNoise = consoleNoise;
        stop = true;
    }

    /**
     * Will check if there is enough data to write to the file
     * If we have more than 5 things to write then write the whole log to disk
     * Also if its the end then do one last write
     */
    private void checkLog(boolean end) throws InterruptedException {
        if (log.size() > 5 || end) ingestLog();
    }

    /**
     * Will take all items from the log queue and send them to get written to disk
     */
    private void ingestLog() throws InterruptedException {
        ArrayBlockingQueue<String> logs = new ArrayBlockingQueue<String>(100, true);
        int count = 0;

        //While there are logs to read and we haven't read more than 25
        while(!log.isEmpty() || count < 25) {
            if (consoleNoise && log.peek() != null) { System.out.println(log.peek()); }
            logs.add(log.take());
            count++;
        }

        saveDataToFile(logs);
    }

    public void stopRun() {
        stop = false;
    }

    /**
     * Will handle writing output from out program to a file of our choice
     *
     * @param data data to save, write out
     */
    public void saveDataToFile(Queue<String> data) {
        try {
            //Create our file writer object
            FileWriter fileWriter = new FileWriter(outputFileName, true); //Append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //Write data
            while (!data.isEmpty()) {
                bufferedWriter.write(data.remove() + newLineChar); //Write next Log to the file and adds new line charecter to the end
            }
            //Close our resources
            bufferedWriter.close();
            fileWriter.close();
        } catch (Exception e) {
            //We could potentially have a couple different types of exceptions so just catch a generic exception and dump the stack
            e.printStackTrace();
        }
    }

    /**
     * Need to watch the log queue and when it gets to a certain amount
     * Dump it into the output file
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                //Determines how often we check for new writes
                Thread.sleep(logInterval);
                checkLog(false);
            }
            log.add("End of program *******************");
            checkLog(true);
        } catch (InterruptedException e) {
        }
    }

}


