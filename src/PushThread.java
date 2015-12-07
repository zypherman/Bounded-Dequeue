import java.time.Instant;
import java.util.concurrent.LinkedBlockingQueue;

public class PushThread extends QueueThread {

    public PushThread(BoundedDequeue boundedDequeue, LinkedBlockingQueue<String> log) {
        super(boundedDequeue, log);
    }

    @Override
    @SuppressWarnings("unchecked") //Live life dangerously
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            // Create new element from random number
            int element = randomService.getRandom();
            try {
                //Enq on queue
                boundedDequeue.push(element);

                //Output Req Both to console and to file
                log.add("Thread " + Thread.currentThread().getName() +
                        " pushed an item " + element +
                        " at time " + Instant.now().toString());

                Thread.sleep(randomService.getRandom());
            } catch (InterruptedException e) {
                log.add("Thread " + Thread.currentThread().getName() +
                        " died at time " + Instant.now().toString());
            }
        }

    }
}
