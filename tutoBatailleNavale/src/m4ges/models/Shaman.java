package m4ges.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Shaman extends Personnage {

	private final static String DESCRIPTION = "Le chamane est l'élément vital du groupe, son but étant de garder ses alliés en vie par tous les moyens.";

	private static volatile TextureRegion[] regions = null;
	
	public Shaman() {
		super();
		super.hp=100;
		super.mana=100;
		super.strength=10;
		super.speed=10;
		super.intel=10;
		
		super.listSkills=Skill.getSkillForClass(Personnage.SHAMANE);

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
	public String toString() {
		return "Shaman [hp=" + getHp() + ", mana=" + getMana() + ", strength=" + strength
				+ ", speed=" + speed + ", name=" + name + "]";
	}
	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return DESCRIPTION;
	}
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	public  TextureRegion[] dessine() {
		//Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
		//d'éviter un appel coûteux à synchronized, 
		//une fois que l'instanciation est faite.
		if (Shaman.regions == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Shaman.class) {
				if (Shaman.regions == null) {
					Texture sprite = new Texture(Gdx.files.internal("character/shaman.png"));
					regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 32, 44);
					regions[1] = new TextureRegion(sprite, 32, 0, 32, 44);
					regions[2] = new TextureRegion(sprite, 64, 0, 32, 44);
					regions[3] = new TextureRegion(sprite, 96, 0, 32, 44);
					regions[4] = new TextureRegion(sprite, 128, 0, 28, 44);
					regions[5] = new TextureRegion(sprite, 160, 0, 26, 44);
					regions[6] = new TextureRegion(sprite, 191, 0, 30, 44);
					regions[7] = new TextureRegion(sprite, 0, 44, 41, 20);
					regions[8] = new TextureRegion(sprite, 64, 44, 41, 20);
				}
			}
		}
		return Shaman.regions;
	}

}
