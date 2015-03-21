package tools;

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *  The BinomMinPQ class represents a priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations, 
 *  along with the merging of two heaps together.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  It is possible to build the priority queue using a Comparator.
 *  If not, the natural order relation between the keys will be used.
 *  
 *  This implementation uses a binomial heap.
 *  The insert, delete-the-minimum, union, min-key
 *  and size operations take logarithmic time.
 *  The is-empty and constructor operations take constant time.
 * 
 *  @author Tristan Claverie
 */

public class BinomialMinPQ<Key extends Comparable<Key>> implements Iterable<Key> {
	private Node head;    				//head of the list of roots
	private final Comparator<Key> COMP;	//A Comparator over the keys
	
	//Represents a node of a Binomial Tree
	private class Node {
		Key key;					//Key contained by the Node
		int order;					//The order of the Binomial Tree rooted by this Node
		Node child, sibling;		//child and sibling of this Node
	}
	
	/**
	 * Initializes an empty priority queue
	 */
	public BinomialMinPQ() {
		COMP = new MyComparator();
	}
	
	/**
	 * Initializes an empty priority queue
	 * @param Comp a comparator over the keys
	 */
	public BinomialMinPQ(Comparator<Key> Comp) {
		COMP = Comp;
	}
	
	/**
	 * Initializes a priority queue with given keys
	 * @param a an array of keys
	 */
	public BinomialMinPQ(Key[] a) {
		COMP = new MyComparator();
		for (Key k : a) insert(k);
	}
	
	/**
	 * Initializes a priority queue with given keys
	 * @param Comp a comparator over the keys
	 * @param a an array of keys
	 */
	public BinomialMinPQ(Comparator<Key> Comp, Key[] a) {
		COMP = Comp;
		for (Key k : a) insert(k);
	}

	/**
	 * Whether the priority queue is empty
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Number of elements currently on the priority queue, takes logarithmic time
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		int result = 0, tmp;
		Node node = head;
		for (int i = 0 ; i < 32 && node != null ; i++) {
			tmp = (node.order == i) ? 1 << i: 0;
			node = (node.order == i) ? node.sibling : node;
			result |= tmp;
		}
		return result;
	}

	/**
	 * Put a Key in the heap
	 * @param key a Key
	 */
	public void insert(Key key) {
		Node x = new Node();
		x.key = key;
		x.order = 0;
		BinomialMinPQ<Key> H = new BinomialMinPQ<>();
		H.head = x;
		this.head = this.union(H).head;
	}

	/**
	 * Get the minimum key currently in the queue
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node min = head;
		Node current = head;
		while (current.sibling != null) {
			min = (greater(min.key, current.sibling.key)) ? current : min;
			current = current.sibling;
		}
		return min.key;
	}

	/**
	 * Delete the minimum key
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key
	 */
	
	public Key delMin() {
		if(isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		Node min = eraseMin();
		Node x = (min.child == null) ? min : min.child;
		if (min.child != null) {
			min.child = null;
			Node prevx = null, nextx = x.sibling;
			while (nextx != null) {
				x.sibling = prevx;
				prevx = x;
				x = nextx;nextx = nextx.sibling;
			}
			x.sibling = prevx;
			BinomialMinPQ<Key> H = new BinomialMinPQ<>();
			H.head = x;
			head = union(H).head;
		}
		return min.key;
	}
	
	/**
	 * Merge two Binomial heaps together in logarithmic time
	 * @throws java.util.IllegalArgumentException if the heap in parameter is null
	 * @return the union of two heaps
	 */
	
	public BinomialMinPQ<Key> union(BinomialMinPQ<Key> heap) {
		if (heap == null) throw new IllegalArgumentException("Cannot merge a Binomial Heap with null");
		BinomialMinPQ<Key> H = new BinomialMinPQ<>();
		H.head = merge(new Node(), this.head, heap.head).sibling;
		Node x = H.head;
		Node prevx = null, nextx = x.sibling;
		while (nextx != null) {
			if (x.order < nextx.order ||
			   (nextx.sibling != null && nextx.sibling.order == x.order)) {
				prevx = x; x = nextx;
			} else if (greater(nextx.key, x.key)) {
				x.sibling = nextx.sibling;
				link(nextx, x);
			} else {
				if (prevx == null) { H.head = nextx; }
				else { prevx.sibling = nextx; }
				link(x, nextx);
				x = nextx;
			}
			nextx = x.sibling;
		}
		return H;
	}
	
	/*************************************************
	 * General helper functions
	 ************************************************/
	
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return COMP.compare(n, m) > 0;
	}
	
	//Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(Node root1, Node root2) {
		root1.sibling = root2.child;
		root2.child = root1;
		root2.order++;
	}
	
	//deletes and return the node containing the minimum key
	private Node eraseMin() {
		Node min = head;
		Node previous = new Node();
		Node current = head;
		while (current.sibling != null) {
			if (greater(min.key, current.sibling.key)) {
				previous = current;
				min = current.sibling;
			}
			current = current.sibling;
		}
		previous.sibling = min.sibling;
		if (min == head) head = min.sibling;
		return min;
	}
	
	/**************************************************
	 * Functions for inserting a key in the heap
	 *************************************************/
	
	//Merges two root lists into one, there can be up to 2 Binomial Trees of same order
	private Node merge(Node h, Node x, Node y) {
		if (x == null && y == null) return h;
		if (x == null) 				h.sibling = merge(y, x, y.sibling);
		else if (y == null) 		h.sibling = merge(x, x.sibling, y);
		else if (x.order < y.order) h.sibling = merge(x, x.sibling, y);
		else 						h.sibling = merge(y, x, y.sibling);
		return h;
	}
	
	/******************************************************************
	 * Iterator
	 *****************************************************************/
	
	/**
	 * Get an Iterator over the keys in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * @return an Iterator over the keys in the priority queue in ascending order
	 */
	
	public Iterator<Key> iterator() {
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<Key> {
		BinomialMinPQ<Key> data;
		
		//Constructor clones recursively the elements in the queue
		//It takes linear time
		public MyIterator() {
			data = new BinomialMinPQ<Key>();
			data.head = clone(head, false, false, null);
		}
		
		private Node clone(Node x, boolean isParent, boolean isChild, Node parent) {
			if (x == null) return null;
			Node node = new Node();
			node.key = x.key;
			node.sibling = clone(x.sibling, false, false, parent);
			node.child = clone(x.child, false, true, node);
			return node;
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