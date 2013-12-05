package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Shaman extends Personnage{
	
	public final static String DESCRIPTION = "Les shamans sont les piliers d’un bon groupe de combattant, leurs techniques de soutien sont indispensables pour sortir victorieux de la bataille. De par ses pouvoirs, un shaman ne peut s’aventurer seul car il est incapable de se défendre par ses propres moyens.";

	public Shaman() {
		super();
		super.hp=50;
		super.sp=50;
		super.str=3;
		super.agi=2;
		super.intel=3;
		super.dex=3;
		super.luk=4;
		super.vit=3;
		super.sprite=new Texture(Gdx.files.internal("character/shaman.png"));
		
		super.listSkills.add(Skill.donDeVie);
		super.listSkills.add(Skill.soinDeMasse);
		super.listSkills.add(Skill.donDeMana);
		super.listSkills.add(Skill.restriction);
		if(super.sprite !=null){
			super.getRegions()[0] = new TextureRegion(super.sprite, 0, 0, 32, 44);
			super.getRegions()[1] = new TextureRegion(super.sprite, 32, 0, 32, 44);
			super.getRegions()[2] = new TextureRegion(super.sprite, 64, 0, 32, 44);
			super.getRegions()[3] = new TextureRegion(super.sprite, 96, 0, 32, 44);
			super.getRegions()[4] = new TextureRegion(super.sprite, 128, 0, 28, 44);
			super.getRegions()[5] = new TextureRegion(super.sprite, 160, 0, 26, 44);
			super.getRegions()[6] = new TextureRegion(super.sprite, 191, 0, 30, 44);
			super.getRegions()[7] = new TextureRegion(super.sprite, 0, 44, 41, 20);
			super.getRegions()[8] = new TextureRegion(super.sprite, 64, 44, 41, 20);
		}
		super.desc = "hi";
	}
	@Override
	public void write(Kryo kryo, Output output) {
		output.setBuffer(new byte[1024]);
		output.writeInt(hp, true);
		output.writeInt(sp, true);
		output.writeInt(str, true);
		output.writeInt(agi, true);
		output.writeInt(intel, true);
		output.writeInt(dex, true);
		output.writeInt(luk, true);
		output.writeInt(vit, true);
		output.writeString(name);
		
//        kryo.writeClassAndObject(output, name);

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
//        name = (String) kryo.readClassAndObject(input);
        //		desc = DESCRIPTION;

	}
	@Override
	public String toString() {
		return "Shaman [hp=" + hp + ", sp=" + sp + ", str=" + str + ", agi="
				+ agi + ", intel=" + intel + ", dex=" + dex + ", luk=" + luk
				+ ", vit=" + vit + ", name=" + name + ", desc=" + desc + "]";
	}
	
	
}
