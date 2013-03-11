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
		for(int y = 0; y * distance < s.wysize; y ++) {
			for(int x = 0; x * distance < s.wxsize ; x ++) {
				Vector2d vec = new Vector2d(x * distance, y * distance);
				if(s.estDansObjet(vec) == null) {
					tab[x][y] = new WayPoint(vec);
				}
				else {
					tab[x][y] = null;
				}
			}
		}
		for(int y = 0; y * distance < s.wysize - distance; y ++) {
			for(int x = 0; x * distance < s.wxsize - distance ; x ++) {
				WayPoint w = tab[x][y];
				if(w != null) {
					w.ajouterVoisin(tab[x + 1][y]);
					w.ajouterVoisin(tab[x][y + 1]);
					w.ajouterVoisin(tab[x + 1] [y + 1]);
					
					//TODO 2nde diag
				}
			}
		}
	}
	
	
}
