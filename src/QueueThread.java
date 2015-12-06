import java.util.concurrent.LinkedBlockingQueue;

/**
 * Base queue thread to hold common thread stuff
 */
public class QueueThread implements Runnable {

    LinkedBlockingQueue<String> log;
    RandomService randomService;
    BoundedDequeue boundedDequeue;
    BoundedQueue boundedQueue;

    public QueueThread(BoundedQueue boundedQueue, LinkedBlockingQueue<String> log) {
        this.randomService = new RandomService(1000, 100);
        this.boundedQueue = boundedQueue;
        this.log = log;
    }

    public QueueThread(BoundedDequeue boundedDequeue, LinkedBlockingQueue<String> log) {
        this.boundedDequeue = boundedDequeue;
        this.log = log;
    }
    @Override
    public void run() { }
}
