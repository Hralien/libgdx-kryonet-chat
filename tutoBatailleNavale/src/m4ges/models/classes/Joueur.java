package m4ges.models.classes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import m4ges.models.Personnage;
import m4ges.models.Skill;

public abstract class Joueur extends Personnage{

	@Override
	public void write(Kryo kryo, Output output) {
		// TODO Auto-generated method stub
		output.writeShort(getHp());
		output.writeShort(getMana());
		output.writeShort(strength);
		output.writeShort(speed);
		output.writeShort(intel);
		kryo.writeClassAndObject(output, name);
	}

	@Override
	public void read(Kryo kryo, Input input) {
		// TODO Auto-generated method stub
		setHp(input.readShort());
		setMana(input.readShort());
		strength = input.readShort();
		speed = input.readShort();
		intel = input.readShort();
		name = (String) kryo.readClassAndObject(input);
	}

	@Override
	public abstract String getDesc();

	@Override
	public abstract Animation animate();

	public abstract byte[] getBytes();
	
	//TODO a faire (hey oui)
	//UNIQUEMENT POUR LES ATTAQUES D'UN JOUEUR VERS UN NPC
	public void attaque(Personnage p, Skill s){
		System.out.println("Methode attaque joueurs > NPC appelee !");
	}
}
