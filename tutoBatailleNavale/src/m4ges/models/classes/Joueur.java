package m4ges.models.classes;

import com.badlogic.gdx.graphics.g2d.Animation;
import m4ges.models.Personnage;
import m4ges.models.Skill;

public abstract class Joueur extends Personnage{

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
}
