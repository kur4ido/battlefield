package surface;

import java.awt.Color;
import java.awt.Graphics;
import utils.Drawable;
import utils.Vector2d;

public class Drapeau implements Drawable{
	private Vector2d m_position;
	private WayPoint init;
	private Color couleur;
	private boolean free = true;
	
	public Drapeau(WayPoint wp,Color color) {
		init = wp;
		m_position = wp.getPosition();
		couleur = color;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(couleur);
		int[] x = {(int)m_position.x + 7,(int)m_position.x - 5,(int) m_position.x - 5};
		int[] y = {(int)m_position.y,(int) m_position.y - 5, (int) m_position.y + 5};
		g.fillPolygon(x, y, 3);
		init.draw(g);
	}
	
	public void reset() {
		m_position = init.getPosition();
		free = true;
	}

	public WayPoint getInit() {
		return init;
	}
	
	public Vector2d getPosition() {
		return new Vector2d(m_position.x,m_position.y);
	}

	public boolean isFree() {
		return free;
	}

	public void setPosition(WayPoint pos) {
		m_position = new Vector2d(pos.getPosition().x,pos.getPosition().y);
		free = false;
	}

}
