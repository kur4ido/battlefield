package ia;

import java.util.ArrayList;
import java.util.HashMap;
import surface.WayPoint;

public class PathFinder {
	
	/*public static HashSet<WayPoint> listeDestination(WayPoint init, int mouvement) {
		HashSet<WayPoint> resultat = new HashSet<WayPoint>();
		resultat.add(init);
		if(mouvement > 0) {
			for(WayPoint t : init.getVoisins()) {
				if(t.getCout() <= mouvement && t.isAccessible()) {
					resultat.add(t);
					resultat.addAll(listeDestination(t,mouvement - t.getCout()));
				}
			}
		}
		return resultat;
	}*/
	
	public static ArrayList<WayPoint> aStar(WayPoint init,WayPoint dest, int mouvement) {
		ArrayList<WayPoint> dejaVu = new ArrayList<WayPoint>();
		ArrayList<WayPoint> enCours = new ArrayList<WayPoint>();
		enCours.add(init);
		boolean cheminTrouve = false;
		HashMap<WayPoint, Float> coutsWayPoint = new HashMap<WayPoint, Float>();
		HashMap<WayPoint, WayPoint> pere = new HashMap<WayPoint, WayPoint>();
		coutsWayPoint.put(init, 0.0f);
		while(!enCours.isEmpty() && !cheminTrouve){
			//System.out.println("inside while");
			WayPoint w = choisirWayPoint(enCours, coutsWayPoint);
			if(dest.equals(w)){
				cheminTrouve = true;
			}
			else{
				enCours.remove(w);
				dejaVu.add(w);
				for(WayPoint voisin : w.getVoisins()){
					if(!dejaVu.contains(voisin) && !enCours.contains(voisin)){
						pere.put(voisin, w);	
						coutsWayPoint.put(voisin, WayPoint.distance(w,voisin) + coutsWayPoint.get(w));
						enCours.add(voisin);
					}
					else{
						if( coutsWayPoint.get(voisin) > coutsWayPoint.get(w) +  WayPoint.distance(w,voisin) ){
							if(dejaVu.contains(voisin)){
								dejaVu.remove(voisin);
							}
							pere.put(voisin, w);
							coutsWayPoint.put(voisin,  WayPoint.distance(w,voisin)  + coutsWayPoint.get(w));
							enCours.add(voisin);
						}
					}
				}
			}
		}
		if(!cheminTrouve) {
			return null;
		}else {
			ArrayList<WayPoint> resultat = new ArrayList<WayPoint>();
			WayPoint t = dest;
			while(!t.equals(init)) {
				resultat.add(0, t);
				t = pere.get(t);
			}
			resultat.add(0,t);
			for(int i =  resultat.size() - 1; i > 0; i--) {
				if(coutsWayPoint.get(resultat.get(i)) > mouvement) {
					resultat.remove(i);
				}
			}

			return resultat;
		}
	}
	
	private static WayPoint choisirWayPoint(ArrayList<WayPoint> enCours,HashMap<WayPoint, Float> coutsWayPoint) {
		double min = Integer.MAX_VALUE;
		WayPoint resultat = null;
		for(WayPoint t : enCours){
			if(min >= coutsWayPoint.get(t)){
				resultat = t;
				min = coutsWayPoint.get(t);
			}
		}
		return resultat;
	}
}
