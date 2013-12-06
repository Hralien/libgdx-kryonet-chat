package com.me.mygdxgame;

import java.io.IOException;
import chat.ChatServer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Menu principal selection de ce qu'on veut faire
 * 
 * @author Florian
 * 
 */
public class MenuPrincipalScreen implements Screen {
	/**
	 * design en json place dans les assets de android {@link Skin}
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
	 * objet {@link MyGame} pour pouvoir changer d'ecran et recup des infos
	 * generales
	 */
	private MyGame game;

	// private Sound sound;
	/** for serveur */
	private int nbjoueur;

	private AtlasRegion imgBackground;
	private SpriteBatch batch;

	/**
	 * bg private Texture bg; private static final int FRAME_COLS = 8; // #1
	 * private static final int FRAME_ROWS = 1; // #2 private Animation
	 * walkAnimation; // #3
	 * 
	 * private TextureRegion[] walkFrames; // #5 private SpriteBatch batch;
	 * 
	 * private float stateTime; // #8 private TextureRegion currentFrame; // #7
	 */

	public MenuPrincipalScreen(MyGame myGame) {
		this.game = myGame;

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				false);
		fpsLabel = new Label("fps:", skin);
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("ui/magic.pack"));
		batch = new SpriteBatch();
		imgBackground = atlas.findRegion("title");

	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		// sound.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

		// on switch le num du bouton selectionner et son affiche le screen
		// correspondant
		switch (buttonSelected) {
		case 1:
			// sound.stop();
			game.changeScreen(MyGame.CHATSCREEN);
			break;
		case 2:
			// game.setScreen(game.animationScreen);
			// sound.stop();

			break;
		case 3:
			// sound.stop();
			game.changeScreen(MyGame.NEWCHARACTERSCREEN);
			break;
		default:
			break;
		}
		batch.begin();
		batch.draw(imgBackground,
				Gdx.graphics.getWidth() / 2 - imgBackground.getRegionWidth()
						/ 2, Gdx.graphics.getHeight() / 2);
		batch.end();
		/*
		 * sound.play(0.1f); // play new sound and keep handle for further
		 * manipulation stateTime += Gdx.graphics.getDeltaTime(); // #15
		 * currentFrame = walkAnimation.getKeyFrame(stateTime, true); // #16
		 * 
		 * batch.begin(); batch.draw(currentFrame, 0,
		 * 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		 * 
		 * batch.end();
		 */
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);

		// sound =
		// Gdx.audio.newSound(Gdx.files.internal("sound/CloudTopLoops.mp3"));
		// bg = new Texture(Gdx.files.internal("background2.png"));
		//
		// TextureRegion[][] tmp = TextureRegion.split(bg, bg.getWidth() /
		// FRAME_COLS, bg.getHeight() / FRAME_ROWS); // #10
		// walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		// int index = 0;
		// for (int i = 0; i < FRAME_ROWS; i++) {
		// for (int j = 0; j < FRAME_COLS; j++) {
		// walkFrames[index++] = tmp[i][j];
		// }
		// }
		// walkAnimation = new Animation(0.5f, walkFrames); // #11
		// batch = new SpriteBatch();
		// stateTime = 0f;

		// nos 3 boutons de selection
		// TextButton tbChat = new TextButton("rejoindre un chat", skin);
		TextButton tbHost = new TextButton("heberger un chat", skin);
		TextButton tbJoin = new TextButton("creer un perso", skin);

		// game.androidUI.showAlertBox("Server", "hello", "Button text",stage);

		// nos listeners sur les boutons
		// tbChat.addListener(new ChangeListener() {
		//
		// @Override
		// public void changed(ChangeEvent event, Actor actor) {
		// // TODO Auto-generated method stub
		// buttonSelected = 1;
		// }
		// });

		tbHost.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				buttonSelected = 2;
				// TODO:demander nb joueur
				nbjoueur = 2;
				if (nbjoueur != 0) {

					final Window creerServer = new Window("Host a game", skin);
					final TextField nomServ = new TextField("",skin);
					final TextButton creer = new TextButton("Host", skin);
					creerServer.add(nomServ);
					creerServer.row();
					creerServer.add(creer);
					creerServer.row();
					creerServer.pack();
					creerServer.setPosition(
							((float) (Gdx.graphics.getHeight() * 0.5)),
							((float) (Gdx.graphics.getWidth() * 0.5)));
					stage.addActor(creerServer);

					creer.addListener(new ChangeListener() {
						public void changed(ChangeEvent arg0, Actor arg1) {
							
							try {
								if (nomServ.getText().length()>0) {
									game.chatServer = new ChatServer(nbjoueur,
											nomServ.getMessageText());
									game.androidUI.showAlertBox("Server",
											"Serveur created", "Button text",
											stage);
									creerServer.remove();
								}else{
									game.androidUI.showAlertBox("Server",
											"Error : server's name invalid", "ok",
											stage);
								}
							} catch (IOException e) {
								e.printStackTrace();
								game.androidUI.showAlertBox("Server",
										"Serveur already created",
										"Button text", stage);
							}
						}

					});
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

		// window.debug();
		Window window = new Window("Selection Perso", skin);
		window.getButtonTable().add(new TextButton("X", skin))
				.height(window.getPadTop());
		window.setPosition(Gdx.graphics.getWidth() / 3,
				Gdx.graphics.getHeight() / 4);
		window.defaults().pad(20, 20, 20, 20);
		window.row().fill().expandX();
		window.row();
		// window.add(tbChat);
		// window.row();
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
