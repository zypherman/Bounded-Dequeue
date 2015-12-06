import java.util.concurrent.LinkedBlockingQueue;

/**
 * Base queue thread to hold common thread stuff
 */
public class QueueThread extends Thread {

    LinkedBlockingQueue<String> log;
    BoundedDequeue boundedDequeue;
    BoundedQueue boundedQueue;
    boolean run;

    public QueueThread(BoundedQueue boundedQueue, LinkedBlockingQueue<String> log) {
        this.boundedQueue = boundedQueue;
        this.log = log;
        run = true;
    }

    public QueueThread(BoundedDequeue boundedDequeue, LinkedBlockingQueue<String> log) {
        this.boundedDequeue = boundedDequeue;
        this.log = log;
        run = true;
    }

    public void setRun() {
        run = false;
    }
}
