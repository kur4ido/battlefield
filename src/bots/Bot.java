package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import surface.WayPoint;
import utils.Vector2d;

public class Bot implements IBot{

	private Vector2d m_position;
	private ArrayList<WayPoint> m_chemin;
	
	
	
	private Color _col;
	private final int RADIUS = 10;
	private final int SPEED = 20;
	
	public Bot(int x, int y, Color col) {
		m_position = new Vector2d(x,y);
		_col = col;
		m_chemin = new ArrayList<WayPoint>();
	}
	
	@Override
	public Vector2d getCoord() {
		return new Vector2d(m_position.x,m_position.y);
	}

	@Override
	public float botRadius() {
		return RADIUS;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(_col);
		g.drawOval((int) m_position.x,(int)  m_position.y, RADIUS, RADIUS);
		g.fillOval((int) m_position.x,(int)  m_position.y, RADIUS, RADIUS);
		
	}

	@Override
	public void AI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

}
