package tools;
import java.util.Iterator;

/* Mémoire :
 * Overhead --> 16
 * array --> 32N + 48 (8N + 48)
 * size --> 4
 * Padding --> 4
 * Total : 32N + 72 (8N + 72)
 */
public class StackA<Item> implements Stack<Item> {
	private ResizingArray<Item> array;
	private int size;
	
	private class MyIterator implements Iterator<Item> {
		int current;
		Item[] RA;
		int size;
		
		public MyIterator(Item[] i, int s) {
			current = s;
			RA = i;
			size = s;
		}
		
		public boolean hasNext() {
			return current > 0;
		}
		
		public Item next() {
			Item item = RA[--current];
			return item;
		}
		
		public void remove() {
			int N = RA.length;
			if (N == 0) return;
			RA[--N] = null;
			size--;
		}
	}
	
	public StackA() {
		array = new ResizingArray<Item>();
		size = 0;
	}
	
    public boolean isEmpty() { return size == 0; }
    
	public int size() { return size; }
	
	public void push(Item item) {
		array.insert(item);
		size++;
	}
	
	public void merge(StackA<Item> stack) {
		for (Item i : stack) {
			this.push(i);
		}
	}
	
	public Item pop() {
		Item it = array.remove();
		size--;
		return it;
	}
	
	public Iterator<Item> iterator() {
		Item[] tab = (Item[]) new Object[size];
		for(int i = 0 ; i < size ; i++) {
			tab[i] = array.get(i);
		}
		MyIterator MI = new MyIterator(tab, size);
		return MI;
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
		StackA<Integer> SA = new StackA<>();
		for (int i = 0 ; i < 20 ; i++) {
			SA.push(i);
		}
		for (Integer i : SA) {
			System.out.println(i);
		}
		System.out.println(SA);
	}

}
