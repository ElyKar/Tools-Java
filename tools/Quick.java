package tools;

public class Quick {
	
	private static class Knuth {
		
	    public static void shuffle(Object[] a) {
	        int N = a.length;
	        for (int i = 0; i < N; i++) {
	            int r = i + (int) (Math.random() * (N - i));
	            Object temp = a[r];
	            a[r] = a[i];
	            a[i] = temp;
	        }
	    }
	}
	
	public static void sort(Comparable[] a) {
		Knuth.shuffle(a);
		int N = a.length-1;
		sort(a, 0, N);
	}
	
	private static void sort(Comparable[] a, int start, int end) { 
        if (end <= start) return;
        if (end - start <= 4) {
        	Insertion.sort(a, start, end);
        	return;
        }
        int j = partition(a, start, end);
        sort(a, start, j-1);
        sort(a, j+1, end);
    }

    private static int partition(Comparable[] a, int start, int end) {
        int i = start;
        int j = end + 1;
        Comparable v = a[start];
        while (true) {
            while (less(a, ++i, start))
                if (i == end) break;
            while (less(a, start, --j))
                if (j == start) break;
            if (i >= j) break;
            exch(a, i, j);
        }
        exch(a, start, j);
        return j;
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
		Integer[] tab = new Integer[1000];
		for (int i = 0 ; i < 1000 ; i++) {
			tab[i] = (Integer) ((int) (Math.random()*5000));
		}
		Quick.sort(tab);
		for (Integer i : tab) {System.out.println(i);}
	}
}
