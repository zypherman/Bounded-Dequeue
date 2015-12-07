import java.util.concurrent.LinkedBlockingQueue;

/**
 * Bounded DeQueue is a subclass of the Bounded Queue Class
 * <p/>
 * Implements a push method to push an element to the front of the queue
 * More of a budge method I would say
 */
public class BoundedDequeue<T> extends BoundedQueue {


    public BoundedDequeue(int capacity, LinkedBlockingQueue<String> log) {
        super(capacity, log);
    }

    /**
     * Push element to the head of the queue
     *
     * Added a pushNotFullCondition to ensure that there was a waiting queue for push threads
     *
     * @param x T generic
     */
    @SuppressWarnings("unchecked")
    public void push(T x) {
        boolean mustWakeDequeuers = false;
        deqLock.lock(); // Obtain the deq lock since we are trying to work on the head node

        try {
            //Check if the queue is full before we add an element,
            while (size.get() == capacity) {
                pushNotFullCondition.await(); //Wait for the buffer to have open space

//                We could do some hot garbage that looks like this but yuck
//                Because the notFullCondition is on the enq lock and we need to work with the deq Lock

//                deqLock.unlock();
//                enqLock.lock();
//                notFullCondition.await();
//                deqLock.lock();
            }

            Node newNode = new Node(x); // Create new node
            newNode.next = head;        // Set the new node next node to the current head
            head = newNode;             // Set the new node as the head

            //Check to see if the size before enqueue was 0 since we have to notify waiting dequeuers
            if (size.getAndIncrement() == 0) {
                mustWakeDequeuers = true;
            }

//            log.add(printQueue());
        } catch (InterruptedException ie) {
            System.out.println("push(): Interrupted Exception");
        } finally {
            deqLock.unlock();
        }

        if (mustWakeDequeuers) {        //Wake the Dequers waiting for there to be something to deq
            deqLock.lock();
            try {
                notEmptyCondition.signalAll();
            } finally {
                deqLock.unlock();
            }
        }
    }


}
