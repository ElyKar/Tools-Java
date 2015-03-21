package tools;

public final class Insertion {
	
	public static void sort(Comparable[] a) {
		int N = a.length;
		for (int i = 0 ; i < N-1 ; i++) {
			int j = i+1;
			while (less(a, j, j-1)) {
				exch(a, j, j-1);
				j--;
			}
		}
	}
	
	public static void sort(Comparable[] a, int start, int end) {
		for (int i = start ; i < end ; i++) {
			int j = i+1;
			while (less(a, j, j-1) && j > start) {
				exch(a, j, j-1);
				j--;
			}
		}
	}
	
	private static boolean less(Comparable[] a, int b, int c) {
		if ( b < 0 || c < 0) return false;
		return (a[b].compareTo(a[c]) < 0);
	}
	
	private static void exch (Comparable[] a, int b, int c) {
		Comparable temp = a[b];
		a[b] = a[c];
		a[c] = temp;
		temp = null;
	}
	
	public static void main(String[] args) {
		Integer[] tab = {5,2,4,3,1,0};
		for (Integer i : tab) {System.out.println(i);}
		Insertion.sort(tab,1,5);
		for (Integer i : tab) {System.out.println(i);}
	}

}
