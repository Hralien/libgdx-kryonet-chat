package gameMechanic;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.esotericsoftware.kryo.KryoSerializable;

public abstract class Personnage extends Actor implements KryoSerializable, IPlayer {

	protected int hp;
	protected int sp;
	protected int str;
	protected int agi;
	protected int intel;
	protected int dex;
	protected int luk;
	protected int vit;
	
	protected String name;

	protected ArrayList<Skill> listSkills;

	private int frameToDraw;
	
	public Personnage() {
		this.listSkills = new ArrayList<Skill>();
        setTouchable(Touchable.enabled);
        this.setOrigin(50, 50);

	}
	
	@Override
	public void sayHello(){
		System.err.println("hello");
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Skill> getListSkills() {
		return listSkills;
	}
	public abstract String getDesc();
	public TextureRegion[] dessine() {
		return null;
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(dessine()[frameToDraw],getOriginX(),getOriginY());
	}

	public static boolean pointInRectangle (TextureRegion sprite,float spriteX, float spriteY,  float touchX, float touchY) {
		System.err.println("sprite width"+sprite.getRegionWidth()+"sprite height"+sprite.getRegionHeight()+"spriteX"+spriteX+"spriteY"+spriteY+"touchX"+touchX+"touchY"+touchY);
	    return spriteX <= touchX && spriteX + sprite.getRegionWidth() >= touchX && spriteY <= touchY && spriteY + sprite.getRegionHeight() >= touchY;
	}
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled) return null;
//		System.err.println("[personnage] is hit");
		return x >= 0 && x < dessine()[frameToDraw].getRegionWidth() && y >= 0 && y < dessine()[frameToDraw].getRegionHeight() ? this : null;
	}
}
