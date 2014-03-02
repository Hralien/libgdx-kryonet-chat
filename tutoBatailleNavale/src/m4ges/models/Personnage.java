package m4ges.models;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.esotericsoftware.kryo.KryoSerializable;
/**
 * Classe representant un personnae
 * @author Florian
 *
 */
public abstract class Personnage extends Actor implements KryoSerializable {

	//classe
	public static final int SHAMAN=0;
	public static final int NECROMANCIEN=1;
	public static final int PYROMANCIEN=2;
	public static final int AQUAMANCIEN=3;
	
	//etat
	public static final int COMPLETE=0;	
	public static final int MORT=1;
	public static final int WAIT=2;

	//stats
	protected int hp;
	protected int mana;
	protected int strength;
	protected int speed;
	protected int intel;
	protected String name;
	protected int element;
	protected ArrayList<Skill> listSkills;
	
	//animation
	protected int state;
	protected TextureRegion currentFrame;
	float stateTime;

	
	public Personnage() {
		this.listSkills = new ArrayList<Skill>();
		this.state=COMPLETE;
		this.stateTime=0;
		this.currentFrame = null;
		setTouchable(Touchable.enabled);
		this.setOrigin(50, 50);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {

		switch (state) {
		case COMPLETE:
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = animate().getKeyFrame(stateTime, true);
			batch.draw(currentFrame,getOriginX(),getOriginY());
			break;
		case MORT:
			currentFrame = animate().getKeyFrame(9, true);
			batch.draw(currentFrame,getOriginX(),getOriginY());
			break;
		case WAIT:
			currentFrame = animate().getKeyFrame(0, true);
			batch.draw(currentFrame,getX(),getY());
			break;
		default:
			break;
		}
		this.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		this.setBounds(getX(), getY(), currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	}

	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled) return null;
		return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight() ? this : null;
	}
	public abstract String getDesc();
	public abstract Animation animate();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Skill> getListSkills() {
		return listSkills;
	}


	public int getHp() {
		return hp;
	}


	public void setHp(int hp) {
		this.hp = hp;
	}


	public int getMana() {
		return mana;
	}


	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Personnage [hp=" + hp + ", mana=" + mana + ", strength="
				+ strength + ", speed=" + speed + ", intel=" + intel
				+ ", name=" + name + ", listSkills=" + listSkills + ", state="
				+ state + ", currentFrame=" + currentFrame + ", stateTime="
				+ stateTime + "]";
	}
	
	private void writeObject(ObjectOutputStream io) throws IOException{
		io.writeBytes(name);
		io.writeInt(hp);
	}
	
	public byte[] getBytes(){
		//Cette méthode ne devrait pas etre appeler
		return null;
	}
	
	//TODO a faire (hey oui)
	//UNIQUEMENT POUR LES ATTAQUES D'UN JOUEUR VERS UN NPC
	public void attaque(Personnage p, Skill s){
		System.out.println("Methode attaque joueurs > NPC appelee !");
	}
	
	//TODO IDEM
	//UNIQUEMENT POUR LES ATTAQUES D'UN NPC VERS UN JOUEURS
	public void attaque(Personnage p){
		System.out.println("Methode d'attaque NPC > joueurs appelee");
	}
	
}
