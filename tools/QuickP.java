package tools;
import stdlib.StdIn;
/*
 * Cette repr�sentation permet une construction en un temps proportionnel � M*lg(N)
 * La recherche de connexions prend un temps inf�rieur � 2*lg(N)
 * On compresse le chemin pour rendre l'acc�s plus rapide
 */
/*
 * Plut�t qu'une repr�sentation lin�aire, cet algorithme permet de repr�senter les noeuds en arbre
 * Pour savoir si deux noeuds sont connect�s, on compare leurs racines, et on it�re.
 * De plus, on accroche syst�matiquement le plus petit arbre au plus grand, afin d'�quilibrer l'ensemble
 * On essaye d'accrocher le plus grand nombre de noeuds � la racine pour r�duire le temps de parcours d'un chemin
 * (ie avoir un arbre "plat")
 */
public class QuickP {
	
	public static void main(String[] args) {
		int N = StdIn.readInt();
		int id[] = new int[N], sz[] = new int[N];
		for(int i = 0 ; i < N ; id[i] = i, sz[i++] = 1);
		while(!StdIn.isEmpty()) {
			int i, j, p = StdIn.readInt(), q = StdIn.readInt();
			for (i = p ; i != id[i] ; i = id[i]) {
				id[i] = id[id[i]];
			}
			for (j = q ; j != id[j] ; j = id[j]) {
				id[j] = id[id[j]];
			}
			if (i == j) continue;
			if (sz[i] < sz[j]) {
				id[i] = j; sz[j] += sz[i]; }
			else {
				id[j] = i; sz[i] += sz[j]; }
			String s = "";
			for (int K = 0 ; K < N ; K++) {
				s += " " + id[K];
			}
			System.out.println(s);
		}
	}

}

/*
10

3 4 	0 1 2 3 3 5 6 7 8 9
4 9	 	0 1 2 3 3 5 6 7 8 3
8 0 	8 1 2 3 3 5 6 7 8 3
2 3 	8 1 3 3 3 5 6 7 8 3
5 6 	8 1 3 3 3 5 5 7 8 3
2 9 	8 1 3 3 3 5 5 7 8 3
5 9 	8 1 3 3 3 3 5 7 8 3
7 3 	8 1 3 3 3 3 5 3 8 3
4 8 	8 1 3 3 3 3 5 3 3 3
5 6 	8 1 3 3 3 3 5 3 3 3
0 2 	8 1 3 3 3 3 5 3 3 3
6 1 	8 3 3 3 3 3 5 3 3 3
 */
