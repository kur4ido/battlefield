package surface;

import java.util.ArrayList;

import utils.Vector2d;

public class WayPoint {
	private static int nbWayPoint = 0;
	private int ID;
	protected Vector2d m_position;
	protected ArrayList<WayPoint> m_lstVoisins;
	
	public WayPoint(Vector2d position) {
		m_position = position;
		ID = nbWayPoint;
		nbWayPoint ++;
	}
	
	public void ajouterVoisin(WayPoint w) {
		if(w != null && !m_lstVoisins.contains(w)) {
			m_lstVoisins.add(w);
			w.ajouterVoisin(this);
		}
	}
	
	public Vector2d getPosition() {
		return new Vector2d(m_position.x,m_position.y);
	}
	
	public ArrayList<WayPoint> getVoisins() {
		return m_lstVoisins;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof WayPoint)) {
			return false;
		}else {
			WayPoint w = (WayPoint) o;
			return w.m_position.equals(m_position);
		}
	}

	public static float distance(WayPoint w, WayPoint voisin) {
		return w.m_position.distance(voisin.m_position);
	}
}
