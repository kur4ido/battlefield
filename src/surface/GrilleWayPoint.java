package surface;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import utils.Drawable;
import utils.Vector2d;

public class GrilleWayPoint implements Drawable{
	private HashMap<Integer, WayPoint> grille;
	private int distance;
	
	public GrilleWayPoint(int distance) {
		grille = new HashMap<Integer, WayPoint>();
		this.distance = distance;
	}
	
	public void faireMappage(Surface s) {
		WayPoint[][] tab = new WayPoint[1 + s.width / distance][1 + s.height / distance];
		for(int y = 0; y < tab[0].length - 1; y ++) {
			for(int x = 0; x < tab.length - 1 ; x ++) {
				Vector2d vec = new Vector2d((x + 0.5f) * distance, (y + 0.5f) * distance);
				if(s.estDansObjet(vec) == null) {
					tab[x][y] = new WayPoint(vec);
					grille.put(tab[x][y].getID(), tab[x][y]);
				}
				else {
					tab[x][y] = null;
				}
			}
		}
		for(int y = 0; y < tab[0].length - 1; y ++) {
			for(int x = 0; x < tab.length - 1 ; x ++) {
				WayPoint w = tab[x][y];
				WayPoint tmp;
				if(w != null) {
					tmp = tab[x + 1][y];
					if(tmp != null && s.cansee(w.getPosition(), tmp.getPosition())) {
						w.ajouterVoisin(tmp);
					}
					
					tmp = tab[x][y + 1];
					if(tmp != null && s.cansee(w.getPosition(), tmp.getPosition())) {
						w.ajouterVoisin(tmp);
					}
					
					tmp = tab[x + 1] [y + 1];
					if(tmp != null && s.cansee(w.getPosition(), tmp.getPosition())) {
						w.ajouterVoisin(tmp);
					}
					
					tmp = (x > 0) ? tab[x - 1][y + 1] : null;
					if(tmp != null && s.cansee(w.getPosition(), tmp.getPosition())) {
						w.ajouterVoisin(tmp);
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
	
	public ArrayList<WayPoint> getWayPoint() {
		return new ArrayList<WayPoint>(grille.values());
	}

	@Override
	public void draw(Graphics g) {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		for(WayPoint w : grille.values()) {
			if(! tmp.contains(w.getID())) {
				w.draw(g);
				tmp.add(w.getID());
			}
		}
		
	}

	public WayPoint getWayPoint(int ID) {
		return grille.get(ID);
	}
	
}
