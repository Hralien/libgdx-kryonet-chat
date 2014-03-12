package m4ges.models.monster;

import m4ges.models.Personnage;


public abstract class Monstre extends Personnage{


	public int attaque(){
		return super.strength*3;
	}

	//TODO IDEM
	//UNIQUEMENT POUR LES ATTAQUES D'UN NPC VERS UN JOUEURS
	public void attaque(Personnage p){
		System.out.println("Methode d'attaque NPC > joueurs appelee");
	}
}
