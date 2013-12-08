package gameMechanic;

import java.util.ArrayList;
import java.util.List;

import chat.Network.SkillNumber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.me.mygdxgame.MyGame;

public class Skill implements Cloneable{

	private int id;
	private int spCost;
	private String skillName;
	// private ParticleEffect effect;
	private Sound sound;

	/** Shaman */
	public static Skill donDeVie = new Skill(1, 1, "don de vie", "ultima", 5, 6);
	public static Skill soinDeMasse = new Skill(2, 1, "soin de Masse","heal", 5, 4);
	public static Skill donDeMana = new Skill(3, 1, "don de Mana", "dondemana", 5,10);
	public static Skill restriction = new Skill(4, 1, "restriction", "eclair",5, 10);
	/** Necromencien */
	public static Skill volDeVie = new Skill(5, 1, "vol de Vie", "voldevie", 5, 5);
	public static Skill paralysie = new Skill(6, 1, "paralysie", "paralysie", 5, 6);
	public static Skill protection = new Skill(7, 1, "protection", "protection", 5,	5);
	public static Skill boost = new Skill(8, 1, "boost", "boost", 5, 5);
	/** Mage chaud */
	public static Skill bouleDeFeu = new Skill(9, 1, "boule de feu", "brasier",5, 4);
	public static Skill lanceArdente = new Skill(10, 1, "lance ardente","brulure", 5, 8);
	public static Skill chocDeMasse = new Skill(11, 1, "cyclone", "cyclone", 5,6);
	public static Skill foudroiement = new Skill(12, 1, "foudroiement","tonnerre", 5, 6);
	/** Mage froid */
	public static Skill prisonAcqeuse = new Skill(13, 1, "prison aqueuse","prisonaqueuse", 5, 5);
	public static Skill tridenAqueue = new Skill(14, 1, "hydrocanon","hydrocanon", 5, 6);
	public static Skill massedAir = new Skill(15, 1, "séisme", "seisme", 5, 4);
	public static Skill cyclone = new Skill(16, 1, "blizard", "blizard", 5, 6);

	public static ArrayList<Skill> listSkill = new ArrayList<Skill>();

	/** Skill sprite management */
	private static volatile TextureAtlas atlas;

	private int frame_cols; // #1
	private int frame_rows; // #2

	Animation walkAnimation; // #3
	TextureRegion currentFrame; // #7

	float stateTime; // #8
	private boolean skillEffectEnded;
	/**
	 * 
	 * @param id 
	 * @param spCost
	 * @param skillName
	 * @param skillEffect old name of skillsheet before packing
	 * @param frame_cols number of colone
	 * @param frame_rows number of rows
	 */
	public Skill(int id, int spCost, String skillName, String skillEffect,
			int frame_cols, int frame_rows) {
		super();
		this.id = id;
		this.spCost = spCost;
		this.skillName = skillName;
		this.sound = Gdx.audio.newSound(Gdx.files.internal("sound/_heal_effect.wav"));
		this.frame_cols = frame_cols;
		this.frame_rows = frame_rows;

//		Array<AtlasRegion> ar = getInstance().findRegions(skillEffect);
//		System.err.println(ar.size);
//		for(AtlasRegion it :ar){
//			TextureRegion[][] tmp = TextureRegion.split(it.getTexture(), it.getRegionWidth()/this.frame_cols, it.getRegionHeight()/this.frame_rows);
//			TextureRegion[] walkFrames = new TextureRegion[this.frame_cols*this.frame_rows];
//			int index = 0;
//			for (int i = 0; i < this.frame_rows; i++) {
//				for (int j = 0; j < this.frame_cols; j++) {
//					walkFrames[index++] = tmp[i][j];
//				}
//			}
//			
//		}
		
		AtlasRegion ar = getInstance().findRegion(skillEffect);
		TextureRegion[][] tmp = TextureRegion.split(ar.getTexture(), ar.getRegionWidth()/this.frame_cols, ar.getRegionHeight()/this.frame_rows);
		TextureRegion[] walkFrames = new TextureRegion[this.frame_cols*this.frame_rows];
		int index = 0;
		for (int i = 0; i < this.frame_rows; i++) {
			for (int j = 0; j < this.frame_cols; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(0.05f, walkFrames);
		stateTime = 0f; // #13

		

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
			System.err.println("ended");
		}
		return currentFrame;
	}
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	private final static TextureAtlas getInstance() {
		//Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
		//d'éviter un appel coûteux à synchronized, 
		//une fois que l'instanciation est faite.
		if (Skill.atlas == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized(Skill.class) {
				if (Skill.atlas == null) {
					Skill.atlas = MyGame.manager.get("effects/skill.pack", TextureAtlas.class);
				}
			}
		}
		return Skill.atlas;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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
