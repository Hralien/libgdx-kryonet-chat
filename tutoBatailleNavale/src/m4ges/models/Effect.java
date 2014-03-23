package m4ges.models;

import java.util.HashMap;

import m4ges.controllers.MyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Effect extends Actor {
	private int id;
	private String nom;
	private Animation effectAnimation;
	private TextureRegion currentFrame;
	private float stateTime;

	private static HashMap<Integer,Effect> mapEffect;
	
	private static volatile TextureAtlas atlas;

	public Effect(int id,String effectName, int frame_cols, int frame_rows) {
		this.setId(id);
		this.setNom(effectName);
		TextureRegion animsheet = new TextureRegion(
				Initialisation().findRegion(effectName));
		TextureRegion[][] tmp = animsheet.split(animsheet.getRegionWidth()
				/ frame_cols, animsheet.getRegionHeight() / frame_rows); // #10
		TextureRegion[] effectFrames = new TextureRegion[frame_cols
		                                                 * frame_rows];
		int index = 0;
		for (int i = 0; i < frame_rows; i++) {
			for (int j = 0; j < frame_cols; j++) {
				effectFrames[index++] = tmp[i][j];
			}
		}
		effectAnimation = new Animation(0.08f, effectFrames); // #11
		stateTime = 0f;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = effectAnimation.getKeyFrame(stateTime, true); // #16
		batch.draw(currentFrame, this.getX(), this.getY()); // #17
	}

	public static Effect selectEffectFromEffectID(int effectId) {
		if(mapEffect.containsKey(effectId)){
			return mapEffect.get(effectId);
		}
		return null;
	}
	private static void buildListEffect() {
		mapEffect = new HashMap<Integer, Effect>();
		mapEffect.put(0, new Effect(0, "Brulure", 6,1));
		mapEffect.put(1, new Effect(1, "Gele", 6,1));


	}
	
	/**
	 * Méthode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
	 */
	public final static TextureAtlas Initialisation() {
		// Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet
		// d'éviter un appel coûteux à synchronized,
		// une fois que l'instanciation est faite.
		if (Effect.atlas == null) {
			// Le mot-clé synchronized sur ce bloc empêche toute instanciation
			// multiple même par différents "threads".
			// Il est TRES important.
			synchronized (Effect.class) {
				if (Effect.atlas == null) {
					Effect.atlas = MyGame.manager.get("effects/skill.pack",
							TextureAtlas.class);
					buildListEffect();
				}
			}
		}
		return Effect.atlas;
	}
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
