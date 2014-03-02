package m4ges.controllers;

import m4ges.util.Constants;
import m4ges.util.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class AbstractScreen implements com.badlogic.gdx.Screen {

	protected MyGame game;
	protected Label fpsLabel;
	protected Skin skin;
	protected SpriteBatch batch;
	protected OrthographicCamera cameraGUI;

	private static volatile long timePlayed;
	
	public AbstractScreen(MyGame game) {
		this.game = game;
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		this.fpsLabel = new Label("fps:", skin);
		this.batch = new SpriteBatch();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.setToOrtho(false); // flip y-axis
		cameraGUI.update();
		timePlayed = getTimePlayed();
	}

	@Override
	public void pause() {
	};

	@Override
	public void resume() {
	};

	@Override
	public void dispose() {
		
	};

	@Override
	public void hide() {
	};

	@Override
	public void show() {
	};
	
	public void destroy() {
	};

	@Override
	public void render(float arg0) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cameraGUI.combined);

		batch.begin();
		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		fpsLabel.setPosition(200, 200);
		fpsLabel.draw(batch, arg0);
		batch.end();
		

	}

	@Override
	public void resize(int width, int height) {
		batch.setProjectionMatrix(cameraGUI.combined);
	}
	
	/**
     * Méthode permettant de renvoyer une instance de la classe Singleton
     * @return Retourne l'instance du singleton.
     */
    public final static long getTimePlayed() {
        //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
        //d'éviter un appel coûteux à synchronized, 
        //une fois que l'instanciation est faite.
        if (AbstractScreen.timePlayed == 0) {
           // Le mot-clé synchronized sur ce bloc empêche toute instanciation
           // multiple même par différents "threads".
           // Il est TRES important.
           synchronized(AbstractScreen.class) {
             if (AbstractScreen.timePlayed == 0) {
            	 AbstractScreen.timePlayed = TimeUtils.millis();
             }
           }
        }
        return AbstractScreen.timePlayed;
    }

}