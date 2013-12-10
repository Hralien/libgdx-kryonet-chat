package m4ges.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class AbstractScreen implements com.badlogic.gdx.Screen {

	protected MyGame game;
	protected Label fpsLabel;
	protected Skin skin;
	public AbstractScreen(MyGame game) {
		this.game = game;
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		this.fpsLabel = new Label("fps:", skin);
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
		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

	}

	@Override
	public void resize(int arg0, int arg1) {
	}
}