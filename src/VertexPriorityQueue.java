

import java.util.ArrayList;
import java.util.Collections;

/**
 * Creates a MinPriorityQueue of vertices. Operations include insert, remove, 
 * isEmpty, and size. Each position in queue is given a vertex location and 
 * priority value.
 *
 * @authors: McKinley Sconiers-Hasan, Grant Darin
 */
public class VertexPriorityQueue implements MinPriorityQueueADT<Object[]> {

	ArrayList<Object[]> pairs; //vertex pairs in queue
	private int num; //number of vertex pairs in queue

	/** VertexPriorityQueue constructor*/
	public VertexPriorityQueue() {
		pairs = new ArrayList<Object[]>();
		num = 0;
	}
	
	/** Removes and returns vertex with the minimum value */
	public Object[] removeMin() throws PriorityQueueEmptyException {
		if(pairs.size() == 0) {
			throw new PriorityQueueEmptyException();
		}

		//create new arraylist to stores vertex priority values
		ArrayList<Double> list = new ArrayList<Double>();
		//initialize vertex locations to each have a priority value
		for(int i = 0; i < pairs.size(); i++) {
			Object[] m = pairs.get(i);
			list.add((Double) m[0]);
		}
		//sort list based on value to prepare for removal of minimum
		Collections.sort(list);
		
		//find where list.get(0) is in the pairs array
		for(int i = 0; i < pairs.size(); i++) {
			if(pairs.get(i)[0] == list.get(0)) {
				num--;
				return pairs.remove(i);
			}
		}
		return null;
	}
	/**Inserts a vertex into queue.
	 * 
	 * parameter: double a = priority value of vertex
	 * parameter: Location b = location of vertex
	 */
	public void insert(double a, Location b) throws PriorityQueueFullException{
		Object[] array = new Object[2]; 
		array[0] = a; //initialize first part of vertex to its value
		array[1] = b; //initialize second part of vertex to its location
		pairs.add(array);
		num++;
	}
	
	/**Returns true of false based on statement saying queue is empty*/
	public boolean isEmpty() {
		return pairs.size() == 0;
	}

	/**Returns number of vertices in queue*/
	public int size() {
		return num;
	}
}
