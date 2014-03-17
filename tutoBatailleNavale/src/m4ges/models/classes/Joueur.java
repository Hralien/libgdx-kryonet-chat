package m4ges.models.classes;

import java.util.ArrayList;

import m4ges.models.Item;
import m4ges.models.Personnage;
import m4ges.models.Skill;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public abstract class Joueur extends Personnage implements Serializable{
	
	private boolean pret;
	private ArrayList<Item> inventaire;
	private String macAddress;
	
	public Joueur(){
		super();
		pret = false;
		setInventaire(new ArrayList<Item>());
	}

	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
		json.writeObjectStart(getNameClass());
//		json.writeValue("classe", getNameClass());
//		json.writeField(name, "name");
//		json.writeField(hp,"hp");
//		json.writeField(hpMax,"hpMax");
//		json.writeField(mana,"mana");
//		json.writeField(manaMax,"manaMax");
//		json.writeField(strength,"strength");
//		json.writeField(speed,"speed");
//		json.writeField(intel,"intel");
		json.writeFields(this);
		System.out.println(this.name);
		json.writeField(this.name, "name");
		json.writeObjectEnd();
		String s="";
		json.fromJson(Joueur.class, s);
		System.out.println(s);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		json.readFields(this, jsonData);
//		jsonData.child().name();
//		jsonData.child().asString();
//		this.name=jsonData.child().asString();
//		this.hp= jsonData.child().asInt();
//		this.hpMax= jsonData.child().asInt();
//		this.mana= jsonData.child().asInt();
//		this.manaMax= jsonData.child().asInt();
//		this.strength= jsonData.child().asInt();
//		this.speed= jsonData.child().asInt();
//		this.intel= jsonData.child().asInt();
		
	}
	//UNIQUEMENT POUR LES ATTAQUES D'UN JOUEUR VERS UN NPC OU UN SOIN
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
		System.out.println(" COUT  : " + s.getSpCost());
		this.setMana(this.getMana() - s.getSpCost());
		
		
	}
	@Override
	public abstract String getDesc();

	@Override
	public abstract Animation animate();

	public abstract byte[] getBytes();
	
	
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
