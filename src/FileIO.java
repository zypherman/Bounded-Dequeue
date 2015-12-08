import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * FileIO class to handle writing to our output file
 */
public class FileIO implements Runnable {

    String newLineChar = System.getProperty("line.separator");
    private LinkedBlockingQueue<String> log;
    String outputFileName = "output.txt";
    private boolean consoleNoise;
    long logInterval = 50;

    /**
     * FileIO constructor
     *
     * @param (LinkedBlockingQueue<String>) log
     * @param (boolean)                     consoleNoise
     */
    public FileIO(LinkedBlockingQueue<String> log, boolean consoleNoise) {
        this.log = log;
        this.consoleNoise = consoleNoise;
    }

    /**
     * Will check if there is enough data to write to the file
     * If we have more than 5 things to write then write the whole log to disk
     * Also if its the end then do one last write
     *
     * @param (boolean) end of program
     */
    private void checkLog(boolean end) throws InterruptedException {
        if (log.size() > 5 || end) ingestLog();
    }

    /**
     * Will take atleast 25 items from the log queue and send them to get written to disk
     */
    private void ingestLog() throws InterruptedException {
        ArrayBlockingQueue<String> logs = new ArrayBlockingQueue<String>(100, true);
        int count = 0;

        //While there are logs to read and we haven't read more than 25
        while (!log.isEmpty() || count < 25) {
            if (consoleNoise && log.peek() != null) {
                System.out.println(log.peek());
            }
            logs.add(log.take());
            count++;
        }

        saveDataToFile(logs);
    }

    /**
     * Will handle writing output from out program to a file of our choice
     *
     * @param (Queue<String>) data data to save, write out
     */
    public void saveDataToFile(Queue<String> data) {
        try {
            //Create our file writer object
            FileWriter fileWriter = new FileWriter(outputFileName, true); //Append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //Write data
            while (!data.isEmpty()) {
                bufferedWriter.write(data.remove() + newLineChar); //Write next Log to the file and adds new line character to the end
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
            checkLog(true); //One last write of the rest of the log data
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}


