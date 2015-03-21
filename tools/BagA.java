package tools;
import java.util.Iterator;

public class BagA<Item> implements Iterable<Item> {
	private Item[] array;
	private int size;
	
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
	
	public BagA() {
		array = (Item[]) new Object[2];
		size = 0;
	}
	
	public boolean isEmpty() { return size == 0; }
	
	public int size() { return size; }
	
	public void add(Item item) {
		array[size] = item;
		size++;
		int N = array.length;
		if(size == N) {
			resize(2*N);
		}
	}
	
	public Item pop() {
		if (size == 0) return null;
		int rnd = (int) (Math.random()*size);
		size--;
		Item it = array[rnd];
		array[rnd] = array[size];
		array[size] = null;
		int N = array.length;
		if (size*4 == N) {
			resize(N/2);
		}
		return it;
	}
	
	private void resize(int N) {
		Item[] newarray = (Item[]) new Object[N];
		for (int i = 0 ; i < size ; i++) {
			newarray[i] = array[i];
			array[i] = null;
		}
		array = newarray;
	}
	
	public Iterator<Item> iterator() {
		if (size == 0) return null;
		Item[] tab = (Item[]) new Object[size];
		for (int i = 0; i < size ; i++) {
			tab[i] = array[i];
		}
		StdRandom.shuffle(tab);
		Iterator<Item> mi = new MyIterator(tab, size);
		return mi;
	}
	
	public String toString() {
		if (size == 0) return null;
		String s = "size "+size+"/"+array.length+" : ";
		Iterator<Item> mi = iterator();
		while(mi.hasNext()) {
			s = s+mi.next()+" ";
		}
		return s;
	}
	
	public static void main(String[] args){
		BagA<Integer> SA = new BagA<>();
		for (int i = 0 ; i < 20 ; i++) {
			SA.add(i);
		}
		System.out.println(SA.pop());
		System.out.println(SA.pop());
		System.out.println(SA);
	}

}
