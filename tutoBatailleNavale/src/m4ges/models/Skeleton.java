package m4ges.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Skeleton extends Personnage {
	
	private final static String DESCRIPTION = "Le Skeleton est ou n'est pas ...";
	private static volatile TextureRegion[] regions = null;

	
	
	public Skeleton() {
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
		output.writeShort(hp);
		output.writeShort(mana);
		output.writeShort(strength);
		output.writeShort(speed);
		output.writeShort(intel);

		kryo.writeClassAndObject(output, name);

	}

	@Override
	public void read(Kryo kryo, Input input) {
		hp = input.readShort();
		mana = input.readShort();
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
	public TextureRegion[] dessine() {
		//Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
		//d'éviter un appel coûteux à synchronized, 
		//une fois que l'instanciation est faite.
		if (Skeleton.regions == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Skeleton.class) {
				if (Skeleton.regions == null) {
					Texture sprite = new Texture(Gdx.files.internal("character/skeleton.png"));
					regions = new TextureRegion[8]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 128, 100);
					regions[1] = new TextureRegion(sprite, 128, 0, 128, 100);
					regions[2] = new TextureRegion(sprite, 256, 0, 128, 100);
					regions[3] = new TextureRegion(sprite, 384, 0, 128, 100);
					regions[4] = new TextureRegion(sprite, 0, 100, 128, 200);
					regions[5] = new TextureRegion(sprite, 128, 100, 128, 200);
					regions[6] = new TextureRegion(sprite, 256, 100, 128, 200);
					regions[7] = new TextureRegion(sprite, 384, 100, 128, 200);

				}
			}
		}
		return Skeleton.regions;
	}
	@Override
	public String getName(){
		return getClass().getSimpleName();
	}
}
