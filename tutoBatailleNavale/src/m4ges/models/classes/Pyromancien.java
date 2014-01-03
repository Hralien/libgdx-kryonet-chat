package m4ges.models.classes;

import m4ges.models.Personnage;
import m4ges.models.Skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Pyromancien extends Personnage {
	
	private final static String DESCRIPTION = "Le pyromancien est un adepte de la puissance. Aspirant à la destruction, mieux vaut ne pas l'énerver";
	protected static volatile Animation animation;

	public Pyromancien() {
		super();
		super.hp=90;
		super.mana=110;
		super.strength=8;
		super.speed=11;
		super.intel=12;
		
		super.listSkills=Skill.getSkillForClass(Personnage.PYROMANCIEN);

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
		// TODO Auto-generated method stub
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
		if (Pyromancien.animation == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Pyromancien.class) {
				if (Pyromancien.animation == null) {
					Texture sprite = new Texture(Gdx.files.internal("character/pyromancien.png"));
					TextureRegion[] regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 32, 44);
					regions[1] = new TextureRegion(sprite, 32, 0, 29, 44);
					regions[2] = new TextureRegion(sprite, 61, 0, 33, 44);
					regions[3] = new TextureRegion(sprite, 93, 0, 32, 44);
					regions[4] = new TextureRegion(sprite, 125, 0, 34, 44);
					regions[5] = new TextureRegion(sprite, 160, 0, 26, 44);
					regions[6] = new TextureRegion(sprite, 190, 0, 32, 44);
					regions[7] = new TextureRegion(sprite, 0, 44, 49, 27);
					regions[8] = new TextureRegion(sprite, 70, 44, 49, 27);
					
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Pyromancien.animation;
	}
}
