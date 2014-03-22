package m4ges.models;

import java.util.ArrayList;

import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
/**
 * Classe representant un personnae
 * @author Florian
 *
 */
public abstract class Personnage extends Actor {

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
	protected int hpMax;
	protected int mana;
	protected int manaMax;
	protected int strength;
	protected int speed;
	protected int intel;
	protected String nom;
	protected int element;
	protected ArrayList<Skill> listSkills;
	//permet de connaitre les effets actif sur le perso
	protected ArrayList<Integer> effet;
	//permet de connaitre le tour de jeu
	protected boolean token;
	
	//animation
	protected int state;
	protected TextureRegion currentFrame;
	protected float stateTime;

	
	public Personnage() {
		this.listSkills = new ArrayList<Skill>();
		this.effet = new ArrayList<Integer>();
		this.state=COMPLETE;
		this.stateTime=0;
		this.currentFrame = null;
		setTouchable(Touchable.enabled);
		this.token = false;
		this.setOrigin(50, 50);
	}
	
	public void addEffect(int effet){
		this.effet.add(effet);
	}
	
	public boolean isGele(){
		return this.effet.contains(Constants.GELE);
	}
	
	public boolean isResistant(){
		return this.effet.contains(Constants.RESISTANCE);
	}
	
	public void traiteEffet(){
		for(Integer e:this.effet){
			switch (e) {
			case Constants.COMBUSTION:
				this.perdreVie(5);
				break;

			default:
				break;
			}
		}
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
			currentFrame = animate().getKeyFrame(animate().getKeyFrameIndex(8));
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
		return nom;
	}

	public void setName(String name) {
		this.nom = name;
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

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hp_max) {
		this.hpMax = hp_max;
	}

	public int getManaMax() {
		return manaMax;
	}

	public void setManaMax(int mana_max) {
		this.manaMax = mana_max;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public boolean isToken(){
		return token;
	}
	
	public void setToken(boolean t){
		this.token = t;
	}

	@Override
	public String toString() {
		return "Personnage [hp=" + hp + ", mana=" + mana + ", strength="
				+ strength + ", speed=" + speed + ", intel=" + intel
				+ ", name=" + nom + ", listSkills=" + listSkills + ", state="
				+ state + ", currentFrame=" + currentFrame + ", stateTime="
				+ stateTime + "]";
	}
	
	public void perdreVie(int x){
		int hp = this.getHp();
		hp -= x;
		if(hp <= 0){
			this.setHp(0);
			this.setState(MORT);
		}
		else{
			this.setHp(hp);
		}
	}
	
}
