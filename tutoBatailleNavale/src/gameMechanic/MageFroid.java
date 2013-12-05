package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class MageFroid extends Personnage{
	

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
		super.sprite=new Texture(Gdx.files.internal("character/necromancien.png"));
		
		super.listSkills.add(Skill.prisonAcqeuse);
		super.listSkills.add(Skill.tridenAqueue);
		super.listSkills.add(Skill.massedAir);
		super.listSkills.add(Skill.cyclone);
		
		super.getRegions()[0] = new TextureRegion(super.sprite, 0, 0, 32, 44);
		super.getRegions()[1] = new TextureRegion(super.sprite, 32, 0, 32, 44);
		super.getRegions()[2] = new TextureRegion(super.sprite, 64, 0, 32, 44);
		super.getRegions()[3] = new TextureRegion(super.sprite, 96, 0, 32, 44);
		super.getRegions()[4] = new TextureRegion(super.sprite, 128, 0, 28, 44);
		super.getRegions()[5] = new TextureRegion(super.sprite, 160, 0, 26, 44);
		super.getRegions()[6] = new TextureRegion(super.sprite, 191, 0, 30, 44);
		super.getRegions()[7] = new TextureRegion(super.sprite, 0, 44, 41, 20);
		super.getRegions()[8] = new TextureRegion(super.sprite, 64, 44, 41, 20);
		
		super.desc="Le mage froid est expert dans la maitrise des éléments air et eau, il a fini son entrainement et est maintenant prêt pour le combat. Il complète parfaitement un mage chaud.";

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
		output.writeString(getDesc());

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
		desc =input.readString();
	}
}
