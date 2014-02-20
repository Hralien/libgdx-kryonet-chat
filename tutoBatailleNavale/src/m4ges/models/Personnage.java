package m4ges.models;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonSerializable;
import com.esotericsoftware.jsonbeans.JsonValue;
import com.esotericsoftware.kryo.KryoSerializable;

public abstract class Personnage extends Actor implements KryoSerializable {

	//classe
	public static final int SHAMANE=0;
	public static final int NECROMANCIEN=1;
	public static final int PYROMANCIEN=2;
	public static final int AQUAMANCIEN=3;
	
	//etat
	public static final int COMPLETE=0;	
	public static final int MORT=1;	

	//stats
	protected int hp;
	protected int mana;
	protected int strength;
	protected int speed;
	protected int intel;
	protected String name;
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

		default:
			break;
		}
		this.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	}

//	@Override
//	public Actor hit (float x, float y, boolean touchable) {
//		if (touchable && getTouchable() != Touchable.enabled) return null;
//		//		System.err.println("[personnage] is hit");
//		return x >= 0 && x < currentFrame.getRegionWidth() && y >= 0 && y < currentFrame.getRegionHeight() ? this : null;
//	}
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

	@Override
	public String toString() {
		return "Personnage [hp=" + hp + ", mana=" + mana + ", strength="
				+ strength + ", speed=" + speed + ", intel=" + intel
				+ ", name=" + name + ", listSkills=" + listSkills + ", state="
				+ state + ", currentFrame=" + currentFrame + ", stateTime="
				+ stateTime + "]";
	}
}
