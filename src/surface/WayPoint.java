package surface;

import java.util.ArrayList;

import utils.Vector2d;

public class WayPoint {

	protected Vector2d m_position;
	protected ArrayList<WayPoint> m_lstVoisins;
	
	public WayPoint(Vector2d position) {
		m_position = position;
	}
	
	public void ajouterVoisin(WayPoint w) {
		if(!m_lstVoisins.contains(w)) {
			m_lstVoisins.add(w);
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
}
