package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Skill {

	private int spCost;
	private String skillName;
	public ParticleEffect effect;

	
	public Skill(int spCost, String skillName, String skillParticuleName) {
		super();
		this.spCost = spCost;
		this.skillName = skillName;
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/"+skillParticuleName), Gdx.files.internal("effects"));
		effect.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		effect.start();
	}
	
}
