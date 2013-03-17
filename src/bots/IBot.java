package bots;

import java.awt.Graphics;
import java.util.ArrayList;

import surface.Drapeau;
import surface.WayPoint;
import utils.Drawable;
import utils.Vector2d;

/**
 * Petite interface "indicative" de ce qu'il peut y avoir dans un bot.
 * Il faut aussi gérer l'ensemble des bots du jeu dans une structure à part.
 *  
 * @author L. Simon, Univ. Paris Sud, 2008
 *
 */
public interface IBot extends Drawable{
	
	/**
	 * @return Les coordonnées du bot, ou -1,-1 si pas sur la carte par ex.
	 */
	public Vector2d getCoord();
	
	public WayPoint getPosition();
	
	public void setEnnemies(ArrayList<IBot> list);
	
	public void setAllies(ArrayList<IBot> list);
	
	/**
	 * On va représenter les bots en rond... Pour les collisions sur la carte,
	 * on a besoin du rayon du bot
	 * @return le rayon (en coordonnées de cartes) du bot
	 */
	public float botRadius();
	
	/**
	 * Affiche le bot sur le dessin... A vos idées !
	 * @param g
	 */
	public void draw(Graphics g);
	
	/**
	 * @return la chaine representant le bot et son etat, pour debug ou autre
	 */
	public String toString();
	
	/**
	 * Mise à jour IA
	 * des decisions à prendre 
	 */
	public void AI();

	/**
	 * Suivant ses decisions et les autres, le bot peut mettre a jour ses positions
	 */
	public void updatePosition();

	public boolean isDead();

	public void setDrapeauEnnemi(Drapeau flag);

	public void setDrapeauAmi(Drapeau flag);
	
	
}
