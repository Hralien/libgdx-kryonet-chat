package gameMechanic;

import java.util.ArrayList;
import chat.Network.SkillNumber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Skill implements Cloneable {

	private int id;
	private int spCost;
	private String skillName;
	// private ParticleEffect effect;
	private Sound sound;

	/** Shaman */
	public static Skill donDeVie = new Skill(1, 1, "don de vie", "seisme", 5, 4);
	public static Skill soinDeMasse = new Skill(2, 1, "soin de Masse",
			"seisme", 5, 4);
	public static Skill donDeMana = new Skill(3, 1, "don de Mana", "seisme", 5,
			4);
	public static Skill restriction = new Skill(4, 1, "restriction", "seisme",
			5, 4);
	/** Necromencien */
	public static Skill volDeVie = new Skill(5, 1, "vol de Vie", "seisme", 5, 4);
	public static Skill paralysie = new Skill(6, 1, "paralysie", "seisme", 5, 4);
	public static Skill protection = new Skill(7, 1, "protection", "seisme", 5,
			4);
	public static Skill boost = new Skill(8, 1, "boost", "seisme", 5, 4);
	/** Mage chaud */
	public static Skill bouleDeFeu = new Skill(9, 1, "boule de feu", "seisme",
			5, 4);
	public static Skill lanceArdente = new Skill(10, 1, "lance ardente",
			"seisme", 5, 4);
	public static Skill chocDeMasse = new Skill(11, 1, "cyclone", "seisme", 5,
			4);
	public static Skill foudroiement = new Skill(12, 1, "foudroiement",
			"seisme", 5, 4);
	/** Mage froid */
	public static Skill prisonAcqeuse = new Skill(13, 1, "prison aqueuse",
			"seisme", 5, 4);
	public static Skill tridenAqueue = new Skill(14, 1, "trident à queue",
			"seisme", 5, 4);
	public static Skill massedAir = new Skill(15, 1, "séisme", "seisme", 5, 4);
	public static Skill cyclone = new Skill(16, 1, "cyclone", "seisme", 5, 4);

	public static ArrayList<Skill> listSkill = new ArrayList<Skill>();

	/** Skill sprite management */
	private int frame_cols = 5; // #1
	private int frame_rows = 4; // #2

	Animation walkAnimation; // #3
	Texture walkSheet; // #4
	TextureRegion[] walkFrames; // #5
	TextureRegion currentFrame; // #7

	float stateTime; // #8
	private boolean skillEffectEnded;

	public Skill(int id, int spCost, String skillName, String skillEffect,
			int frame_cols, int frame_rows) {
		super();
		this.id = id;
		this.spCost = spCost;
		this.skillName = skillName;
		this.sound = Gdx.audio.newSound(Gdx.files
				.internal("sound/_heal_effect.wav"));
		this.frame_cols = frame_cols;
		this.frame_rows = frame_rows;
		// effect = new ParticleEffect();
		// effect.load(Gdx.files.internal("effects/"+skillParticuleName),
		// Gdx.files.internal("effects"));
		// effect.setPosition(Gdx.graphics.getWidth() / 2,
		// Gdx.graphics.getHeight() / 2);
		// effect.start();
		// skill effect
		walkSheet = new Texture(Gdx.files.internal("effects/" + skillEffect
				+ ".png")); // #9
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / frame_cols, walkSheet.getHeight()
				/ frame_rows); // #10
		walkFrames = new TextureRegion[frame_cols * frame_rows];
		int index = 0;
		for (int i = 0; i < frame_rows; i++) {
			for (int j = 0; j < frame_cols; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(0.05f, walkFrames); // #11
		stateTime = 0f; // #13

	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static void initListSkill() {
		/** shaman */
		Skill.listSkill.add(Skill.donDeVie);
		Skill.listSkill.add(Skill.soinDeMasse);
		Skill.listSkill.add(Skill.donDeMana);
		Skill.listSkill.add(Skill.restriction);
		/** necro */
		Skill.listSkill.add(Skill.volDeVie);
		Skill.listSkill.add(Skill.paralysie);
		Skill.listSkill.add(Skill.protection);
		Skill.listSkill.add(Skill.boost);
		/** mage chaud */
		Skill.listSkill.add(Skill.bouleDeFeu);
		Skill.listSkill.add(Skill.lanceArdente);
		Skill.listSkill.add(Skill.chocDeMasse);
		Skill.listSkill.add(Skill.foudroiement);
		/** mage froid */
		Skill.listSkill.add(Skill.prisonAcqeuse);
		Skill.listSkill.add(Skill.tridenAqueue);
		Skill.listSkill.add(Skill.massedAir);
		Skill.listSkill.add(Skill.cyclone);
	}

	public static Skill selectSkillFromSkillNumber(SkillNumber skillnum) {
		for (Skill it : listSkill) {
			if (it.id == skillnum.skillId)
				return it;
		}
		return null;
	}

	public TextureRegion afficheSkill() {
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = walkAnimation.getKeyFrame(stateTime, false); // #16
		if (walkAnimation.isAnimationFinished(stateTime)) {
			setSkillEffectEnded(true);
			System.err.println("eneded");
		}
		return currentFrame;
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

	// public ParticleEffect getEffect() {
	// return effect;
	// }
	//
	//
	// public void setEffect(ParticleEffect effect) {
	// this.effect = effect;
	// }

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public boolean isSkillEffectEnded() {
		return skillEffectEnded;
	}

	public void setSkillEffectEnded(boolean skillEffectEnded) {
		this.skillEffectEnded = skillEffectEnded;
	}

}
