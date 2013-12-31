package m4ges.models;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.esotericsoftware.kryo.KryoSerializable;

public abstract class Personnage extends Actor implements KryoSerializable {

	protected int hp;
	protected int mana;
	protected int strength;
	protected int speed;
	protected int intel;
	
	protected String name;

	protected ArrayList<Skill> listSkills;

	private int frameToDraw;
	public static final int SHAMANE=0;
	public static final int NECROMANCIEN=1;
	public static final int PYROMANCIEN=2;
	public static final int AQUAMANCIEN=3;


	public Personnage() {
		this.listSkills = new ArrayList<Skill>();
        setTouchable(Touchable.enabled);
        this.setOrigin(50, 50);
	}
	
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(dessine()[frameToDraw],getOriginX(),getOriginY());
	}

	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled) return null;
//		System.err.println("[personnage] is hit");
		return x >= 0 && x < dessine()[frameToDraw].getRegionWidth() && y >= 0 && y < dessine()[frameToDraw].getRegionHeight() ? this : null;
	}
	public abstract String getDesc();
	public TextureRegion[] dessine() {
		return null;
	}
	public int getFrameToDraw() {
		return frameToDraw;
	}

	public void setFrameToDraw(int frameToDraw) {
		this.frameToDraw = frameToDraw;
	}
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
}
