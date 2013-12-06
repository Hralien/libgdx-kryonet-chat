package gameMechanic;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.KryoSerializable;

public abstract class Personnage implements KryoSerializable, IPlayer {

	protected int hp;
	protected int sp;
	protected int str;
	protected int agi;
	protected int intel;
	protected int dex;
	protected int luk;
	protected int vit;

	protected String name;

	protected ArrayList<Skill> listSkills;

	public Personnage() {
		this.listSkills = new ArrayList<Skill>();
	}
	
	@Override
	public void sayHello(){
		System.err.println("hello");
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
	public abstract String getDesc();
	public TextureRegion[] dessine() {
		return null;
	}
	
}
