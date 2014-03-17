package m4ges.models.classes;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;

import m4ges.models.Item;
import m4ges.models.Personnage;
import m4ges.models.Skill;

public abstract class Joueur extends Personnage{
	
	private boolean pret;
	private ArrayList<Item> inventaire;
	private String macAddress;
	
	public Joueur(){
		super();
		pret = false;
		setInventaire(new ArrayList<Item>());
	}

	@Override
	public abstract String getDesc();

	@Override
	public abstract Animation animate();

	public abstract byte[] getBytes();
	
	//UNIQUEMENT POUR LES ATTAQUES D'UN JOUEUR VERS UN NPC
	public void attaque(Personnage p, Skill s){
		System.out.println("Methode attaque joueurs > NPC appelee !");
		System.out.println(p);
		//resurection
		if(s.getId()==4 && p.getHp() <= 0 ){
			p.setState(Personnage.WAIT);
			p.setHp(p.getHpMax());
		}
		else{
			if(p.getHp() > 0){
				p.setHp(p.getHp() - s.getDamage());
				if(p.getHp() <=0){
					p.setHp(0);
					p.setState(Personnage.MORT);
				}
			}
		}
		this.setMana(this.getMana() - s.getSpCost());
		
		
	}
	
	public abstract String getNameClass();
	
	public boolean estPret(){
		return this.pret;
	}
	
	public void setPret(boolean p){
		this.pret = p;
	}

	public ArrayList<Item> getInventaire() {
		return inventaire;
	}

	public void setInventaire(ArrayList<Item> inventaire) {
		this.inventaire = inventaire;
	}
	
	
}
