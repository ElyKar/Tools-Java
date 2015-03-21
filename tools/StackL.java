package tools;
import java.util.Iterator;

public class StackL<Item> implements Stack<Item>{
	private Node root;
	private int size;
	
	private class Node {
		Item item;
		Node previous;
		
		public Node(){}
		
		public Node(Item i) {
			item = i;
		}
	}
	
	private class MyIterator implements Iterator<Item>{
		Node probe;
		
		public MyIterator() {
			Node a = new Node();
			a.previous = root;
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
			probe = probe.previous;
		}
	}
	
	public StackL() {
		size = 0;
	}
	
	public int size() { return size; }
	
	public boolean isEmpty() { return size ==0; }
	
	public void push(Item item) {
		if (item == null) return;
		Node newn = new Node(item);
		newn.previous = root;
		root = newn;
		size++;
	}
	
	public Item pop() {
		if (root == null) return null;
		Item ret = root.item;
		root = root.previous;
		size--;
		return ret;
	}
	
	public Iterator<Item> iterator() {
		if (root == null) return null;
		return new MyIterator();
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
		StackL<Integer> SA = new StackL<>();
		for (int i = 0 ; i < 20 ; i++) {
			SA.push(i);
		}
		for (Integer i : SA) {
			System.out.println(i);
		}
		System.out.println(SA);
	}
}