package tools;

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *  The DaryMinPQ class represents a priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  It is possible to build the priority queue using a Comparator.
 *  If not, the natural order relation between the keys will be used.
 *  
 *  This implementation uses a d-ary heap.
 *  For simplified notations, logarithm in base d will be referred as log-d
 *  The delete-the-minimum operation takes time proportional to d*log-d(n)
 *  The insert takes time proportional to log-d(n)
 *  The is-empty, min-key and size operations take constant time.
 *  Constructor takes time proportional to the specified capacity.
 *
 *  @author Tristan Claverie
 */

public class DaryMinPQ<Key extends Comparable<Key>> implements Iterable<Key> {
	private final int D; 				//Dimension of the heap
	private int N;						//Number of keys currently in the heap
	private int order;					//Number of levels of the tree
	private Key[] keys;					//Array of keys
	private final Comparator<Key> COMP;	//A comparator over the keys
	
	
	/**
     * Initializes an empty priority queue
     * @param D dimension of the heap
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public DaryMinPQ(int D) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.D = D;
		order = 1;
		keys = (Key[]) new Comparable[1+D];
		COMP = new MyComparator();
	}
	
	/**
     * Initializes an empty priority queue
     * @param D dimension of the heap
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public DaryMinPQ(int D, Comparator<Key> Comp) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.D = D;
		order = 1;
		keys = (Key[]) new Comparable[1+D];
		COMP = Comp;
	}
	
	/**
     * Initializes a priority queue with given indexes
     * @param D dimension of the heap
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public DaryMinPQ(int D, Key[] a) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.D = D;
		order = 1;
		keys = (Key[]) new Comparable[1+D];
		COMP = new MyComparator();
		for (Key k : a) insert(k);
	}
	
	/**
     * Initializes a priority queue with given indexes
     * @param D dimension of the heap
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public DaryMinPQ(int D, Comparator<Key> Comp, Key[] a) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.D = D;
		order = 1;
		keys = (Key[]) new Comparable[1+D];
		COMP = Comp;
		for (Key k : a) insert(k);
	}

	/**
	 * Whether the priority queue is empty
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * Number of elements currently on the priority queue
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		return N;
	}

	/**
	 * Add a Key to the priority queue
	 * @param key a Key
	 */
	public void insert(Key key) {
		keys[N] = key;
		swim(N++);
		if (N == keys.length) {
			resize(getN(order+1));
			order++;
		}
	}

	/**
	 * Get the minimum key currently in the queue
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return keys[0];
	}

	/**
	 * Delete the minimum key
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key
	 */
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		exch(0, --N);
		sink(0);
		Key min = keys[N];
		keys[N] = null;
		int number = getN(order-2);
		if(order > 1 && N == number)  {
			resize(number+(int)Math.pow(D, order-1));
			order--;
		}
		return min;
	}
	
	/***************************
	 * General helper functions
	 **************************/
	
	private boolean greater(int i, int j) {
		if (keys[i] == null) return false;
		if (keys[j] == null) return true;
		return COMP.compare(keys[i], keys[j]) > 0;
	}
	
	//exchange the position of two keys
	private void exch(int i, int j) {
		Key swap = keys[i];
		keys[i] = keys[j];
		keys[j] = swap;
	}
	
	//get the maximum number of keys in the heap, given the number of levels of the tree
	private int getN(int order) {
		int power = 1;
		int res = 1;
		for(int i = 1 ; i <= order ; i++) {
			power = power*D;
			res += power;
		}
		return res;
	}
	
	/***************************
	 * Functions for moving upward or downward
	 **************************/
	
	private void swim(int i) {
		if (i > 0 && greater((i-1)/D, i)) {
			exch(i, (i-1)/D);
			swim((i-1)/D);
		}
	}
	
	private void sink(int i) {
		int child = D*i+1;
		if (child >= N) return;
		int min = minChild(i);
		while (min < N && greater(i, min)) {
			exch(i, min);
			i = min;
			min = minChild(i);
		}
	}
	
	/***************************
	 * Deletes the minimum child
	 **************************/
	
	private int minChild(int i) {
		int loBound = D*i+1, hiBound = D*i+D;
		int min = loBound;
		for (int cur = loBound ; cur <= hiBound ; cur++) {
			if (cur < N && greater(min, cur)) min = cur;
		}
		return min;
	}
	
	/***************************
	 * Resize the priority queue
	 **************************/
	
	private void resize(int N) {
		Key[] array = (Key[]) new Comparable[N];
		for (int i = 0 ; i < Math.min(keys.length, array.length) ; i++) {
			array[i] = keys[i];
			keys[i] = null;
		}
		keys = array;
	}
	
	/***************************
	 * Iterator
	 **************************/
	
	/**
	 * Get an Iterator over the keys in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * @return an Iterator over the keys in the priority queue in ascending order
	 */
	
	public Iterator<Key> iterator() {
		return new MyIterator();
	}
	
	//Constructs an Iterator over the keys in linear time
	private class MyIterator implements Iterator<Key> {
		DaryMinPQ<Key> data;
		
		public MyIterator() {
			data = new DaryMinPQ<Key>(D);
			data.keys = (Key[]) new Comparable[keys.length];
			data.N = N;
			for (int i = 0 ; i < N ; i++) {
				data.keys[i] = keys[i];
			}
		}

		public boolean hasNext() {
			return !data.isEmpty();
		}
		
		public Key next() {
			return data.delMin();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	/***************************
	 * Comparator
	 **************************/
	
	//default Comparator
	private class MyComparator implements Comparator<Key> {
		@Override
		public int compare(Key key1, Key key2) {
			return key1.compareTo(key2);
		}
	}
	
}