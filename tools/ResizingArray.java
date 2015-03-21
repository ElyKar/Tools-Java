package tools;
/*
 * Mémoire : 
 * OverHead --> 16 octets
 * array --> jusqu'à 4N*8 octets (tableau au quart plein)
 * size --> 4 octets
 * Padding --> 4 octets
 * Total : 32N + 48 (8N + 48)
 */
public class ResizingArray<Item> {
	Item[] array;
	int size;
	
	public ResizingArray() {
		array = (Item[]) new Object[2];
	}
	
	public int size() { return size; }
	
	public void insert(Item item) {
		if (item == null) return;
		array[size++] = item;
		if (size == array.length) {
			this.resize(size*2);
		}
	}
	
	public Item remove() {
		if (size == 0) return null;
		Item ret = array[--size];
		array[size] = null;
		if (size*4 == array.length) {
			this.resize(size*2);
		}
		return ret;
	}
	
	public Item get(int i) {
		if (size == 0) return null;
		if (i >= size) return null;
		return array[i];
	}
	
	public Item remove(int i) {
		if (size == 0) return null;
	    if (i >= size) return null;
	    Item ret = array[i];
	    for (int j = i ; j < size ; j++) {
	    	array[j] = array[j+1];
	    }
	    array[--size] = null;
	    if (size*4 == array.length) {
			this.resize(size/4);
		}
	    return ret;
	}
	
	private void resize(int N) {
		Item[] newarray = (Item[]) new Object[N];
		for (int i = 0 ; i < size ; i++) {
			newarray[i] = array[i];
			array[i] = null;
		}
		array = newarray;
	}
	
	public String toString() {
	String s = "";
	for (int i = 0 ; i < size ; i++) {
		s = s+array[i].toString()+" ";
	}
	return s;
	}
	
	public static void main(String[] args) {
		ResizingArray<Integer> RA = new ResizingArray<>();
		for (int i = 0 ; i < 17 ; i++) {
			RA.insert(i);
		}
		System.out.println(RA);
		System.out.println(RA.remove());
		System.out.println(RA);
		System.out.println(RA.remove(0));
		System.out.println(RA);
		System.out.println(RA.remove(20));
		System.out.println(RA);
		System.out.println(RA.remove(14));
		System.out.println(RA);
		System.out.println(RA.remove(5));
		System.out.println(RA);
	}

}
/*0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 
16
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 
0
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 
null
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 
15
1 2 3 4 5 6 7 8 9 10 11 12 13 14 
6
1 2 3 4 5 7 8 9 10 11 12 13 14 */
