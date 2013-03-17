package surface;

import utils.Vector2d;
import java.util.*;
import java.awt.*;

/**
 * A simple class to handle the surface itself. All objects are polylines.
 * 
 * @author L. Simon, Univ. Paris Sud, 2008.
 *
 */
public class Surface {


	public int width, height;

	// All objects on the surface are recorded in this vector of polylines.
	private Vector<PolylineObject> objects; // The objects on the surface
	private GrilleWayPoint grille;
	/**
	 * Well, right now the objects are built "by hands". May by the first
	 * thing to do would be to put polylines objects in a map, and read the
	 * file and objects.
	 * 
	 * In this case, the size of the surface should be set inside the constructor?
	 * 
	 * @param wxsize
	 * @param wysize
	 * @param scale
	 */
	public Surface( int wxsize, int wysize, float scale) {
		this.width = wxsize;
		this.height = wysize;
		objects = new Vector<PolylineObject>();

		PolylineObject ob1 = new PolylineObject(new Vector2d(100F,200F),this);
		ob1.addNode(new Vector2d(100F,250F));
		ob1.addNode(new Vector2d(200F,250F));
		ob1.addNode(new Vector2d(200F,200F));
		ob1.fixObject();
		objects.add(ob1);

		PolylineObject ob2 = new PolylineObject(new Vector2d(300F,500F),this);
		ob2.addNode(new Vector2d(300F,800F));
		ob2.addNode(new Vector2d(320F,800F));
		ob2.addNode(new Vector2d(320F,500F));
		ob2.fixObject();
		objects.add(ob2);
	}

	public void addObject(PolylineObject obj) {
		objects.add(obj);
	}
	
	public void mappingWayPoint() {
		
	}
	
	/**
	 * Draws all objects on the surface.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		for(int i=0;i<objects.size();i++) {
			objects.get(i).draw(g);
		}
	}

	/**
	 * One of the main methods. Checks if the segment (tmpA, tmpB)
	 * intersects any of the lines of any polyline. If not, then
	 * the point tmpA 'can see' the point tmpB.
	 * 
	 * @param tmpA
	 * @param tmpB
	 * @return true if tmpA can see tmpB
	 */
	public boolean cansee(Vector2d tmpA, Vector2d tmpB) {
		for(int i=0;i<objects.size();i++) {
			if (objects.get(i).intersectsWith(tmpA, tmpB))
				return false;
		}
		return true;

	}

	public PolylineObject estDansObjet(Vector2d p) {
		for(int i=0;i<objects.size();i++) {
			if (objects.get(i).contains(p)) {
				return objects.get(i);
			}
		}
		return null;
	}


}

