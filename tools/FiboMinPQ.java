package tools;

import java.util.Iterator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Comparator;


/**
 *  TheFiboMinPQ class represents a priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations, 
 *  along with the merging of two heaps together.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  It is possible to build the priority queue using a Comparator.
 *  If not, the natural order relation between the keys will be used.
 *  
 *  This implementation uses a Fibonacci heap.
 *  The delete-the-minimum operation takes amortized logarithmic time.
 *  The insert, min-key, is-empty, size, union and constructor take constant time.
 *
 *  @author Tristan Claverie
 */
public class FiboMinPQ<Key extends Comparable<Key>> implements Iterable<Key> {
	private Node head;					//head of the circular root list
	private Node min;					//Minimum Node of the root list
	private int size;					//Number of keys in the heap
	private final Comparator<Key> COMP;	//A Comparator over the keys
	private HashMap<Integer, Node> table = new HashMap<>(); //Used for the consolidate operation
	
	//Represents a Node of a tree
	private class Node {
		Key key;			//Key of this Node
		int order;			//Order of the tree rooted by this Node
		Node prev, next;	//Siblings of this Node
		Node child;			//Child of this Node
	}
	
	/**
	 * Initializes an empty priority queue
	 * @param Comp a Comparator over the Keys
	 */
	public FiboMinPQ(Comparator<Key> Comp) {
		COMP = Comp;
	}
	
	/**
     * Initializes an empty priority queue
     */
	public FiboMinPQ() {
		COMP = new MyComparator();
	}
	
	/**
	 * Initializes a priority queue with given keys
	 * @param a an array of keys
	 */
	public FiboMinPQ(Key[] a) {
		COMP = new MyComparator();
		for (Key k : a) insert(k);
	}
	
	/**
	 * Initializes a priority queue with given keys
	 * @param Comp a comparator over the keys
	 * @param a an array of keys
	 */
	public FiboMinPQ(Comparator<Key> Comp, Key[] a) {
		COMP = Comp;
		for (Key k : a) insert(k);
	}

	/**
	 * Whether the priority queue is empty
	 * @return true if the priority queue is empty, false if not
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Number of elements currently on the priority queue, takes constant time
	 * @return the number of elements on the priority queue
	 */
	public int size() {
		return size;
	}

	/**
	 * Insert a key in the queue
	 * @param key a Key
	 */
	public void insert(Key key) {
		Node x = new Node();
		x.key = key;
		size++;
		head = insert(x, head);
		if (min == null) min = head;
		else 			 min = (greater(min.key, key)) ? head : min;
	}

	/**
	 * Get the minimum key currently in the queue
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	public Key minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return min.key;
	}

	/**
	 * Delete the minimum key
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key
	 */
	public Key delMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		head = cut(min, head);
		Node x = min.child;
		Key key = min.key;
		min.key = null;
		if (x != null) {
			head = meld(head, x);
			min.child = null;
		}
		size--;
		if (!isEmpty()) consolidate();
		else 			min = null;
		return key;
	}
	
	/**
	 * Merge two heaps together
	 * @param that a Fibonacci heap
	 * @return the union of the two heaps
	 */
	public FiboMinPQ<Key> union(FiboMinPQ<Key> that) {
		FiboMinPQ<Key> pq = new FiboMinPQ<>(COMP);
		pq.head = meld(head, that.head);
		pq.min = (greater(this.min.key, that.min.key)) ? that.min : this.min;
		pq.size = this.size+that.size;
		return pq;
	}
	
	/*************************************
	 * General helper functions
	 ************************************/
	
	private boolean greater(Key n, Key m) {
		if (n == null) return false;
		if (m == null) return true;
		return COMP.compare(n,m) > 0;
	}
	
	//Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(Node root1, Node root2) {
		root2.child = insert(root1, root2.child);
		root2.order++;
	}
	
	/*************************************
	 * Function for consolidating all trees in the root list
	 ************************************/
	
	private void consolidate() {
		table.clear();
		Node x = head;
		int maxOrder = 0;
		min = head;
		Node y = null; Node z = null;
		do {
			y = x;
			x = x.next;
			z = table.get(y.order);
			while (z != null) {
				table.remove(y.order);
				if (greater(y.key, z.key)) {
					link(y, z);
					y = z;
				} else {
					link(z, y);
				}
				z = table.get(y.order);
			}
			table.put(y.order, y);
			if (y.order > maxOrder) maxOrder = y.order;
		} while (x != head);
		head = null;
		for (Node n : table.values()) {
			if (n != null) {
				if (min.key.compareTo(n.key) >= 0) min = n;
				head = insert(n, head);
			}
		}
	}
	
	/*************************************
	 * General helper functions for manipulating circular lists
	 ************************************/
	
	//insert a Node in a circular list containing head, returns a new head
	private Node insert(Node x, Node head) {
		if (head == null) {
			x.prev = x;
			x.next = x;
		} else {
			head.prev.next = x;
			x.next = head;
			x.prev = head.prev;
			head.prev = x;
		}
		return x;
	}
	
	//Removes a tree from the list defined by the head pointer
	private Node cut(Node x, Node head) {
		if (x.next == x) {
			x.next = null;
			x.prev = null;
			return null;
		} else {
			x.next.prev = x.prev;
			x.prev.next = x.next;
			Node res = x.next;
			x.next = null;
			x.prev = null;
			if (head == x)  return res;
			else 			return head;
		}
	}
	
	//We assume both lists are different and belongs to the same heap,
	//so we don't have to update the min pointer
	private Node meld(Node x, Node y) {
		if (x == null) return y;
		if (y == null) return x;
		x.prev.next = y.next;
		y.next.prev = x.prev;
		x.prev = y;
		y.next = x;
		return x;
	}
	
	/*************************************
	 * Iterator
	 ************************************/
	
	/**
	 * Get an Iterator over the Keys in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * @return an Iterator over the Keys in the priority queue in ascending order
	 */
	
	public Iterator<Key> iterator() {
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<Key> {
		private FiboMinPQ<Key> copy;
		
		
		//Constructor takes linear time
		public MyIterator() {
			copy = new FiboMinPQ<>();
			process(head);
		}
		
		private void process(Node head) {
			if (head == null) return;
			Node x = head;
			do {
				copy.insert(x.key);
				process(x.child);
				x = x.next;
			} while (x != head);
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public boolean hasNext() {
			return !copy.isEmpty();
		}
		
		//Takes amortized logarithmic time
		public Key next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMin();
		}
	}
	
	/*************************************
	 * Comparator
	 ************************************/
	
	//default Comparator
	private class MyComparator implements Comparator<Key> {
		@Override
		public int compare(Key key1, Key key2) {
			return key1.compareTo(key2);
		}
	}
}