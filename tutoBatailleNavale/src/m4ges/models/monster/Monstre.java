package m4ges.models.monster;

import java.util.ArrayList;

import m4ges.models.Item;
import m4ges.models.Personnage;


public abstract class Monstre extends Personnage{

	protected ArrayList<Item> dropPossible;


	protected  String description;
	
	public Monstre() {
		super();
		dropPossible = new ArrayList<Item>();
	}

	public int attaque(){
		return super.strength*3;
	}

	//TODO IDEM
	//UNIQUEMENT POUR LES ATTAQUES D'UN NPC VERS UN JOUEURS
	public void attaque(Personnage p){
		System.out.println("Methode d'attaque NPC > joueurs appelee");
	}
	public ArrayList<Item> getDropPossible() {
		return dropPossible;
	}

	public void setDropPossible(ArrayList<Item> dropPossible) {
		this.dropPossible = dropPossible;
	}
}
