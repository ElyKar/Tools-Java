package tools;
import java.util.Iterator;

public class QueueL<Item> implements Queue<Item> {
	Node first, last;
	int size;
	
	private class Node {
		Item item;
		Node next, previous;
		
		public Node(){}
		
		public Node(Item i) {
			item = i;
		}
	}
	
	private class MyIterator implements Iterator<Item>{
		Node probe;
		
		public MyIterator() {
			Node a = new Node();
			a.previous = last;
			probe = a;
		}
		
		public boolean hasNext() {
			return probe.previous != null;
		}
		
		public Item next() {
			if (! hasNext()) return null;
			probe = probe.previous;
			return probe.item;
		}
		
		public void remove() {
			next();
		}
	}
	
	public QueueL() {
		size = 0;
	}
	
	public boolean isEmpty() { return size == 0; }
	
	public int size() { return size; }
	
	public void enqueue(Item item) {
		Node newn = new Node(item);
		if (size == 0) {
			first = newn;
			last = newn;
		}else if (size == 1) {
			first = newn;
			first.next = last;
			last.previous = first;
		} else {
		newn.next = first;
		first.previous = newn;
		first = newn;
		}
		size++;
	}
	
	public Item dequeue() {
		if (size == 0) return null;
		Item ret = last.item;
		Node temp = last.previous;
		last.previous.next = null;
		last.previous = null;
		last = temp;
		size--;
		return ret;
	}
	
	public Iterator<Item> iterator() {
		if (size == 0) return null;
		return new MyIterator();
	}
	
	public boolean notIn(Item test) {
		if (this.size() == 0) {return true;}
		for (Item item : this) {
			if (item.equals(test)) {return false; }
		}
		return true;
	}
	
	public String toString() {
		if (size==0) return null;
		String s = "size "+this.size+" : ";
		Iterator<Item> mi = iterator();
		while(mi.hasNext()) {
			s = s+mi.next().toString()+" ";
		}
		return s;
	}
	
	public static void main(String[] args){
		QueueL<Integer> SA = new QueueL<>();
		for (int i = 0 ; i < 20 ; i++) {
			SA.enqueue(i);
		}
		System.out.println(SA);
		for (Integer i : SA) {
			System.out.println(i);
		}
	}

}
