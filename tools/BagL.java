package tools;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class BagL<Item> implements Iterable<Item> {
	private Node first;
	private int size;
	
	private class Node {
		Item item;
		Node next;
	}
	
	public BagL() {
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void add(Item item) {
		Node oldf = first;
		first = new Node();
		first.item = item;
		first.next = oldf;
		size++;
	}
	
	public Item remove(Item item) {
		if (isEmpty()) return null;
		Node prev = first;
		Node cur = first.next;
		if (first.item.equals(item)) {
			first = first.next;
			return prev.item;
		}
		while(cur != null && (!cur.item.equals(item))) {
			prev = cur;
			cur = cur.next;
		}
		if (cur == null) throw new NoSuchElementException();
		prev.next = cur.next;
		return cur.item;
	}
	
	public Iterator<Item> iterator() {
		Iterator<Item> mi = new MyIterator(first);
		return mi;
	}
	
	public Item pop() {
		if (isEmpty()) throw new NoSuchElementException();
		Item res = first.item;
		first = first.next;
		size--;
		return res;
	}
	
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (Item i : this) {
			res.append(i+", ");
		}
		return res.toString();
	}
	
	private class MyIterator implements Iterator<Item> {
		Node probe;
		
		public MyIterator(Node cur) {
			probe = cur;
		}
		
		public boolean hasNext() {
			return probe != null;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item it = probe.item;
			probe = probe.next;
			return it;
		}
	}
	
	public static void main(String[] args) {
		BagL<Integer> bag = new BagL<>();
		Deque<Integer> d = new LinkedList<Integer>();
		Chrono ch = new Chrono();
		for (int i = 0 ; i < 1000000 ; i++) {
			bag.add(i);
		}
		System.out.println("1000000 insert bag : "+ch.elapsedTime());
		ch.reset();
		for (int i = 0 ; i < 1000000 ; i++) {
			d.add(i);
		}
		System.out.println("1000000 insert list : "+ch.elapsedTime());
		ch.reset();
		for (int i : bag);
		System.out.println("iterator 1000000 elts bag : "+ch.elapsedTime());
		ch.reset();
		for (int i : d);
		System.out.println("iterator 1000000 elts : list "+ch.elapsedTime());
		ch.reset();
		for (int i = 0 ; i < 100 ; i++) {
			if (bag.remove(i) == null) System.out.println("PB "+i);
		}
		System.out.println("1000000 remove bag : "+ch.elapsedTime());
		ch.reset();
		for (int i = 0 ; i < 100 ; i++) {
			d.removeFirstOccurrence(i);
		}
		System.out.println("1000000 remove list : "+ch.elapsedTime());
	}

}