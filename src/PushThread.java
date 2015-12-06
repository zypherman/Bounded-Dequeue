import java.time.Instant;
import java.util.concurrent.LinkedBlockingQueue;

public class PushThread extends QueueThread {

    public PushThread(BoundedDequeue boundedDequeue, LinkedBlockingQueue<String> log) {
        super(boundedDequeue, log);
    }

    @Override
    @SuppressWarnings("unchecked") //Live life dangerously
    public void run() {

        while (run) {
            // Create element to enq and make random
            int element = 16;
            try {
                //Enq on queue
                boundedDequeue.push(element);

                //Output Req Both to console and to file
                log.add("Thread " + getName() +
                        " pushed an item " + element +
                        " at time " + Instant.now().toString());

                Thread.sleep(1000); //TODO Change to random interval
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
