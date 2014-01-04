package m4ges.models;

import java.util.ArrayList;
import java.util.List;

import reseau.Network.SkillNumber;

import m4ges.controllers.MyGame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Skill extends Actor implements Cloneable{

	private int id;
	private int spCost;
	private String skillName;
	// private ParticleEffect effect;
	private Sound sound;

	public static ArrayList<Skill> listSkill = new ArrayList<Skill>();

	/** Skill sprite management */
	private static volatile TextureAtlas atlas;

	private int frame_cols; // #1
	private int frame_rows; // #2

	private Animation skillAnimation; // #3
	private TextureRegion currentFrame; // #7

	private float stateTime; // #8
	

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

		AtlasRegion spritesheet = getInstance().findRegion(skillEffect);
//		System.err.println("name:"+skillEffect+"width"+spritesheet.getRegionWidth()+"hieght"+spritesheet.getRegionHeight());
		TextureRegion[] walkFrames = new TextureRegion[this.frame_cols*this.frame_rows];
		int width = spritesheet.getRegionWidth()/this.frame_cols;
		int height = spritesheet.getRegionHeight()/this.frame_rows;
		int index = 0;
		for(int i = 0; i<this.frame_cols;i++){
			for(int j=0;j<this.frame_rows;j++){
				walkFrames[index] = new TextureRegion(spritesheet,i*width,j*height,width,height); 
//				System.err.println("index:"+index);
				index++;

			}
		}

//		System.err.println(walkFrames.length);
		skillAnimation = new Animation(0.1f, walkFrames);
//		System.err.println("skillanim"+skillName+"lenght"+skillAnimation.animationDuration);
		skillAnimation.setPlayMode(Animation.NORMAL);
		stateTime = 0f; // #13
		currentFrame = skillAnimation.getKeyFrame(0);
		super.setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
	}

	public static ArrayList<Skill> getSkillForClass(int classID){
		ArrayList<Skill> list = new ArrayList<Skill>();
		switch(classID){
		case 0:
			/** Shaman */
			list.add(new Skill(1, 1, "soin", "Soin", 5, 4));
			list.add(new Skill(2, 1, "motivation","Motivation", 5, 7));
			list.add(new Skill(3, 1, "ressistance", "Resistance", 5,4));
			list.add(new Skill(4, 1, "resurrection", "Resurrection",5, 7));
			break;
		case 1:
			/** Necromencien */
			list.add(new Skill(5, 1, "abimes", "Abimes", 5, 6));
			list.add(new Skill(6, 1, "malédiction", "Malediction", 5, 6));
			list.add(new Skill(7, 1, "empoisonement", "Empoisonnement", 5,5));
			list.add(new Skill(8, 1, "perturbation", "Perturbation", 5, 5));
			break;
		case 2:
			/** Mage chaud */
			list.add(new Skill(9, 1, "deflagration", "Deflagration",5, 4));
			list.add(new Skill(10, 1, "foudre","Foudre", 5, 10));
			list.add(new Skill(11, 1, "choc sismique", "Choc_sismique", 5,4));
			list.add(new Skill(12, 1, "combustion","Combustion", 5, 8));
			break;
		case 3:
			/** Mage froid */
			list.add(new Skill(13, 1, "geyser","Geyser", 5, 6));
			list.add(new Skill(14, 1, "gel","Gel", 5, 6));
			list.add(new Skill(15, 1, "tornade", "Tornade", 5, 6));
			list.add(new Skill(16, 1, "rafale", "Rafale", 5, 5));
			break;
		}
		return list;
	}

	public static Skill selectSkillFromSkillNumber(SkillNumber skillnum) {
		for (Skill it : listSkill) {
			if (it.id == skillnum.skillId)
				return it;
		}
		return null;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime();                       // #15
		System.err.println(stateTime);
		if((stateTime<=skillAnimation.animationDuration/5)){
			currentFrame = skillAnimation.getKeyFrame(stateTime, false);      // #16
			System.out.println("animation :"+skillAnimation.isAnimationFinished(stateTime));
			batch.draw(currentFrame, 300, 150);                     // #17
		}
		else{
			System.out.println("animation :"+skillAnimation.isAnimationFinished(stateTime));
		}
		//		if(stateTime>=(frame_cols*frame_rows+1)*skillAnimation.frameDuration)
		//			skillEffectEnded=true;

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
	/**
	 * Reset animation.
	 *
	 * You can use this to ensure the animation plays from the start again. It's
	 * handy if you have one-shot animations like explosions but you are using
	 * re-usable Sprites. You must reset the animation to ensure the animation
	 * plays back again.
	 */
	public void resetAnimation()
	{
		stateTime = 0;
	}

	/**
	 * Check to see if animation finished.
	 *
	 * @param stateTime
	 *
	 * @return True if finished.
	 */
	public boolean isAnimationFinished()
	{
		return skillAnimation.isAnimationFinished(stateTime);
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

	public int getSpCost() {
		return spCost;
	}

	public Sound getSound() {
		return sound;
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

}
