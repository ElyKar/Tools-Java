package tools;

public interface Stack<Item> extends Iterable<Item>{
	
	boolean isEmpty();
	
	int size();
	
	void push(Item o);
	
	Item pop();
	
	String toString();

}
