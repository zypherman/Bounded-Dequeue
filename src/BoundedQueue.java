import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue<T> {

    ReentrantLock enqLock, deqLock;
    //notEmpty goes on the deque lock to ensure you dont remove from an empty queue
    //notFull goes on enq to stop from adding to full queue
    Condition notEmptyCondition, notFullCondition;
    AtomicInteger size;
    volatile Node head, tail;
    int capacity;


    /**
     * Constructor for bounded queue
     * Will add conditions to locks and initialize them as reentrant locks
     *
     * @param _capacity int
     */
    public BoundedQueue(int _capacity) {
        capacity = _capacity;
        head = new Node(null);
        tail = head;
        size = new AtomicInteger(0);
        enqLock = new ReentrantLock();
        notFullCondition = enqLock.newCondition();
        deqLock = new ReentrantLock();
        notEmptyCondition = deqLock.newCondition();
    }


    /**
     * Enqueue method to add element to tail of the queue
     *
     * @param x T generic
     */
    public void enq(T x) {
        boolean mustWakeDequeuers = false;

        //Gain the lock on the tail
        enqLock.lock();
        try {
            //Check if the queue is full before we add an element, if it is then we wait for the condition to be resolved
            while (size.get() == capacity) {
                notFullCondition.await();
            }

            //Declare new node we are going to add to the queue and give it the generic T object we are adding
            Node e = new Node(x);
            tail.next = e; //Set the current tails next node to be the node we add
            tail = e; //Change the tail to be our added node

            //Check to see if the size before enqueue was 0 since we have to notify waiting dequeuers
            if (size.getAndIncrement() == 0) {
                mustWakeDequeuers = true;
            }

        } catch (InterruptedException ie) {
            System.out.println("enq(): Interrupted Exception");
        } finally {
            enqLock.unlock();
        }

        //Notify the deqers
        if (mustWakeDequeuers) {
            deqLock.lock();
            try {
                //Signal all waiting on empty condition after gaining the deq lock
                notEmptyCondition.signalAll();
            } finally {
                deqLock.unlock();
            }
        }
    }


    /**
     * Dequeue method to remove element from the head of the queue
     *
     * @return T
     */
    public T deq() {
        T result = null;
        boolean mustWakeEnqueuers = false;
        //Obtail the deq lock
        deqLock.lock();
        try {
            //Ensure there is something to deq, if not then wait
            while (size.get() == 0) {
                notEmptyCondition.await();
            }

            //Checks the the next value is not null, we should actually null check this better since it can wreck the whole project right?
            result = head.next.value;

            //New head is the next element
            head = head.next;

            //If we previously had a full queue then we need to alert the enquers
            if (size.getAndDecrement() == capacity) {
                mustWakeEnqueuers = true;
            }

        } catch (InterruptedException ie) {
            System.out.println("enq(): Interrupted Exception");
        } finally {
            deqLock.unlock();
        }

        //Wake the enquers waiting for the lock
        if (mustWakeEnqueuers) {
            enqLock.lock();
            try {
                notFullCondition.signalAll();
            } finally {
                enqLock.unlock();
            }
        }
        return result;
    }

    /**
     * Node object class
     * Holds a generic T which is the value
     * Holds a reference to the next node
     */
    protected class Node {
        public T value;
        //Volatile ensures all threads see the same value
        public volatile Node next;

        public Node(T x) {
            value = x;
            next = null;
        }
    }
}