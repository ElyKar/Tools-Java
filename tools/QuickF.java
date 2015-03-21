package tools;
import stdlib.StdIn;
/*
 * L'algorithme présent permet de voir si deux objets appartenant à un graphe sont liés
 * La construction de l'ensemble prend un temps proportionnel à N*M
 * La vérification si deux points sont liés ou non se fait en temps constant.
 */
/*
 * On créé un tableau de n éléments
 * Lorsque l'on rentre une paire, on affecte la valeur de l'un des noeuds à l'autre
 * Puis on itère à travers le tableau pour actualiser les liens déjà existants
 */
public class QuickF {
	
	public static void main(String[] args) {
		int N = StdIn.readInt();
		int id[] = new int[N];
		for (int i = 0 ; i < N ; i++) id[i] = i;
		while (!StdIn.isEmpty()) {
			int p = StdIn.readInt(); int q = StdIn.readInt();
			int t = id[p];
			if (t == id[q]) continue;
			for (int i = 0 ; i < N ; i++)
				if (id[i] == t) id[i] = id[q];
			String s = "";
			for (int i = 0 ; i < N ; i++) {
				s += " " + id[i];
			}
			System.out.println(s);
		}
	}

}
/*
 * Etapes successives suite à l'insertion d'une suite de paires
 */
/*
10 		0 1 2 3 4 5 6 7 8 9
3 4 	0 1 2 4 4 5 6 7 8 9
4 9 	0 1 2 9 9 5 6 7 8 9
8 0 	0 1 2 9 9 5 6 7 0 9
2 3 	0 1 9 9 9 5 6 7 0 9
5 6 	0 1 9 9 9 6 6 7 0 9
2 9 	0 1 9 9 9 6 6 7 0 9
5 9 	0 1 9 9 9 9 9 7 0 9
7 3 	0 1 9 9 9 9 9 9 0 9
4 8 	0 1 0 0 0 0 0 0 0 0
5 6 	0 1 0 0 0 0 0 0 0 0
0 2 	0 1 0 0 0 0 0 0 0 0
6 1 	1 1 1 1 1 1 1 1 1 1
*/

