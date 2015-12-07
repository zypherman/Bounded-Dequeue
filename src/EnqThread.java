import java.time.Instant;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Enqueue thread
 * Will attempt to enqueue elements from our BoundedQueue and BoundedDequeue
 */
public class EnqThread extends QueueThread {

    public EnqThread(BoundedQueue boundedQueue, LinkedBlockingQueue<String> log) {
        super(boundedQueue, log);
    }

    @Override
    @SuppressWarnings("unchecked") //Live life dangerously
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            // Create element to enq and make random
            int element = randomService.getRandom();
            try {
                //Enq on queue
                boundedQueue.enq(element);

                //Output Req Both to console and to file
                log.add("Thread " + Thread.currentThread().getName() +
                        " enqueued an item " + element +
                        " at time " + Instant.now().toString());

                Thread.sleep(randomService.getRandom());
            } catch (InterruptedException e) {
                log.add("Thread " + Thread.currentThread().getName() +
                        " died at time " + Instant.now().toString());
            }
        }

    }
}
