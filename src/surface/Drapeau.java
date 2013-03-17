package surface;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.Drawable;
import utils.Vector2d;

public class Drapeau implements Drawable{
	private Vector2d m_position;
	private WayPoint init;
	private BufferedImage image;
	
	public Drapeau(WayPoint wp, BufferedImage img) {
		init = wp;
		m_position = wp.getPosition();
		image = img;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(image,(int) m_position.x - image.getWidth() / 2, (int)m_position.y - image.getHeight() / 2,
				image.getWidth(), image.getHeight(), null);
		
	}
	
	public void reset() {
		m_position = init.getPosition();
	}

}
