/**
 * Bounded DeQueue is a subclass of the Bounded Queue Class
 * <p/>
 * Implements a push method to push an element to the front of the queue
 * More of a budge method I would say
 */
public class BoundedDequeue<T> extends BoundedQueue {


    public BoundedDequeue(int capacity) {
        super(capacity);
    }

    /**
     * Push element to the head of the queue
     *
     * @param x T generic
     */
    @SuppressWarnings("unchecked")
    public void push(T x) {
        boolean mustWakeDequeuers = false;
        deqLock.lock();                 // Obtain the deq lock since we are trying to work on the head node
        try {
            //Check if the queue is full before we add an element,
            // if it is then we wait for the condition to be resolved
            while (size.get() == capacity) {
                notFullCondition.await();
            }

            Node newNode = new Node(x); // Create new node
            newNode.next = head;        // Set the new node next node to the current head
            head = newNode;             // Set the new node as the head

            //Check to see if the size before enqueue was 0 since we have to notify waiting dequeuers
            if (size.getAndIncrement() == 0) {
                mustWakeDequeuers = true;
            }
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
