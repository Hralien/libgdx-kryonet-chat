package m4ges.views;

import java.util.ArrayList;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.Skeleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.esotericsoftware.kryonet.Client;

/**
 * Premier ecran en début de partie
 * @author Florian
 *
 */
public class BeginingScreen extends AbstractScreen {
	/**
	 * design en json place dans les assets de android
	 * {@link Skin}
	 */
	private Skin skin;
	/**
	 * {@link Stage}
	 */
	private Stage stage;
	private Sound sound;

	private SpriteBatch batch;

	private Texture bg;


	public BeginingScreen(MyGame myGame){
		super(myGame);

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		bg = new Texture(Gdx.files.internal("moon.png"));

		batch = new SpriteBatch();
	}



	@Override
	public void resize (int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
		sound.dispose();
		batch.dispose();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		batch.begin();
		batch.draw(bg, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);

	}

	@Override
	public void show() {
		//on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		int i=0;
		for(final Personnage it : super.game.playersConnected){
//			batch.draw(it.dessine()[0], 100+i, 100+i);
			it.setVisible(true);
			it.setOrigin(width/2+i, height/3+i);
			it.setBounds(width/2+i, height/3+i, it.dessine()[0].getRegionWidth(), it.dessine()[0].getRegionHeight());
			it.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"down");
					return true;
				}
				
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"up");
				}
			});
			i+=50;
			stage.addActor(it);
		}
		ArrayList<Personnage> mob = new ArrayList<Personnage>();
		mob.add(new Skeleton());
		i=0;

		for(final Personnage it: mob){
			it.setVisible(true);
			it.setOrigin(width/3+i, height/2+i);
			it.setBounds(width/3+i, height/2+i, it.dessine()[0].getRegionWidth(), it.dessine()[0].getRegionHeight());
			it.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"down");
					return true;
				}
				
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"up");
				}
			});
			i+=50;
			stage.addActor(it);
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

}

