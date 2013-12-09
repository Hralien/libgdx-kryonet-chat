package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Aquamancien extends Personnage {
	
	private final static String DESCRIPTION = "L'aqueromancien ne paye peut �tre pas de mine, mais son pouvoir de destruction, combin� � celui d'un pyromancien, en font un adversaire redoutable.";
	private static volatile TextureRegion[] regions = null;

	public Aquamancien() {
		super();
		super.hp=50;
		super.intel=3;
		super.sp=5*super.intel;
		super.str=3;
		super.agi=2;
		super.vit=3;
		super.dex=3;
		super.luk=4;
		
		super.listSkills=Skill.getSkillForClass(Personnage.AQUAMANCIEN);

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
		// TODO Auto-generated method stub
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
		if (Aquamancien.regions == null) {
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
			// Il est TRES important.
			synchronized(Aquamancien.class) {
				if (Aquamancien.regions == null) {
					Texture sprite = new Texture(Gdx.files.internal("character/aquamancien.png"));
					regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 30, 50);
					regions[1] = new TextureRegion(sprite, 30, 0, 33, 50);
					regions[2] = new TextureRegion(sprite, 64, 0, 32, 50);
					regions[3] = new TextureRegion(sprite, 95, 0, 32, 50);
					regions[4] = new TextureRegion(sprite, 127, 0, 33, 50);
					regions[5] = new TextureRegion(sprite, 160, 0, 29, 50);
					regions[6] = new TextureRegion(sprite, 189, 0, 30, 50);
					regions[7] = new TextureRegion(sprite, 0, 50, 51, 26);
					regions[8] = new TextureRegion(sprite, 70, 50, 51, 26);
				}
			}
		}
		return Aquamancien.regions;
	}
}
