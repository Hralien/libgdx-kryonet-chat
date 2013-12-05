package gameMechanic;

import java.util.ArrayList;
import chat.Network.SkillNumber;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.mygdxgame.MyGame;

public class Skill {

	private int id;
	private int spCost;
	private String skillName;
	private ParticleEffect effect;
	private Sound sound;

	/**Shaman*/
	public static Skill donDeVie = new Skill(1, 1, "don de vie","explosion.p");
	public static Skill soinDeMasse = new Skill(2, 1, "soin de Masse","explosion.p");
	public static Skill donDeMana = new Skill(3, 1, "don de Mana","flame.p");
	public static Skill restriction= new Skill(4,1, "restriction","explosion.p");
	/**Necromencien*/
	public static Skill volDeVie = new Skill(5,1, "vol de Vie","explosion.p");
	public static Skill paralysie = new Skill(6,1, "paralysie","flame.p");
	public static Skill protection =new Skill(7,1, "protection","explosion.p");
	public static Skill boost =new Skill(8,1, "boost","explosion.p");
	/**Mage chaud */
	public static Skill bouleDeFeu =new Skill(9,1, "boule de feu","flame.p");
	public static Skill lanceArdente =new Skill(9,1, "lance ardente","flame.p");
	public static Skill chocDeMasse =new Skill(9,1, "choc de masse","flame.p");
	public static Skill foudroiement =new Skill(9,1, "foudroiement","flame.p");
	/**Mage froid */
	public static Skill prisonAcqeuse =new Skill(9,1, "prison aqueuse","flame.p");
	public static Skill tridenAqueue =new Skill(9,1, "trident à queue","flame.p");
	public static Skill massedAir =new Skill(9,1, "masse d'air","flame.p");
	public static Skill cyclone =new Skill(9,1, "cyclone","flame.p");

	public static  ArrayList<Skill> listSkill = new ArrayList<Skill>();
	/**Skill sprite management*/
	private static final int        FRAME_COLS = 6;         // #1
	private static final int        FRAME_ROWS = 5;         // #2

	Animation                       walkAnimation;          // #3
	Texture                         walkSheet;              // #4
	TextureRegion[]                 walkFrames;             // #5
	SpriteBatch                     spriteBatch;            // #6
	TextureRegion                   currentFrame;           // #7

	float stateTime;                                        // #8

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
		/**shaman*/
		Skill.listSkill.add(Skill.donDeVie);
		Skill.listSkill.add(Skill.soinDeMasse);
		Skill.listSkill.add(Skill.donDeMana);
		Skill.listSkill.add(Skill.restriction);
		/**necro*/
		Skill.listSkill.add(Skill.volDeVie);
		Skill.listSkill.add(Skill.paralysie);
		Skill.listSkill.add(Skill.protection);
		Skill.listSkill.add(Skill.boost);
		/**mage chaud*/
		Skill.listSkill.add(Skill.bouleDeFeu);
		Skill.listSkill.add(Skill.lanceArdente);
		Skill.listSkill.add(Skill.chocDeMasse);
		Skill.listSkill.add(Skill.foudroiement);
		/**mage froid*/
		Skill.listSkill.add(Skill.prisonAcqeuse);
		Skill.listSkill.add(Skill.tridenAqueue);
		Skill.listSkill.add(Skill.massedAir);
		Skill.listSkill.add(Skill.cyclone);
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
