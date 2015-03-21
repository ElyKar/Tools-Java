package tools;

public class LinkedList<Item> {
	private Node root;
	private int size;
	
	private class Node {
		Item item;
		Node previous;
		
		public Node(Item i) {
			item = i;
		}
	}
	
	public LinkedList() {
		size = 0;
	}
	
	public void insert(Item item) {
		if (item == null) return;
		Node newn = new Node(item);
		newn.previous = root;
		root = newn;
		size++;
	}
	
	public Item remove() {
		if (root == null) return null;
		Item ret = root.item;
		root = root.previous;
		size--;
		return ret;
	}
	
	public Item remove (int i) {
		if (root == null) return null;
		if (size < i) return null;
		Node probe = root;
		for (int j = 0 ; j < i-1 ; j++) {
			probe = probe.previous;
		}
		Node temp = probe.previous;
		Item ret = temp.item;
		probe.previous = probe.previous.previous;
		temp = null;
		size--;
		return ret;
	}
	
	public Item get(int i) {
		if (root == null) return null;
		if (size < i) return null;
		Node probe = root;
		for (int j = 0 ; j < i; j++, probe = probe.previous);
		Item ret = probe.item;
		return ret;
	}
	
	public String toString() {
		if (root == null) return null;
		String s = "";
		for (Node probe = root ; probe != null ; probe = probe.previous) {
			s = s+probe.item.toString()+" ";
		}
		return s;
	}
	
	public static void main(String[] args) {
		Chrono s = new Chrono();
		LinkedList<Integer> RA = new LinkedList<>();
		for (int i = 0 ; i < 1000000 ; i++) {
			RA.insert(i);
		}
		System.out.println("1000000 insert en : " + s.elapsedTime());
		s.reset();
		for (int i = 0 ; i < 100000 ; i++) {
			RA.get(i);
		}
		System.out.println("1000000 get en : " + s.elapsedTime());
		s.reset();
		for (int i = 0 ; i < 1000000 ; i++) {
			RA.remove();
		}
		System.out.println("1000000 remove en : " + s.elapsedTime());
	}
	/*1000000 insert en : 0.902
1000000 get en : 47.474
1000000 remove en : 0.046*/

}

/*16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0 
16
15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0 
14
15 13 12 11 10 9 8 7 6 5 4 3 2 1 0 
null
15 13 12 11 10 9 8 7 6 5 4 3 2 1 0 
0
15 13 12 11 10 9 8 7 6 5 4 3 2 1 
9
15 13 12 11 10 8 7 6 5 4 3 2 1 */
