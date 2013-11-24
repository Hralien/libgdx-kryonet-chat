package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

/**
 * Menu principal
 * selection de ce qu'on veut faire
 * @author Florian
 *
 */
public class MenuPrincipalScreen implements Screen {

	private Skin skin;
	private Stage stage;
	private SpriteBatch batch;
	private Texture texture1;
	private Texture texture2;
	private Label fpsLabel;
	private int buttonSelected;
	private MyGame game;
	public MenuPrincipalScreen(MyGame game){
		this.game=game;
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);

		// Group.debug = true;

		TextButton tbChat = new TextButton("creer un chat", skin);
		TextButton tbHost = new TextButton("heberger une partie", skin);
		TextButton tbJoin = new TextButton("rejoindre une partie", skin);

		tbChat.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				buttonSelected = 1;
			}
		});
		tbHost.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				buttonSelected = 2;
			}
		});
		tbJoin.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				buttonSelected = 3;
			}
		});
		fpsLabel = new Label("fps:", skin);


		// window.debug();
		Window window = new Window("Selection Perso", skin);
		window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(650, 200);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.row();
		window.add(tbChat);
		window.row();
		window.add(tbHost);
		window.row();
		window.add(tbJoin);
		window.row();
		window.add(fpsLabel).colspan(4);
		window.pack();

		// stage.addActor(new Button("Behind Window", skin));
		stage.addActor(window);

	}


	@Override
	public void resize (int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		
		if (buttonSelected!=0){ // use your own criterion here
			switch (buttonSelected) {
			case 1:
	            game.setScreen(game.anotherScreen);			
				break;
			case 2:
	            game.setScreen(game.anotherScreen);			
				break;
			case 3:
	            game.setScreen(game.anotherScreen);			
				break;
			default:
				break;
			}
		}


		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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

