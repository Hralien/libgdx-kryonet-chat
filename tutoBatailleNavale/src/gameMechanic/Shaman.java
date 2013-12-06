package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Shaman extends Personnage {

	private final static String DESCRIPTION = "Les shamans sont les piliers d’un bon groupe de combattant, leurs techniques de soutien sont indispensables pour sortir victorieux de la bataille. De par ses pouvoirs, un shaman ne peut s’aventurer seul car il est incapable de se défendre par ses propres moyens.";

	private static volatile TextureRegion[] regions = null;
	
	public Shaman() {
		super();
		super.hp = 50;
		super.sp=50;
		super.str=3;
		super.agi=2;
		super.intel=3;
		super.dex=3;
		super.luk=4;
		super.vit=3;

		super.listSkills.add(Skill.donDeVie);
		super.listSkills.add(Skill.soinDeMasse);
		super.listSkills.add(Skill.donDeMana);
		super.listSkills.add(Skill.restriction);

	}

	@Override
	public void write(Kryo kryo, Output output) {
		// output.setBuffer(new byte[128]);
		kryo.writeClassAndObject(output, name);
		output.writeShort(hp);
		output.writeShort(sp);
		output.writeShort(str);
		output.writeShort(agi);
		output.writeShort(intel);
		output.writeShort(dex);
		output.writeShort(luk);
		output.writeShort(vit);
		// output.writeString(name);
		// output.close();
		// kryo.writeClassAndObject(output, name);

	}

	@Override
	public void read(Kryo kryo, Input input) {
		name = (String) kryo.readClassAndObject(input);
		hp = input.readShort();
		sp = input.readShort();
		str = input.readShort();
		agi = input.readShort();
		intel = input.readShort();
		dex = input.readShort();
		luk = input.readShort();
		vit = input.readShort();
		// name = input.readString();

		// name = (String) kryo.readClassAndObject(input);
		// desc = DESCRIPTION;

	}

	@Override
	public String toString() {
		return "Shaman [hp=" + hp + ", sp=" + sp + ", str=" + str + ", agi="
				+ agi + ", intel=" + intel + ", dex=" + dex + ", luk=" + luk
				+ ", vit=" + vit + ", name=" + name + ", desc=" + DESCRIPTION
				+ "]";
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
