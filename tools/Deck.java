package tools;

public interface Deck<Item> extends Iterable<Item>{
	
	int size();
	boolean isEmpty();
	void addFirst(Item item);
	void addLast(Item item);
	Item removeFirst();
	Item removeLast();
	String toString();

}
