package m4ges.models.classes;

import com.badlogic.gdx.graphics.g2d.Animation;
import m4ges.models.Personnage;
import m4ges.models.Skill;

public abstract class Joueur extends Personnage{
	
	boolean pret;
	
	public Joueur(){
		super();
		pret = false;
	}

	@Override
	public abstract String getDesc();

	@Override
	public abstract Animation animate();

	public abstract byte[] getBytes();
	
	//TODO a faire (hey oui)
	//UNIQUEMENT POUR LES ATTAQUES D'UN JOUEUR VERS UN NPC
	public void attaque(Personnage p, Skill s){
		System.out.println("Methode attaque joueurs > NPC appelee !");
	}
	
	public abstract String getNameClass();
	
	public boolean estPret(){
		return this.pret;
	}
	
	public void setPret(boolean p){
		this.pret = p;
	}
	
	
}
