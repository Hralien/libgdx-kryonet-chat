package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class MageFroid extends Personnage {
	
	private final static String DESCRIPTION = "Le mage froid est expert dans la maitrise des �l�ments air et eau, il a fini son entrainement et est maintenant pr�t pour le combat. Il compl�te parfaitement un mage chaud.";
	private static volatile TextureRegion[] regions = null;

	public MageFroid() {
		super();
		super.hp=50;
		super.intel=3;
		super.sp=5*super.intel;
		super.str=3;
		super.agi=2;
		super.vit=3;
		super.dex=3;
		super.luk=4;
		
		super.listSkills.add(Skill.prisonAcqeuse);
		super.listSkills.add(Skill.tridenAqueue);
		super.listSkills.add(Skill.massedAir);
		super.listSkills.add(Skill.cyclone);
	
		

	}
	@Override
	public void write(Kryo kryo, Output output) {
		output.writeInt(hp, true);
		output.writeInt(sp, true);
		output.writeInt(str, true);
		output.writeInt(agi, true);
		output.writeInt(intel, true);
		output.writeInt(dex, true);
		output.writeInt(luk, true);
		output.writeInt(vit, true);
		output.writeString(name);

	}

	@Override
	public void read(Kryo kryo, Input input) {
		hp = input.readInt();
		sp = input.readInt();
		str = input.readInt();
		agi = input.readInt();
		intel = input.readInt();
		dex = input.readInt();
		luk = input.readInt();
		vit = input.readInt();

		name = input.readString();
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
		if (MageFroid.regions == null) {
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
			// Il est TRES important.
			synchronized(MageFroid.class) {
				if (MageFroid.regions == null) {
					Texture sprite = new Texture(Gdx.files.internal("character/necromancien.png"));
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
		return MageFroid.regions;
	}
}
