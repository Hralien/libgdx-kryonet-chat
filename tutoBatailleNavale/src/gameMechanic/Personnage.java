package gameMechanic;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public abstract class Personnage implements KryoSerializable {

	protected int hp;
	protected int sp;
	protected int str;
	protected int agi;
	protected int intel;
	protected int dex;
	protected int luk;
	protected int vit;

	protected String name;
	protected String desc;

	protected ArrayList<Skill> listSkills;
	protected Texture sprite;
	protected TextureRegion[] regions;

	public Personnage() {
		this.listSkills = new ArrayList<Skill>();
		this.regions = new TextureRegion[9]; // #2
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

	public void setListSkills(ArrayList<Skill> listSkills) {
		this.listSkills = listSkills;
	}

	public Texture getSprite() {
		return sprite;
	}

	public void setSprite(Texture sprite) {
		this.sprite = sprite;
	}
	public TextureRegion[] getRegions() {
		return regions;
	}
	public void setRegions(TextureRegion[] regions) {
		this.regions = regions;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
