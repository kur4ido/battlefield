package bots;

import ia.PathFinder;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import surface.Drapeau;
import surface.GrilleWayPoint;
import surface.Surface;
import surface.WayPoint;
import utils.Vector2d;

public class Bot implements IBot{

	protected static Surface surface;
	protected static GrilleWayPoint grille;
	
	private static final int RADIUS = 12;
	private static final int SIGHT = 250;
	
	private WayPoint m_position;
	private ArrayList<WayPoint> m_chemin;
	private int pv;
	private int angleCourant;
	private ArrayList<IBot> lstAmis;
	private ArrayList<IBot> lstEnnemis;
	private Drapeau ennemiFlag,flag;
	
	private Color _col,_colArc;
	
	
	public Bot(WayPoint pos, Color col) {
		pv = 100;
		angleCourant = 0;
		m_position = pos;
		_col = col;
		_colArc = new Color(_col.getRed(),_col.getGreen(),_col.getBlue(),128);
		m_chemin = new ArrayList<WayPoint>();
	}
	
	@Override
	public Vector2d getCoord() {
		return new Vector2d(m_position.getPosition().x,m_position.getPosition().y);
	}

	@Override
	public boolean isDead() {
		return pv <= 0;
	}
	
	@Override
	public float botRadius() {
		return RADIUS;
	}

	@Override
	public WayPoint getPosition() {
		return m_position;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(_col);
		Vector2d v = m_position.getPosition();
		g.drawOval((int) v.x - RADIUS / 2,(int)  v.y - RADIUS / 2, RADIUS, RADIUS);
		g.fillOval((int) v.x - RADIUS / 2,(int)  v.y - RADIUS / 2, RADIUS, RADIUS);
		/*Dessin du champ de vision*/
		g.setColor(_colArc);
		g.fillArc((int) v.x-SIGHT / 2,(int) v.y-SIGHT / 2, SIGHT, SIGHT, angleCourant -23, 45);
		g.setColor(_col);
		
		/*Dessin du chemin suivi*/
		ArrayList<WayPoint> al = new ArrayList<WayPoint>(m_chemin);
		al.add(0,m_position);
		for(int i = 0; i < al.size() - 1 ; i ++) {
			Vector2d v1 = al.get(i).getPosition();
			Vector2d v2 = al.get(i + 1).getPosition();
			g.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
		}
	}

	@Override
	public void AI() {
		if(m_chemin.isEmpty()) {
			Random r = new Random();
			ArrayList<WayPoint> al = grille.getWayPoint();
			m_chemin = PathFinder.aStar(m_position, al.get(r.nextInt(al.size())));
			m_chemin = (m_chemin == null ? new ArrayList<WayPoint>() : m_chemin);
		}
		if(!m_chemin.isEmpty()) {
			Vector2d v = new Vector2d(0f, 1f);
			Vector2d u = new Vector2d();
			u.setDiff(m_chemin.get(0).getPosition(),m_position.getPosition());
			u.setNormalize();
			double courant = Math.acos(v.dot(u));
			if(u.x < 0) {
				courant = - courant;
			}
			angleCourant = (int)(courant * 180 / Math.PI) - 90;
		}
	}

	@Override
	public void updatePosition() {
		if(m_chemin.size() != 0) {
			m_position = m_chemin.get(0);
			m_chemin.remove(0);
		}
		
	}
	
	public boolean canSeeBot(IBot ib){
		WayPoint cible = null;
			
		Vector2d posEnnemi = ib.getCoord();
		
//			System.out.println("His pos : x = "+hisPos.x+"\ty = "+hisPos.y);
		if (surface.cansee(m_position.getPosition(), posEnnemi) && !ib.isDead()){
			cible = ib.getPosition();
			float x = cible.getPosition().x - m_position.getPosition().x;
			float y = cible.getPosition().y - m_position.getPosition().y;
			double angleRad=0;
			if(x>0 && y<=0){
				angleRad = Math.atan2(-y, x);
			}else if(x>0 && y>0){
				angleRad = Math.atan2(-y, x)+Math.PI;		
			}else if(x<0){
				angleRad = Math.atan2(-y, x)-Math.PI;				
			}else if(y<0){
				angleRad = Math.PI/2;
			}else if(y>0){
				angleRad = -Math.PI/2;
			}
			if(x != 0 && y !=0)
				angleRad+=Math.PI;
			double courant = angleCourant*Math.PI/180;
			return (angleRad<=courant + Math.PI/8 && angleRad>=courant-Math.PI/8 && posEnnemi.distance(getCoord()) < SIGHT);
		}
		return false;
	}

	public static void setParamStatic(Surface s,GrilleWayPoint gwp) {
		surface = s;
		grille = gwp;
	}

	@Override
	public void setEnnemies(ArrayList<IBot> list) {
		lstEnnemis = new ArrayList<IBot>(list);
	}

	@Override
	public void setAllies(ArrayList<IBot> list) {
		lstAmis = new ArrayList<IBot>(list);
		lstAmis.remove(this);
	}

	@Override
	public void setDrapeauEnnemi(Drapeau flag) {
		ennemiFlag = flag;
	}

	@Override
	public void setDrapeauAmi(Drapeau flag) {
		this.flag = flag;
	}

	

	
}
