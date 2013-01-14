package bots;

import java.awt.Color;
import java.awt.Graphics;

import utils.Vector2d;

public class Bot implements IBot{

	private Vector2d _position;
	private Vector2d _deplacement;
	
	private Color _col;
	private final int RADIUS = 10;
	private final int SPEED = 20;
	
	public Bot(int x, int y, Color col) {
		_position = new Vector2d(x,y);
		_col = col;
	}
	
	@Override
	public Vector2d getCoord() {
		return _position;
	}

	@Override
	public float botRadius() {
		return RADIUS;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(_col);
		g.drawOval((int) _position.x,(int)  _position.y, RADIUS, RADIUS);
		g.fillOval((int) _position.x,(int)  _position.y, RADIUS, RADIUS);
		
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
