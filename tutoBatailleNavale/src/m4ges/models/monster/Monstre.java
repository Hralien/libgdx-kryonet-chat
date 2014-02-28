package m4ges.models.monster;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import m4ges.models.Personnage;

public abstract class Monstre extends Personnage{

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
		hp = (input.readShort());
		mana = (input.readShort());
		strength = input.readShort();
		speed = input.readShort();
		intel = input.readShort();
		name = (String) kryo.readClassAndObject(input);
	}
	public int attaque(){
		return super.strength*3;
	}
}
