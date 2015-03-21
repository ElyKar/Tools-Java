package tools;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The IndexDaryMinPQ class represents an indexed priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations,
 *  along with delete and change-the-key methods. 
 *  In order to let the client refer to keys on the priority queue,
 *  an integer between 0 and N-1 is associated with each key ; the client
 *  uses this integer to specify which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  
 *  This implementation uses a d-ary heap along with an array to associate
 *  keys with integers in the given range.
 *  For simplified notations, logarithm in base d will be referred as log-d
 *  The delete-the-minimum, delete, change-key and increase-key operations
 *  take time proportional to d*log-d(n)
 *  The insert and decrease-key take time proportional to log-d(n)
 *  The is-empty, min-index, min-key, size, contains and key-of operations take constant time.
 *  Construction takes time proportional to the specified capacity.
 *
 *  @author Tristan Claverie
 */

public class IndexDaryMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	private final int D;	//Dimension of the heap
	private int N;			//Number of keys currently in the queue
	private int NMAX;		//Maximum number of items in the queue
	private int[] pq;		//D-ary heap
	private int[] qp;		//Inverse of pq : qp[pq[i]] = pq[qp[i]] = i
	private Key[] keys;		//keys[i] = priority of i
	
	
	/**
     * Initializes an empty indexed priority queue with indices between 0 and N-1.
     * @param N number of keys in the priority queue, index from 0 to N-1
     * @param D dimension of the heap
     * @throws java.lang.IllegalArgumentException if N < 0
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public IndexDaryMinPQ(int N, int D) {
		if (N < 0) throw new IllegalArgumentException("Maximum number of elements cannot be negative");
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.D = D;
		NMAX = N;
		pq = new int[NMAX];
		qp = new int[NMAX];
		keys = (Key[]) new Comparable[NMAX];
		for (int i = 0 ; i < NMAX ; qp[i++] = -1);
	}

	/**
	 * Whether the priority queue is empty
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return N == 0;
	}

	/**
	 * Does the priority queue contains the index i ?
	 * @param i an index
	 * @throws java.lang.IndexOutOfBoundsException if the specified index is invalid
	 * @return true if i is on the priority queue, false if not
	 */
	public boolean contains(int i) {
		if (i < 0 ||i >= NMAX) throw new IndexOutOfBoundsException();
		return qp[i] != -1;
	}

	/**
	 * Number of elements currently on the priority queue
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		return N;
	}

	/**
	 * Associates a key with an index
	 * @param i an index
	 * @param key a Key associated with i
	 * @throws java.lang.IndexOutOfBoundsException if the specified index is invalid
	 * @throws java.util.IllegalArgumentException if the index is already in the queue
	 */
	public void insert(int i, Key key) {
		if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
		if (contains(i)) throw new IllegalArgumentException("Index already there");
		keys[i] = key;
		pq[N] = i;
		qp[i] = N;
		swim(N++);
	}

	/**
	 * Get the index associated with the minimum key
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the index associated with the minimum key
	 */
	public int minIndex() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return pq[0];
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
	 * @return the index associated with the minimum key
	 */
	public int delMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		int min = pq[0];
		exch(0, --N);
		sink(0);
		qp[min] = -1;
		keys[pq[N]] = null;
		pq[N] = -1;
		return min;
	}

	/**
	 * Get the key associated with index i
	 * @param i an index
	 * @throws java.lang.IndexOutOfBoundsException if the specified index is invalid
	 * @throws java.util.IllegalArgumentException if the index is not in the queue
	 * @return the key associated with index i
	 */
	public Key keyOf(int i) {
		if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
		if (! contains(i)) throw new NoSuchElementException("Specified index is not in the queue");
		return keys[i];
	}

	/**
	 * Changes the key associated with index i to the given key
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IndexOutOfBoundsException if the specified index is invalid
	 * @throws java.util.IllegalArgumentException if the index has no key associated with
	 */
	public void changeKey(int i, Key key) {
		if (i < 0 || i >=NMAX) throw new IndexOutOfBoundsException();
		if (! contains(i)) throw new NoSuchElementException("Specified index is not in the queue");
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
	}

	/**
	 * Decreases the key associated with index i to the given key
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IndexOutOfBoundsException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index has no key associated with
	 * @throws java.util.IllegalArgumentException if the given key is greater than the current key
	 */
	public void decreaseKey(int i, Key key) {
		if (i < 0 || i >=NMAX) throw new IndexOutOfBoundsException();
		if (! contains(i)) throw new NoSuchElementException("Specified index is not in the queue");
		if (keys[i].compareTo(key) <= 0) throw new IllegalArgumentException("Calling with this argument would not decrease the Key");
		keys[i] = key;
		swim(qp[i]);
	}

	/**
	 * Increases the key associated with index i to the given key
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IndexOutOfBoundsException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index has no key associated with
	 * @throws java.util.IllegalArgumentException if the given key is lower than the current key
	 */
	public void increaseKey(int i, Key key) {
		if (i < 0 || i >=NMAX) throw new IndexOutOfBoundsException();
		if (! contains(i)) throw new NoSuchElementException("Specified index is not in the queue");
		if (keys[i].compareTo(key) >= 0) throw new IllegalArgumentException("Calling with this argument would not increase the Key");
		keys[i] = key;
		sink(qp[i]);
	}

	/**
	 * Deletes the key associated the given index
	 * @param i an index
	 * @throws java.lang.IndexOutOfBoundsException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the given index has no key associated with
	 */
	public void delete(int i) {
		if (i < 0 || i >= NMAX) throw new IndexOutOfBoundsException();
		if (! contains(i)) throw new NoSuchElementException("Specified index is not in the queue");
		int idx = qp[i];
		exch(idx, --N);
		swim(idx);
		sink(idx);
		keys[i] = null;
		qp[i] = -1;
	}
	
	/***************************
	 * General helper functions
	 **************************/
	
	private boolean greater(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	}
	
	private void exch(int i, int j) {
		int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
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
	 * Iterator
	 **************************/
	
	/**
	 * Get an Iterator over the indexes in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * @return an Iterator over the indexes in the priority queue in ascending order
	 */
	
	public Iterator<Integer> iterator() {
		return new MyIterator();
	}
	
	//Constructor an Iterator over the indices in linear time
	private class MyIterator implements Iterator<Integer> {
		IndexDaryMinPQ<Key> clone;
		
		public MyIterator() {
			clone = new IndexDaryMinPQ<Key>(NMAX, D);
			for (int i = 0 ; i < N ; i++) {
				clone.insert(pq[i], keys[pq[i]]);
			}
		}

		public boolean hasNext() {
			return !clone.isEmpty();
		}
		
		public Integer next() {
			return clone.delMin();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}