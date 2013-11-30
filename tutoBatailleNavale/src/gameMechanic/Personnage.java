package gameMechanic;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Personnage {
	
	public int hp;
	public int sp;
	public int str;
	public int agi;
	public int intel;
	public int dex;
	public int luk;
	public int vit;

	public String name;
	public ArrayList<Skill> listSkills;
	public Texture sprite;
    public TextureRegion[]	regions = new TextureRegion[7]; // #2


	public Personnage(String name){
		this.name=name;
		this.listSkills = new ArrayList<Skill>();
	}


}
