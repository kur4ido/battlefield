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
	
	private static final int PV = 100;
	private static final int RADIUS = 12;
	private static final int SIGHT = 250;
	
	private WayPoint m_position;
	private ArrayList<WayPoint> m_chemin;
	private int pv;
	private int angleCourant;
	private ArrayList<IBot> lstAmis;
	private ArrayList<IBot> lstEnnemis;
	private Drapeau ennemiFlag,flag;
	private boolean hasFlag = false;
	
	private Color _col,_colArc;
	private boolean ennemiSpotted = false;
	
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
		if(pv > 0) {
			g.setColor(_col);
			Vector2d v = m_position.getPosition();
			g.drawOval((int) v.x - RADIUS / 2,(int)  v.y - RADIUS / 2, RADIUS, RADIUS);
			g.fillOval((int) v.x - RADIUS / 2,(int)  v.y - RADIUS / 2, RADIUS, RADIUS);
			/*Dessin du champ de vision*/
			g.setColor(_colArc);
			if(ennemiSpotted) {
				g.setColor(new Color(0,0.5f,0,0.5f));
			}
			//g.fillOval((int) v.x - SIGHT / 2,(int)  v.y - SIGHT / 2, SIGHT, SIGHT);
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
	}
	/**
	 * Implémentation de l'intelligence artificielle et de la prise de décision
	 * -décision de la direction
	 * -décision quant à la cible à éliminer (s'il existe une ou plusieur cible à portée
	 */
	@Override
	public void AI() {
		if(pv > 0) {
			/*Tout d'abbord, le bot choisit sa direction, il a une chance sur 3 de foncer directement
			 * jusqu'au drapeau ennemi. Le reste des probabilités est séparé en 2 : il peut soit rester sur place et
			 * scruter son entourage (cela prend un tour), soit décider un nouveau chemin*/	
			if(m_chemin.isEmpty()) {
				if(hasFlag) {
					m_chemin = PathFinder.aStar(m_position, flag.getInit());
				}else {
					if(Math.random() < 0.3) {
						m_chemin = PathFinder.aStar(m_position, ennemiFlag.getInit());
					}else {
						Random r = new Random();
						if(Math.random() < 0.5) {
							angleCourant += 45 * r.nextInt(5);
							angleCourant = angleCourant % 360;
						}else {
							ArrayList<WayPoint> al = grille.getWayPoint();
							m_chemin = PathFinder.aStar(m_position, al.get(r.nextInt(al.size())));
						}
					}
				}
			}
			if(!m_chemin.isEmpty()) {
				/*L'ordinateur a une chance sur 4 de s'arreter et de scruter son voisinage*/
				if(Math.random() < 0.25) {
					Random r = new Random();
					angleCourant += 45 * r.nextInt(5);
					angleCourant = angleCourant % 360;
				}else {
					/*Si le bot continue son chemin, alors il regarde devant lui*/
					Vector2d v = m_chemin.get(0).getPosition();
					float x = v.x - m_position.getPosition().x;
					float y = v.y - m_position.getPosition().y;
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
					angleCourant = (int)(angleRad * 180 / Math.PI);
				}
			}
			/*Détection des cibles*/
			ennemiSpotted = false;
			IBot cible = null;
			int tmp = PV + 1;
			for(IBot bot : lstEnnemis) {
				if (canSeeBot(bot)) {
					ennemiSpotted = true;
					if(bot.getPV() <= tmp) {
						tmp = bot.getPV();
						cible = bot;
					}
				}
			}
			/*Elimination...ou tentative d'élimination*/
			if(cible != null)
				shoot(cible);
		}
		
			
	}

	/**
	 * Methode de mise à jour de la position, et de celle du drapeau si le bot transporte le
	 * drapeau adverse.
	 * La capture du drapeau (cad le passage de l'état libre à l'état capturé) se fait
	 * également.
	 */
	@Override
	public void updatePosition() {
		if(pv > 0) {
			if(m_chemin.size() != 0) {
				m_position = m_chemin.get(0);
				m_chemin.remove(0);
				if(hasFlag) {
					ennemiFlag.setPosition(m_position);
				}
			}
			if(!hasFlag && ennemiFlag.getPosition().equalsVec(m_position.getPosition()) && ennemiFlag.isFree()) {
				System.out.println("FLAG");
				hasFlag = true;
				ennemiFlag.setPosition(m_position);
			}
		}
	}
	
	/**
	 * Méthode permettant de savoir si le bot courrant peut voir (et donc tirer) le bot donné
	 * en paramètre.
	 * @param ib le bot adverse
	 * @return vrai si le bot courant voit ib
	 */
	public boolean canSeeBot(IBot ib){		
		Vector2d posEnnemi = ib.getCoord();
		Vector2d pos = getCoord();
		float tmp = (pos.x - posEnnemi.x) * (pos.x - posEnnemi.x) + (pos.y - posEnnemi.y) *(pos.y - posEnnemi.y);
//		System.out.println("His pos : x = "+hisPos.x+"\ty = "+hisPos.y);
		if (surface.cansee(m_position.getPosition(), posEnnemi) && !ib.isDead()){
			float x = posEnnemi.x - pos.x;
			float y = posEnnemi.y - pos.y;
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
			return (tmp < SIGHT * SIGHT && angleRad<=courant + Math.PI/8 && angleRad>=courant-Math.PI/8);
		}
		return false;
	}

	/**
	 * Methode permettant de fixer les paramètres statiques. En effet, la grille de waypoint et 
	 * la surface ne nécessite pas d'avoir une variable associée pour chaque instance de Bot.
	 * @param s
	 * @param gwp
	 */
	public static void setParamStatic(Surface s,GrilleWayPoint gwp) {
		surface = s;
		grille = gwp;
	}

	/**
	 * Methode permettant de spécifier la liste des bots adverses
	 */
	@Override
	public void setEnnemies(ArrayList<IBot> list) {
		lstEnnemis = new ArrayList<IBot>(list);
	}

	/**
	 * Methode permettant de spécifier la liste des bots alliés.
	 */
	@Override
	public void setAllies(ArrayList<IBot> list) {
		lstAmis = new ArrayList<IBot>(list);
		lstAmis.remove(this);
	}

	/**
	 * Methode permettant de spécifier le drapeau ennemi.
	 */
	@Override
	public void setDrapeauEnnemi(Drapeau flag) {
		ennemiFlag = flag;
	}

	/**
	 * Methode permettant de spécifier le drapeau Ami
	 */
	@Override
	public void setDrapeauAmi(Drapeau flag) {
		this.flag = flag;
	}

	/**
	 * Methode permettant de tirer sur une cible
	 */
	@Override
	public void shoot(IBot maCible){
		maCible.isShot();
	}
	
	/**
	 * Methode appelée lorsque l'on se fait tirer dessus.
	 */
	@Override
	public boolean isShot(){
		pv -= 20;
		if(pv <= 0){
			/*si le bot est mort, alors le drapeau retourne automatiquement dans sa base*/
			if(hasFlag){
				ennemiFlag.reset();
				hasFlag = false;
			}
			return true;
		}

		return false;
	}

	@Override
	public int getPV() {
		return pv;
	}

	
}
