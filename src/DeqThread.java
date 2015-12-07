import java.time.Instant;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Dequeue thread
 * Will attempt to dequeue elements from our BoundedQueue and BoundedDequeue
 */
public class DeqThread extends QueueThread {

    public DeqThread(BoundedQueue boundedQueue, LinkedBlockingQueue<String> log) {
        super(boundedQueue, log);
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Wait time will be the value of the deq
                int waitTime = (Integer) boundedQueue.deq();

                //Output Req Both to console and to file
                log.add("Thread " + Thread.currentThread().getName() +
                        " dequeued an item " + waitTime +
                        " at time " + Instant.now().toString());

                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                log.add("Thread " + Thread.currentThread().getName() +
                        " died at time " + Instant.now().toString());
            }
        }
    }

}
