package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Necromancien extends Personnage {
	
	private final static String DESCRIPTION = "Le n�cromancien est un support utile mais pas indispensable, il provoque des d�g�ts non n�gligeables et peut paralyser les ennemis.";
	private static volatile TextureRegion[] regions = null;

	
	
	public Necromancien() {
		super();
		super.hp=50;
		super.intel=3;
		super.sp=5*super.intel;
		super.str=3;
		super.agi=2;
		super.vit=3;
		super.dex=3;
		super.luk=4;
		
		super.listSkills=Skill.getSkillForClass(Personnage.NECROMANCIEN);

	}
	@Override
	public void write(Kryo kryo, Output output) {
		output.writeShort(hp);
		output.writeShort(sp);
		output.writeShort(str);
		output.writeShort(agi);
		output.writeShort(intel);
		output.writeShort(dex);
		output.writeShort(luk);
		output.writeShort(vit);
		kryo.writeClassAndObject(output, name);

	}

	@Override
	public void read(Kryo kryo, Input input) {
		hp = input.readShort();
		sp = input.readShort();
		str = input.readShort();
		agi = input.readShort();
		intel = input.readShort();
		dex = input.readShort();
		luk = input.readShort();
		vit = input.readShort();

		name = (String) kryo.readClassAndObject(input);
	}
	@Override
	public String getDesc() {
		return DESCRIPTION;
	}
	/**
	 * M�thode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	public TextureRegion[] dessine() {
		//Le "Double-Checked Singleton"/"Singleton doublement v�rifi�" permet 
		//d'�viter un appel co�teux � synchronized, 
		//une fois que l'instanciation est faite.
		if (Necromancien.regions == null) {
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
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
