package tools;
import java.util.Iterator;

public class DeckL<Item> implements Deck<Item> {
	private Node first, last;
	private int size;
	
	private class Node {
		Node previous, next;
		Item item;
		
		public Node() {}
		
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
	
	public DeckL() {
		size = 0;
	}
	
	public boolean isEmpty() { return size == 0; }
	
	public int size() { return size; }
	
	public void addFirst(Item item) {
		Node newn = new Node(item);
		size++;
		if (isEmpty()) {
			first = newn;
			last = newn;
		} else {
			first.previous = newn;
			newn.next = first;
			first = newn;
		}
	}
	
	public void addLast(Item item) {
		Node newn = new Node(item);
		size++;
		if (isEmpty()) {
			first = newn;
			last = newn;
		} else {
			last.next = newn;
			newn.previous = last;
			last = newn;
		}
	}
	
	public Item removeFirst() {
		if (isEmpty()) return null;
		Item ret = first.item;
		size--;
		if (size == 1) {
			first = null;
			last = null;
		} else {
			Node temp = first.previous;
			first.previous = null;
			temp.next = null;
			first = temp;
		}
		return ret;
	}
	
	public Item removeLast() {
		if (isEmpty()) return null;
		Item ret = last.item;
		size--;
		if (size == 1) {
			first = null;
			last = null;
		} else {
			Node temp = last.next;
			last.next = null;
			temp.previous = null;
			last = temp;
		}
		return ret;
	}
	
	public Iterator<Item> iterator() {
		Iterator<Item> it = new MyIterator();
		return it;
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
	
	public static void main(String[] args) {
		DeckA<Integer> DA = new DeckA<>();
		System.out.println(DA);
		DA.addLast(0);
		System.out.println(DA);
		DA.addLast(1);
		System.out.println(DA);
		DA.addLast(1);
		System.out.println(DA);
		DA.addLast(1);
		System.out.println(DA);
		DA.addLast(1);
		System.out.println(DA);
		DA.addLast(1);
		System.out.println(DA);
		DA.addLast(1);
		System.out.println(DA);
		DA.addFirst(2);
		System.out.println(DA);
		DA.addFirst(2);
		System.out.println(DA);
		DA.addFirst(2);
		System.out.println(DA);
		DA.addFirst(2);
		System.out.println(DA);
		DA.addFirst(2);
		System.out.println(DA);
		DA.addFirst(2);
		System.out.println(DA);
		DA.addFirst(2);
		System.out.println(DA);
		DA.removeLast();
		System.out.println(DA);
		DA.removeLast();
		System.out.println(DA);
		DA.removeLast();
		System.out.println(DA);
		DA.removeLast();
		System.out.println(DA);
		DA.removeFirst();
		System.out.println(DA);
		DA.removeFirst();
		System.out.println(DA);
		DA.removeFirst();
		System.out.println(DA);
		DA.removeFirst();
		System.out.println(DA);
		DA.removeFirst();
		System.out.println(DA);
		for(Integer i : DA) {
			System.out.println(i);
		}
	}

}
