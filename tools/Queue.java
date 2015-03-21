package tools;

public interface Queue<Item> extends Iterable<Item>{
	
	int size();
	
	boolean isEmpty();
	
	void enqueue(Item i);
	
	Item dequeue();
	
	String toString();

}
