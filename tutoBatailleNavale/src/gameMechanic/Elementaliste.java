package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Elementaliste extends Personnage{
	

	public Elementaliste(String name) {
		super(name);
		super.hp=50;
		super.intel=3;
		super.sp=5*super.intel;
		super.str=3;
		super.agi=2;
		super.vit=3;
		super.dex=3;
		super.luk=4;
		super.sprite=new Texture(Gdx.files.internal("character/necromancien.png"));
		
		listSkills.add(Skill.decharge);
		listSkills.add(Skill.rocher);
		listSkills.add(Skill.boule);
		
		super.regions[0] = new TextureRegion(super.sprite, 0, 0, 32, 44);
		super.regions[1] = new TextureRegion(super.sprite, 32, 0, 32, 44);
		super.regions[2] = new TextureRegion(super.sprite, 64, 0, 32, 44);
		super.regions[3] = new TextureRegion(super.sprite, 96, 0, 32, 44);
		super.regions[4] = new TextureRegion(super.sprite, 128, 0, 28, 44);
		super.regions[5] = new TextureRegion(super.sprite, 160, 0, 26, 44);
		super.regions[6] = new TextureRegion(super.sprite, 191, 0, 30, 44);
		super.regions[7] = new TextureRegion(super.sprite, 0, 44, 41, 20);
		super.regions[8] = new TextureRegion(super.sprite, 64, 44, 41, 20);

	}
}
