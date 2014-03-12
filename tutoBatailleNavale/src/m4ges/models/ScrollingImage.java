package m4ges.models;


import m4ges.util.Constants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
* @author Mats Svensson
*/
public class ScrollingImage extends Actor {

    TextureRegion reg;
    float stateTime;

    public ScrollingImage(TextureRegion bg) {
        reg = bg;
        stateTime = 0;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
    }

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
    	System.err.println("b:"+batch+"\nreg:"+reg+"\nstate"+stateTime+"x:"+getX()+"y:"+getY());
        batch.draw(reg, (float) (getX()*(1.5+stateTime)), getY(), 100,100);
    }
}