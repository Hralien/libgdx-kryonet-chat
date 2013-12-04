package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Premier ecran en début de partie
 * @author Florian
 *
 */
public class BeginingScreen implements Screen {
	/**
	 * design en json place dans les assets de android
	 * {@link Skin}
	 */
	private Skin skin;
	/**
	 * {@link Stage}
	 */
	private Stage stage;
	/**
	 * label pour montrer les fps
	 */
	private Label fpsLabel;

	/**
	 * objet {@link MyGame} pour pouvoir changer d'ecran et recup des infos generales
	 */
	private MyGame game;

	private Sound sound;
	
	private SpriteBatch batch;
	
	private Texture bg;

	
	public BeginingScreen(MyGame myGame){
		this.game=myGame;
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		sound = Gdx.audio.newSound(Gdx.files.internal("sound/CornFields.mp3"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		bg = new Texture(Gdx.files.internal("moon.png"));
		
		
		batch = new SpriteBatch();

		fpsLabel = new Label("fps:", skin);



		

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
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

		

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

