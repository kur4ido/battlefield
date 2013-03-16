package surface;

import java.util.HashMap;

import utils.Vector2d;

public class GrilleWayPoint {
	private HashMap<Integer, WayPoint> grille;
	private int distance;
	
	public GrilleWayPoint(int distance) {
		grille = new HashMap<Integer, WayPoint>();
		this.distance = distance;
	}
	
	public void faireMappage(Surface s) {
		WayPoint[][] tab = new WayPoint[s.wxsize / distance][s.wysize / distance];
		for(int y = 0; y < tab[0].length - 1; y ++) {
			for(int x = 0; x < tab.length - 1 ; x ++) {
				Vector2d vec = new Vector2d(x * distance, y * distance);
				if(s.estDansObjet(vec) == null) {
					tab[x][y] = new WayPoint(vec);
				}
				else {
					tab[x][y] = null;
				}
			}
		}
		for(int y = 0; y < tab[0].length - 1; y ++) {
			for(int x = 0; x < tab.length - 1 ; x ++) {
				WayPoint w = tab[x][y];
				if(w != null) {
					w.ajouterVoisin(tab[x + 1][y]);
					w.ajouterVoisin(tab[x][y + 1]);
					w.ajouterVoisin(tab[x + 1] [y + 1]);
					if(x > 0) {
						w.ajouterVoisin(tab[x - 1][y + 1]);
					}
				}
			}
		}
		/*Création des WayPoint sur la dernière ligne*/
		for(int x = 0 ; x < tab.length - 1; x++) {
			WayPoint w = tab[x][tab[x].length - 1];
			if(w != null) {
				w.ajouterVoisin(tab[x + 1][tab[x].length- 1]);
			}
		}
		/*Création des WayPoint sur la dernière colonne*/
		for(int y = 0 ; y < tab[0].length - 1; y++) {
			WayPoint w = tab[tab.length - 1][y];
			if(w != null) {
				w.ajouterVoisin(tab[tab.length - 1][y + 1]);
			}
		}
	}
	
	
}
