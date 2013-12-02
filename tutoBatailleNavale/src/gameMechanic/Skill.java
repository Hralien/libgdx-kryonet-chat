package gameMechanic;

import java.util.ArrayList;
import chat.Network.SkillNumber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.me.mygdxgame.MyGame;

public class Skill {

	private int id;
	private int spCost;
	private String skillName;
	private ParticleEffect effect;
	private Sound sound;

	public static Skill heal = new Skill(1, 1, "heal","explosion.p");
	public static Skill blessing = new Skill(2, 1, "blessing","explosion.p");
	public static Skill shield = new Skill(3, 1, "shield","flame.p");
	public static Skill volDeVie= new Skill(4,1, "vol de vie","explosion.p");
	public static Skill resurection = new Skill(5,1, "resurection","explosion.p");
	public static Skill peur = new Skill(6,1, "Peur","flame.p");
	public static Skill decharge =new Skill(7,1, "Décharge d'eclair","explosion.p");
	public static Skill rocher =new Skill(8,1, "Rocher","explosion.p");
	public static Skill boule =new Skill(9,1, "boule de feu","flame.p");
	public static  ArrayList<Skill> listSkill = new ArrayList<Skill>();

	public Skill(int id, int spCost, String skillName, String skillParticuleName) {
		super();
		this.id = id;
		this.spCost = spCost;
		this.skillName =skillName;
		this.sound  = Gdx.audio.newSound(Gdx.files.internal("sound/_heal_effect.wav"));
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/"+skillParticuleName), Gdx.files.internal("effects"));
		effect.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		effect.start();

	}
	
	public static void initListSkill(){
		Skill.listSkill.add(Skill.heal);
		Skill.listSkill.add(Skill.blessing);
		Skill.listSkill.add(Skill.shield);
		Skill.listSkill.add(Skill.volDeVie);
		Skill.listSkill.add(Skill.resurection);
		Skill.listSkill.add(Skill.peur);
		Skill.listSkill.add(Skill.decharge);
		Skill.listSkill.add(Skill.rocher);
		Skill.listSkill.add(Skill.boule);
	}

	public static Skill selectSkillFromSkillNumber(SkillNumber skillnum){
		for(Skill it: listSkill){
			if(it.id==skillnum.skillId)
				return it;
		}
		return null;
	}
	public int getId() {
		return id;
	}


	public String getSkillName() {
		return skillName;
	}


	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}


	public int getSpCost() {
		return spCost;
	}


	public void setSpCost(int spCost) {
		this.spCost = spCost;
	}


	public ParticleEffect getEffect() {
		return effect;
	}


	public void setEffect(ParticleEffect effect) {
		this.effect = effect;
	}


	public Sound getSound() {
		return sound;
	}


	public void setSound(Sound sound) {
		this.sound = sound;
	}

}
