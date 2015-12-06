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

        while (run) {
            // Create element to enq turn into something more random
            int element = 11;
            try {
                // Wait time will be the value of the deq
                int waitTime = (Integer) boundedQueue.deq();

                //Output Req Both to console and to file
                log.add("Thread " + getName() +
                        " enqueued an item " + element +
                        " at time " + Instant.now().toString());

                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                //Ignore
            }
        }
    }

}
