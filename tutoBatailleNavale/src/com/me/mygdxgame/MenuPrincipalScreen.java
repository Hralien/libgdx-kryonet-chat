package com.me.mygdxgame;

import java.io.IOException;
import chat.ChatServer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Menu principal
 * selection de ce qu'on veut faire
 * @author Florian
 *
 */
public class MenuPrincipalScreen implements Screen {
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
	 * numero du bouton selectionne
	 */
	private int buttonSelected;
	/**
	 * objet {@link MyGame} pour pouvoir changer d'ecran et recup des infos generales
	 */
	MyGame game;


	public MenuPrincipalScreen(MyGame myGame){
		this.game=myGame;
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		//nos 3 boutons de selection
		TextButton tbChat = new TextButton("creer un chat", skin);
		TextButton tbHost = new TextButton("heberger un chat", skin);
		TextButton tbJoin = new TextButton("rejoindre une partie", skin);

		game.androidUI.showAlertBox("Server", "hello", "Button text");

		//nos listeners sur les boutons
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
				try {
					game.chatserver = new ChatServer();
					switch (Gdx.app.getType()) {
					case Android:
						game.androidUI.showAlertBox("Server", "Serveur created", "Button text");
						break;
					case Desktop:
						game.androidUI.showAlertBox("Server", "Serveur created", "Button text",stage);
						break;
					case WebGL:
						// HTML5 specific code
						break;
					default:
						// Other platforms specific code
					}
				} catch (IOException e) {
					e.printStackTrace();
					game.androidUI.showAlertBox("Server", "Serveur already created", "Button text");
					//					new Dialog("Some Dialog", skin, "dialog") {
					//						protected void result (Object object) {
					//						}
					//					}.text("Server already created").button("Yes", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
				}
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

		//on switch le num du bouton selectionner et son affiche le screen correspondant
		switch (buttonSelected) {
		case 1:

			game.setScreen(game.chatScreen);			
			break;
		case 2:
			//			game.setScreen(game.animatiopppppppnScreen);

			break;
		case 3:
			game.setScreen(game.createCharacterScreen);			
			break;
		default:
			break;
		}

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

