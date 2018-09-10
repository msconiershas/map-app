
/**
 * The MinPriorityQueueADT interface describes a data structure that maintains a minimum
 * priority queue, supporting isEmpty(), removeMin(), and insert().
 * 
 * You must implement this interface in a file named FileLinePriorityQueue.java 
 * using an array-based heap.
 */
public interface MinPriorityQueueADT<E> {
    /**
     * Removes the minimum element from the Priority Queue, and returns it.
     *
     * @return the minimum element in the queue
     * @throws PriorityQueueEmptyException if the priority queue has no elements
     * in it
     */
    public E removeMin() throws PriorityQueueEmptyException;

    /**
     * Inserts a FileLine into the queue, making sure to keep the shape and
     * order properties intact.
     *
     * @param double
     * @param Object
     * @throws PriorityQueueFullException if the priority queue is full.
     */
    public void insert(double a, Location b) throws PriorityQueueFullException;

    /**
     * Checks if the queue is empty.
     *
     * @return true, if it is empty; false otherwise
     */
    public boolean isEmpty();
}
