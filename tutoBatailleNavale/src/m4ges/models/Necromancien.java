package m4ges.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Necromancien extends Personnage {
	
	private final static String DESCRIPTION = "Le nécromancien est un adepte de la magie noire et utilise la manipulation pour parvenir à détruire plus facilement son adversaire.";
	private static volatile TextureRegion[] regions = null;

	
	
	public Necromancien() {
		super();
		super.hp=70;
		super.mana=110;
		super.strength=13;
		super.speed=10;
		
		super.listSkills=Skill.getSkillForClass(Personnage.NECROMANCIEN);

	}
	@Override
	public void write(Kryo kryo, Output output) {
		output.writeShort(hp);
		output.writeShort(mana);
		output.writeShort(strength);
		output.writeShort(speed);
		kryo.writeClassAndObject(output, name);

	}

	@Override
	public void read(Kryo kryo, Input input) {
		hp = input.readShort();
		mana = input.readShort();
		strength = input.readShort();
		speed = input.readShort();

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
		if (Necromancien.regions == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Necromancien.class) {
				if (Necromancien.regions == null) {
					Texture sprite = new Texture(Gdx.files.internal("character/necromancien.png"));
					regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 31, 46);
					regions[1] = new TextureRegion(sprite, 31, 0, 32, 46);
					regions[2] = new TextureRegion(sprite, 64, 0, 32, 46);
					regions[3] = new TextureRegion(sprite, 95, 0, 33, 46);
					regions[4] = new TextureRegion(sprite, 128, 0, 33, 46);
					regions[5] = new TextureRegion(sprite, 160, 0, 26, 46);
					regions[6] = new TextureRegion(sprite, 192, 0, 30, 46);
					regions[7] = new TextureRegion(sprite, 0, 46, 48, 26);
					regions[8] = new TextureRegion(sprite, 69, 46, 48, 26);
				}
			}
		}
		return Necromancien.regions;
	}
}
