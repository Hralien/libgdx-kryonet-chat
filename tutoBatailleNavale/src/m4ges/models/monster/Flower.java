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
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Flower extends Personnage {
	
	private final static String DESCRIPTION = "Le Flower est ou n'est pas ...";
	protected static volatile Animation animation;

	public Flower() {
		super();
		super.name="Flower";
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
		if (Flower.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Flower.class) {
				if (Flower.animation == null) {
					TextureAtlas atlas = MyGame.manager.get("character/personnage.pack", TextureAtlas.class);
					AtlasRegion sprite = atlas.findRegion("flower");
					TextureRegion[] regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 64, 64);
					regions[1] = new TextureRegion(sprite, 64, 0, 64, 64);
					regions[2] = new TextureRegion(sprite, 128, 0, 64, 64);
					regions[3] = new TextureRegion(sprite, 192, 0, 64, 64);
					regions[4] = new TextureRegion(sprite, 0, 64, 64, 64);
					regions[5] = new TextureRegion(sprite, 64, 64, 64, 64);
					regions[6] = new TextureRegion(sprite, 128, 64, 64, 64);
					regions[7] = new TextureRegion(sprite, 192, 64, 64, 64);
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Flower.animation;
	}
	@Override
	public String getName(){
		return getClass().getSimpleName();
	}
}
