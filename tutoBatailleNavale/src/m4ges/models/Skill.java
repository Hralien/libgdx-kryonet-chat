package m4ges.models;

import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Skill extends Actor implements Cloneable {

	private int id;
	private int spCost;
	private int damage;
	private String skillName;
	// private ParticleEffect effect;
	private String soundPath;

	public static ArrayList<Skill> listSkill = new ArrayList<Skill>();

	/** Skill sprite management */
	private static volatile TextureAtlas atlas;


	private Animation skillAnimation; // #3
	private TextureRegion currentFrame; // #7

	private float stateTime; // #8
	private boolean playingSound;

	public Skill() {
		super();
	}

	/**
	 * 
	 * @param id
	 * @param spCost
	 * @param skillName
	 * @param skillEffect
	 *            old name of skillsheet before packing
	 * @param frame_cols
	 *            number of colone
	 * @param frame_rows
	 *            number of rows
	 */
	public Skill(int id, int spCost, String skillName, String skillEffect,
			int damage, int frame_cols, int frame_rows) {
		super();
		this.id = id;
		this.spCost = spCost;
		this.damage = damage;
		this.skillName = skillName;
		this.soundPath = "sound/"+skillEffect+".wav";


		AtlasRegion spritesheet = getInstance().findRegion(skillEffect);
		// System.err.println("name:"+skillEffect+"width"+spritesheet.getRegionWidth()+"hieght"+spritesheet.getRegionHeight());
		TextureRegion[] walkFrames = new TextureRegion[frame_cols
		                                               * frame_rows];
		int width = spritesheet.getRegionWidth() / frame_cols;
		int height = spritesheet.getRegionHeight() / frame_rows;
		int index = 0;
		for (int i = 0; i < frame_cols; i++) {
			for (int j = 0; j < frame_rows; j++) {
				walkFrames[index] = new TextureRegion(spritesheet, i * width, j
						* height, width, height);
				// System.err.println("index:"+index);
				index++;

			}
		}

		// System.err.println(walkFrames.length);
		skillAnimation = new Animation(0.1f, walkFrames);
		// System.err.println("skillanim"+skillName+"lenght"+skillAnimation.animationDuration);
		skillAnimation.setPlayMode(Animation.NORMAL);
		stateTime = 0f; // #13
		currentFrame = skillAnimation.getKeyFrame(0);
		super.setSize(currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight());
	}

	/**
	 * return la liste des skills d'une classe de personnage
	 * @param classID
	 * @return
	 */
	public static ArrayList<Skill> getSkillForClass(int classID) {
		//TODO:recuperer les skills de listskill et non en cr�er de nouveau
		ArrayList<Skill> list = new ArrayList<Skill>();
		switch (classID) {
		case 0:
			/** Shaman */
			list.add(new Skill(1, 5, "soin", "Soin", -10, 5, 4));
			list.add(new Skill(2, 7, "motivation", "Motivation", 0, 5, 7));
			list.add(new Skill(3, 4, "ressistance", "Resistance", 0, 5, 4));
			list.add(new Skill(4, 10, "resurrection", "Resurrection", 0, 5, 7));
			break;
		case 1:
			/** Necromencien */
			list.add(new Skill(5, 8, "abimes", "Abimes", 10, 5, 6));
			list.add(new Skill(6, 7, "mal�diction", "Malediction", 10, 5, 6));
			list.add(new Skill(7, 6, "empoisonement", "Empoisonnement", 10, 5,
					5));
			list.add(new Skill(8, 5, "perturbation", "Perturbation", 10, 5, 5));
			break;
		case 2:
			/** Mage chaud */
			list.add(new Skill(9, 5, "deflagration", "Deflagration", 10, 5, 4));
			list.add(new Skill(10, 10, "foudre", "Foudre", 10, 5, 10));
			list.add(new Skill(11, 6, "choc sismique", "Choc_sismique", 10, 5,
					4));
			list.add(new Skill(12, 8, "combustion", "Combustion", 10, 5, 8));
			break;
		case 3:
			/** Mage froid */
			list.add(new Skill(13, 6, "geyser", "Geyser", 10, 5, 6));
			list.add(new Skill(14, 6, "gel", "Gel", 10, 5, 6));
			list.add(new Skill(15, 6, "tornade", "Tornade", 10, 5, 6));
			list.add(new Skill(16, 5, "rafale", "Rafale", 10, 5, 5));
			break;
		}
		return list;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if(!playingSound){
			Sound m  = Gdx.audio.newSound(Gdx.files.internal(soundPath));
			m.setVolume(0, 0.2f);
			m.play();
			playingSound=true;
		}
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		if ((stateTime <= skillAnimation.animationDuration / 5)) {
			currentFrame = skillAnimation.getKeyFrame(stateTime, false); // #16
			batch.draw(currentFrame, this.getX(), this.getY()); // #17
		} else {
			//animation finie, on la vire du parent

			this.remove();
			playingSound=false;
		}
	}

	/**
	 * M�thode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
	 */
	private final static TextureAtlas getInstance() {
		// Le "Double-Checked Singleton"/"Singleton doublement v�rifi�" permet
		// d'�viter un appel co�teux � synchronized,
		// une fois que l'instanciation est faite.
		if (Skill.atlas == null) {
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
			// Il est TRES important.
			synchronized (Skill.class) {
				if (Skill.atlas == null) {
					Skill.atlas = MyGame.manager.get("effects/skill.pack",
							TextureAtlas.class);
					buildListSkill();
				}
			}
		}
		return Skill.atlas;
	}
	/**
	 * 
	 * construit la list des skills possibles
	 */
	public static void buildListSkill() {
		listSkill = new ArrayList<Skill>();

		/** Shaman */
		listSkill.add(new Skill(1, 5, "soin", "Soin", -10, 5, 4));
		listSkill.add(new Skill(2, 7, "motivation", "Motivation", 0, 5, 7));
		listSkill.add(new Skill(3, 4, "ressistance", "Resistance", 0, 5, 4));
		listSkill.add(new Skill(4, 10, "resurrection", "Resurrection", 0, 5, 7));


		/** Necromencien */
		listSkill.add(new Skill(5, 8, "abimes", "Abimes", 10, 5, 6));
		listSkill.add(new Skill(6, 7, "mal�diction", "Malediction", 10, 5, 6));
		listSkill.add(new Skill(7, 6, "empoisonement", "Empoisonnement", 10, 5,
				5));
		listSkill.add(new Skill(8, 5, "perturbation", "Perturbation", 10, 5, 5));


		/** Mage chaud */
		listSkill.add(new Skill(9, 5, "deflagration", "Deflagration", 10, 5, 4));
		listSkill.add(new Skill(10, 10, "foudre", "Foudre", 10, 5, 10));
		listSkill.add(new Skill(11, 6, "choc sismique", "Choc_sismique", 10, 5,
				4));
		listSkill.add(new Skill(12, 8, "combustion", "Combustion", 10, 5, 8));

		/** Mage froid */
		listSkill.add(new Skill(13, 6, "geyser", "Geyser", 10, 5, 6));
		listSkill.add(new Skill(14, 6, "gel", "Gel", 10, 5, 6));
		listSkill.add(new Skill(15, 6, "tornade", "Tornade", 10, 5, 6));
		listSkill.add(new Skill(16, 5, "rafale", "Rafale", 10, 5, 5));

	}

	public static Skill selectSkillFromSkillID(int skillnum) {
		for (Skill it : listSkill) {
			if (it.id == skillnum) {
				return it;
			}
		}
		return null;
	}

	/**
	 * Reset animation.
	 * 
	 * You can use this to ensure the animation plays from the start again. It's
	 * handy if you have one-shot animations like explosions but you are using
	 * re-usable Sprites. You must reset the animation to ensure the animation
	 * plays back again.
	 */
	public void resetAnimation() {
		stateTime = 0;
	}

	/**
	 * Check to see if animation finished.
	 * 
	 * @param stateTime
	 * 
	 * @return True if finished.
	 */
	public boolean isAnimationFinished() {
		return skillAnimation.isAnimationFinished(stateTime);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getId() {
		return id;
	}

	public int getDamage(){
		return this.damage;
	}

	public String getSkillName() {
		return skillName;
	}

	public int getSpCost() {
		return spCost;
	}


	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public byte[] getBytes() {
		byte[] data = new byte[5];
		data[0] = Constants.LANCERSKILL;
		data[1] = (byte) id;
		// data[2] = lanceur.;
		return data;
	}
}
