package tools;
import java.util.Iterator;
import java.util.LinkedList;

public class LLJ<Item>{
	LinkedList<Item> list;
	int size;
	
	public LLJ() {
		size = 0;
		list = new LinkedList<>();
	}
	
	public void insert(Item item) {
		list.add(item);
		size++;
	}
	
	public Item remove() {
		Item ret = list.remove();
		size--;
		return ret;
	}
	
	public Item remove(int i) {
		Item ret = list.remove(i);
		size--;
		return ret;
	}
	
	public Item get(int i) {
		Item ret = list.get(i);
		return ret;
	}
	
	public String toString() {
		String res = "";
		for (Item e : list) {
			res += e;
		}
		return res;
	}
	
	public static void main(String[] args) {
		Chrono s = new Chrono();
		LLJ<Integer> RA = new LLJ<>();
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

}
