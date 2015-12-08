import java.util.concurrent.LinkedBlockingQueue;

/**
 * Base queue thread to hold common thread stuff
 */
public class QueueThread implements Runnable {

    LinkedBlockingQueue<String> log;
    RandomService randomService;
    BoundedDequeue boundedDequeue;
    BoundedQueue boundedQueue;

    /**
     * Constructor for bounded queue
     * @param boundedQueue
     * @param log
     */
    public QueueThread(BoundedQueue boundedQueue, LinkedBlockingQueue<String> log) {
        this.randomService = new RandomService(1000, 100);
        this.boundedQueue = boundedQueue;
        this.log = log;
    }

    //Constuctor for boundedDequeue
    public QueueThread(BoundedDequeue boundedDequeue, LinkedBlockingQueue<String> log) {
        this.randomService = new RandomService(1000, 100);
        this.boundedDequeue = boundedDequeue;
        this.log = log;
    }
    @Override
    public void run() { }
}
