package m4ges.models.monster;


import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.Skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Phantom extends Personnage {
	
	private final static String DESCRIPTION = "Le Phantom est ou n'est pas ...";
	protected static volatile Animation animation;

	public Phantom() {
		super();
		super.hp=50;
		super.intel=3;
		super.mana=5*super.intel;
		super.strength=3;
		super.speed=2;

		
		super.listSkills=Skill.getSkillForClass(Personnage.NECROMANCIEN);

	}
	@Override
	public void write(Kryo kryo, Output output) {
		output.writeShort(getHp());
		output.writeShort(getMana());
		output.writeShort(strength);
		output.writeShort(speed);
		output.writeShort(intel);

		kryo.writeClassAndObject(output, name);

	}

	@Override
	public void read(Kryo kryo, Input input) {
		setHp(input.readShort());
		setMana(input.readShort());
		strength = input.readShort();
		speed = input.readShort();
		intel = input.readShort();


		name = (String) kryo.readClassAndObject(input);
	}
	@Override
	public String getDesc() {
		return DESCRIPTION;
	}
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	public Animation animate() {
		//Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
		//d'éviter un appel coûteux à synchronized, 
		//une fois que l'instanciation est faite.
		if (Phantom.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Phantom.class) {
				if (Phantom.animation == null) {
			        TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
			        AtlasRegion sprite = atlas.findRegion("phantom");
			        TextureRegion[] regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 32, 50);
					regions[1] = new TextureRegion(sprite, 63, 0, 42, 50);
					regions[2] = new TextureRegion(sprite, 128, 0, 51, 50);
					regions[3] = new TextureRegion(sprite, 192, 0, 64, 50);
					regions[4] = new TextureRegion(sprite, 0, 50, 32, 50);
					regions[5] = new TextureRegion(sprite, 63, 50, 42, 50);
					regions[6] = new TextureRegion(sprite, 128, 50, 51, 50);
					regions[7] = new TextureRegion(sprite, 192, 50, 64, 50);
					
					animation = new Animation(0.1f, regions);              // #11


				}
			}
		}
		return Phantom.animation;
	}
	@Override
	public String getName(){
		return getClass().getSimpleName();
	}
}
