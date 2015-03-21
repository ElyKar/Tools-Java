package tools;
import java.util.Iterator;


public class QueueA<Item> implements Queue<Item> {
	private Item[] array;
	private int size;
	private int first;
	private int last;
	
	private class MyIterator<Item> implements Iterator<Item> {
		Item[] array;
		int size;
		int current;
		
		public MyIterator(Item[] i, int s) {
			current = -1;
			array = i;
			size = s;
		}
		
		public boolean hasNext() {
			return current+1 < size;
		}
		
		public Item next() {
			Item item = array[++current];
			return item;
		}
		
		public void remove() {
			int N = array.length;
			if (N == 0) return;
			array[--N] = null;
			size--;
		}
	}
	
	public QueueA() {
		array = (Item[]) (new Object[2]);
		size = 0;
		first = 0;
		last = 0;
	}
	
	public Item get(int i) {
		if (i<0) return null;
		if (size == 0) return null;
		if (i >= size-1) return null;
		return array[i];
	}
	
	public boolean isEmpty() { return size == 0; }
	
	public int size(){ return size; }
	
	public void enqueue(Item item) {
		array[last] = item;
		last++;
		size++;
		int N = array.length;
		if (size == N) {
			resize(2*N);
			return;
		}
		if (last == N) {
			resize(N);
			return;
		}
	}
	
	private void resize(int n) {
		Item[] tab = (Item[]) new Object[n];
		for (int i = first, j = 0 ; i < last ; i++, j++) {
			tab[j] = array[i];
		}
		array = tab;
		first  = 0;
		last = size;
	}
		
	
	public Item dequeue() {
		if (size == 0) return null;
		Item ret = array[first];
		array[first++] = null;
		if (size == 1) {
			first = 0;
			last = 0;
		}
		size--;
		if (size*4 == array.length){ 
			resize(size*2);
		}
		return ret;
	}
	
	public Iterator<Item> iterator() {
		if (size == 0) return null;
		Item[] tab = (Item[]) new Object[size];
		for (int i = first, j = 0 ; j < size ; i++, j++) {
			tab[j] = array[i];
		}
		Iterator<Item> mi = new MyIterator(tab, size);
		return mi;
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
		QueueA<Integer> SA = new QueueA<>();
		for (int i = 0 ; i < 20 ; i++) {
			SA.enqueue(i);
		}
		for (Integer i : SA) {
			System.out.println(i);
		}
		System.out.println(SA);
	}
}
