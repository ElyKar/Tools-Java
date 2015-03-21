package tools;
import java.util.Iterator;

public class DeckA<Item> implements Deck<Item> {
	Item[] array;
	int size, first, last;
	
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
	
	public DeckA() {
		array = (Item[]) new Object[3];
		size = 0;
		first = 1;
		last = 1;
	}
	
	public boolean isEmpty() { return size == 0; }
	
	public int size() { return size; }
	
	public void addFirst(Item item) {
		if (size == 0) {
			array[first] = item;
			size++;
			return;
		}
		array[--first] = item;
		size++;
		int N = array.length;
		if (first == 0) {
			resize(N);
		}
		if (size == 5*N/6) {
			resize(2*N);
		}
	}
	
	public void addLast(Item item) {
		if (size == 0) {
			array[last] = item;
			size++;
			return;
		}
		array[++last] = item;
		size++;
		int N = array.length;
		if (last == N-1) {
			resize(N);
		}
		if (size == 5*N/6) {
			resize(2*N);
		}
	}
	
	private void resize(int n) {
		Item[] tab = (Item[]) new Object[n];
		int f = (n-size)/2;
		for (int i = f, j = first ; j < first+size ; i++, j++) {
			tab[i] = array[j];
		}
		array = tab;
		first = f;
		last = first+size-1;
		}
	
	public Item removeFirst() {
		if (size == 0) return null;
		Item ret = array[first];
		array[first++] = null;
		size--;
		if (size == 1) {
			first = 1;
			last = 1;
			return ret;
		}
		int N = array.length;
		if (size*6 == N) {
			resize(N/2);
		}
		return ret;
	}
	
	public Item removeLast() {
		if (size == 0) return null;
		Item ret = array[last];
		array[last--] = null;
		size--;
		if (size == 1) {
			first = 1;
			last = 1;
			return ret;
		}
		int N = array.length;
		if (size*6 == N) {
			resize(N/2);
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
		if (size == 0) return null;
		String s = "size "+size+"/"+array.length+" : ";
		Item[] tab = (Item[]) new Object[size];
		for (int i = 0, j = first ; i < size ; i++, j++) {
			tab[i] = array[j];
		}
		Iterator<Item> mi = new MyIterator(tab, size);
		while(mi.hasNext()) {
			s = s+mi.next()+" ";
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
