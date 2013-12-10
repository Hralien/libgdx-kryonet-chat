package m4ges.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Shaman extends Personnage {

	private final static String DESCRIPTION = "Le shamane est l'élément vital du groupe, son but étant de garder ses alliés en vie par tous les moyens.";

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

		super.listSkills=Skill.getSkillForClass(Personnage.SHAMANE);


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
