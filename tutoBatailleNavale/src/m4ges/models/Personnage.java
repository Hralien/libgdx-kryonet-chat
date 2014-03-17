package m4ges.models;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.Serializable;
/**
 * Classe representant un personnae
 * @author Florian
 *
 */
public abstract class Personnage extends Actor{

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
	protected String name;
	protected int element;
	protected ArrayList<Skill> listSkills;
	//permet de connaitre le tour de jeu
	protected boolean token;
	
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
		this.token = false;
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Skill> getListSkills() {
		return listSkills;
	}

	private void subit(int damage){
		if(this.hp-damage <= 0){
			this.state = MORT;
			this.hp = 0;
		}
		else
			this.hp -= damage;
		notify();
		
	}
	
	private void lance(int mana){
		if(this.mana - mana <= 0)
			this.mana = 0;
		else
			this.mana -= mana;
	}
	
//	@Override
//	public void write(Json json) {
//		// TODO Auto-generated method stub
//		json.writeField(name, "name");
//		json.writeField(hp,"hp");
//		json.writeField(hpMax,"hpMax");
//		json.writeField(mana,"mana");
//		json.writeField(manaMax,"manaMax");
//		json.writeField(strength,"strength");
//		json.writeField(speed,"speed");
//		json.writeField(intel,"intel");
//	}
//
//	@Override
//	public void read(Json json, JsonValue jsonData) {
//		json.readField(String.class,"name", jsonData);
//		json.readField(Integer.class,"hp", jsonData);
//		json.readField(Integer.class,"hpMax", jsonData);
//		json.readField(Integer.class, "mana", jsonData);
//		json.readField(Integer.class, "manaMax", jsonData);
//		json.readField(Integer.class,"strength", jsonData);
//		json.readField(Integer.class, "speed",jsonData);
//		json.readField(Integer.class, "intel", jsonData);
//
//	}

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
		return "Personnage [hp=" + hp + ", hpMax=" + hpMax + ", mana=" + mana
				+ ", manaMax=" + manaMax + ", strength=" + strength
				+ ", speed=" + speed + ", intel=" + intel + ", name=" + name
				+ ", element=" + element + ", listSkills=" + listSkills
				+ ", token=" + token + ", state=" + state + ", currentFrame="
				+ currentFrame + ", stateTime=" + stateTime + "]";
	}

	public abstract String getNameClass();
}
