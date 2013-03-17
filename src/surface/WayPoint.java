package surface;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import utils.Drawable;
import utils.Vector2d;

public class WayPoint implements Drawable{
	private static int RADIUS = 2;
	private static int nbWayPoint = 0;
	private int ID;
	protected Vector2d m_position;
	protected ArrayList<WayPoint> m_lstVoisins;
	
	public WayPoint(Vector2d position) {
		m_position = position;
		ID = nbWayPoint;
		nbWayPoint ++;
		m_lstVoisins = new ArrayList<WayPoint>();
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
	
	public int getID() {
		return ID;
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

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillOval((int)m_position.x, (int)m_position.y, RADIUS, RADIUS);
		for(WayPoint w : m_lstVoisins) {
			g.fillOval((int)w.m_position.x, (int)w.m_position.y, RADIUS, RADIUS);
			g.drawLine((int)m_position.x, (int)m_position.y,(int)w.m_position.x, (int)w.m_position.y);
		}
	}
}
