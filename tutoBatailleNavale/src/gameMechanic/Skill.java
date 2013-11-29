package gameMechanic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Skill {

	private int spCost;
	private String skillName;
	private ParticleEffect effect;

	public Skill(int spCost, String skillName, String skillParticuleName) {
		super();
		this.spCost = spCost;
		this.setSkillName(skillName);
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/"+skillParticuleName), Gdx.files.internal("effects"));
		effect.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		effect.start();
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
	
}
