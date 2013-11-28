package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Shaman extends Personnage{
	

	public Shaman(String name) {
		super(name);
		super.hp=50;
		super.sp=5*super.intel;
		super.str=3;
		super.agi=2;
		super.vit=3;
		super.intel=3;
		super.dex=3;
		super.luk=4;
		super.sprite=new Texture(Gdx.files.internal("character/acolyte.png"));
		
		listSkills.add(new Skill(1, "heal.png","explosion.p"));
		listSkills.add(new Skill(1, "blessing.png","explosion.p"));
		listSkills.add(new Skill(1, "shield.png","flame.p"));
		
		super.regions[0] = new TextureRegion(super.sprite, 0, 0, 34, 71);          // #3
		super.regions[1] = new TextureRegion(super.sprite, 0.5f, 0f, 1f, 0.5f);    // #4
		super.regions[2] = new TextureRegion(super.sprite, 0, 63, 64, 64);         // #5
		super.regions[3] = new TextureRegion(super.sprite, 0.5f, 0.5f, 1f, 1f);    // #6

	}
}
