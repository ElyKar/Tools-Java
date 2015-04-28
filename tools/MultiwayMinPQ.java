package tools;

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *  The MultiwayMinPQ class represents a priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  It is possible to build the priority queue using a Comparator.
 *  If not, the natural order relation between the keys will be used.
 *  
 *  This implementation uses a multiway heap.
 *  For simplified notations, logarithm in base d will be referred as log-d
 *  The delete-the-minimum operation takes time proportional to d*log-d(n)
 *  The insert takes time proportional to log-d(n)
 *  The is-empty, min-key and size operations take constant time.
 *  Constructor takes time proportional to the specified capacity.
 *
 *  @author Tristan Claverie
 */

public class MultiwayMinPQ<Key> implements Iterable<Key> {
	private final int d; 				//Dimension of the heap
	private int n;						//Number of keys currently in the heap
	private int order;					//Number of levels of the tree
	private Key[] keys;					//Array of keys
	private final Comparator<Key> comp;	//Comparator over the keys
	
	
	/**
     * Initializes an empty priority queue
     * Runs in O(d)
     * @param D dimension of the heap
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public MultiwayMinPQ(int D) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = D;
		order = 1;
		keys = (Key[]) new Comparable[D << 1];
		comp = new MyComparator();
	}
	
	/**
     * Initializes an empty priority queue
     * Runs in O(d)
     * @param D dimension of the heap
     * @param C a Comparator over the keys
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public MultiwayMinPQ(int D, Comparator<Key> C) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = D;
		order = 1;
		keys = (Key[]) new Comparable[D << 1];
		comp = C;
	}
	
	/**
     * Initializes a priority queue with given indexes
     * Runs in O(n*log-d(n))
     * @param D dimension of the heap
     * @param a an array of keys
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public MultiwayMinPQ(int D, Key[] a) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = D;
		order = 1;
		keys = (Key[]) new Comparable[D << 1];
		comp = new MyComparator();
		for (Key k : a) insert(k);
	}
	
	/**
     * Initializes a priority queue with given indexes
     * Runs in O(a*log-d(n))
     * @param D dimension of the heap
     * @param C a Comparator over the keys
     * @param a an array of keys
     * @throws java.lang.IllegalArgumentException if D < 2
     */
	public MultiwayMinPQ(int D, Comparator<Key> C, Key[] a) {
		if (D < 2) throw new IllegalArgumentException("Dimension should be 2 or over");
		this.d = D;
		order = 1;
		keys = (Key[]) new Comparable[D << 1];
		comp = C;
		for (Key k : a) insert(k);
	}

	/**
	 * Whether the priority queue is empty
	 * Runs in O(1)
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return n == 0;
	}

	/**
	 * Number of elements currently on the priority queue
	 * Runs in O(1)
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		return n;
	}

	/**
	 * Add a Key to the priority queue
	 * Runs in O(log-d(n))
	 * @param key a Key
	 */
	public void insert(Key key) {
		keys[n+d] = key;
		swim(n++);
		if (n == keys.length-d) {
			resize(getN(order+1)+d);
			order++;
		}
	}

	/**
	 * Get the minimum key currently in the queue
	 * Runs in O(1)
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return keys[d];
	}

	/**
	 * Delete the minimum key
	 * Runs in O(d*log-d(n))
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key
	 */
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		exch(0, --n);
		sink(0);
		Key min = keys[n+d];
		keys[n+d] = null;
		int number = getN(order-2);
		if(order > 1 && n == number)  {
			resize(number+(int)Math.pow(d, order-1)+d);
			order--;
		}
		return min;
	}
	
	/***************************
	 * General helper functions
	 **************************/
	
	//Compares two keys
	private boolean greater(int x, int y) {
		int i = x+d, j = y+d;
		if (keys[i] == null) return false;
		if (keys[j] == null) return true;
		return comp.compare(keys[i], keys[j]) > 0;
	}
	
	//Exchange the position of two keys
	private void exch(int x, int y) {
		int i = x+d, j = y+d;
		Key swap = keys[i];
		keys[i] = keys[j];
		keys[j] = swap;
	}
	
	//Get the maximum number of keys in the heap, given the number of levels of the tree
	private int getN(int order) {
		return (1-((int)Math.pow(d, order+1)))/(1-d);
	}
	
	/***************************
	 * Functions for moving upward or downward
	 **************************/
	
	//Moves upward
	private void swim(int i) {
		if (i > 0 && greater((i-1)/d, i)) {
			exch(i, (i-1)/d);
			swim((i-1)/d);
		}
	}
	
	//Moves downward
	private void sink(int i) {
		int child = d*i+1;
		if (child >= n) return;
		int min = minChild(i);
		while (min < n && greater(i, min)) {
			exch(i, min);
			i = min;
			min = minChild(i);
		}
	}
	
	/***************************
	 * Deletes the minimum child
	 **************************/
	
	//Return the minimum of the childs starting at position i
	private int minChild(int i) {
		int loBound = d*i+1, hiBound = d*i+d;
		int min = loBound;
		for (int cur = loBound; cur <= hiBound; cur++) {
			if (cur < n && greater(min, cur)) min = cur;
		}
		return min;
	}
	
	/***************************
	 * Resize the priority queue
	 **************************/
	
	//Resizes the array containing the keys
	//If the heap is full, it adds one floor
	//If the heap has two floors empty, it removes one
	private void resize(int N) {
		Key[] array = (Key[]) new Comparable[N];
		for (int i = 0; i < Math.min(keys.length, array.length); i++) {
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
	 * iterator() : Runs in O(n)
	 * next() : Runs in O(d*log-d(n))
	 * hasNext() : Runs in O(1)
	 * @return an Iterator over the keys in the priority queue in ascending order
	 */
	
	public Iterator<Key> iterator() {
		return new MyIterator();
	}
	
	//Constructs an Iterator over the keys in linear time
	private class MyIterator implements Iterator<Key> {
		MultiwayMinPQ<Key> data;
		
		public MyIterator() {
			data = new MultiwayMinPQ<Key>(d, comp);
			data.keys = (Key[]) new Comparable[keys.length];
			data.n = n;
			for (int i = 0; i < keys.length; i++) {
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
			return ((Comparable<Key>) key1).compareTo(key2);
		}
	}
	
	public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        MultiwayMinPQ<String> pq = new MultiwayMinPQ<>(4);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(strings[i]);
        }

        // delete and print each key
        while (!pq.isEmpty()) {
        	System.out.println(pq.minKey());
            System.out.println(pq.delMin());
        }
        System.out.println();

        // reinsert the same strings
        for (int i = 0; i < strings.length; i++) {
            pq.insert(strings[i]);
        }

        // print each key using the iterator
        for (String s : pq) {
            System.out.println(s);
        }
        while (!pq.isEmpty()) {
            pq.delMin();
        }

    }
	
}