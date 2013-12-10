package m4ges.views;

import java.io.IOException;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import chat.ChatServer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Menu principal selection de ce qu'on veut faire
 * 
 * @author Florian
 * 
 */
public class MenuPrincipalScreen extends AbstractScreen {
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
	 * nb of players for serveur
	 */
	private int nbjoueur;
	/**
	 *  {@link AtlasRegion} to get the logo
	 */
	private Sprite imgTitle;
	/** 
	 * {@link SpriteBatch} to draw
	 */
	private SpriteBatch batch;
	/** 
	 * to get the shake effect => current rotation value
	 */
	private float rot;
	/**
	 * camera
	 */
	private OrthographicCamera camera;

	private Texture bg;
	private static final int FRAME_COLS = 1; // #1
	private static final int FRAME_ROWS = 90; // #2 
	private Animation walkAnimation; // #3

	private TextureRegion[] walkFrames; // #5 

	private float stateTime; // #8 
	private TextureRegion currentFrame; // #7

	private Music music;


	public MenuPrincipalScreen(MyGame myGame) {
		super(myGame);

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),false);
		fpsLabel = new Label("fps:", skin);
		batch = new SpriteBatch();

	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();
		music.dispose();
	}

	@Override
	public void render(float delta) {
        super.render( delta );

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

		// on switch le num du bouton selectionner et son affiche le screen
		// correspondant
		switch (buttonSelected) {
		case 1:
			music.stop();
			super.game.changeScreen(MyGame.CHATSCREEN);
			break;
		case 2:
			// game.setScreen(game.animationScreen);
			// sound.stop();

			break;
		case 3:
			music.stop();
			super.game.changeScreen(MyGame.NEWCHARACTERSCREEN);
			break;
		default:
			break;
		}

		batch.begin();

		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = walkAnimation.getKeyFrame(stateTime, true); // #16

		batch.draw(currentFrame, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		imgTitle.draw(batch);

		batch.end();

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);

		bg = new Texture(Gdx.files.internal("ui/bg.png"));
		TextureRegion[][] tmp = TextureRegion.split(bg, bg.getWidth() /
				FRAME_COLS, bg.getHeight() / FRAME_ROWS); // #10
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(0.1f, walkFrames); // #11
		walkAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		batch = new SpriteBatch();
		stateTime = 0f;

		// nos 3 boutons de selection
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(1, h / w);

		TextureAtlas atlas = MyGame.manager.get("ui/loading.pack", TextureAtlas.class);
		TextureRegion image = new TextureRegion(atlas.findRegion("magic_button2"));


		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(image);
		style.font = new BitmapFont();


		TextButton tbHost = new TextButton("heberger un chat", style);
		TextButton tbJoin = new TextButton("creer un perso", style);

		imgTitle = new Sprite(atlas.findRegion("TitleM4ges"));
		imgTitle.setPosition((int)(Gdx.graphics.getWidth()*0.2), (int)(Gdx.graphics.getHeight()*0.5));

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
					creerServer.setPosition(((float) (Gdx.graphics.getWidth() * 0.5)-creerServer.getWidth()),
							((float) (Gdx.graphics.getHeight() * 0.5)));
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
//		Window window = new Window("Selection Perso", skin);
//		window.getButtonTable().add(new TextButton("X", skin))
//		.height(window.getPadTop());
//		window.setPosition(Gdx.graphics.getWidth() / 3,
//				Gdx.graphics.getHeight() / 4);
//		window.defaults().pad(5, 5, 5, 5);
//		window.row().fill().expandX();
//		window.row();
//		// window.add(tbChat);
//		// window.row();
//		window.add(tbHost);
//		window.row();
//		window.add(tbJoin);
//		window.row();
//		window.add(fpsLabel).colspan(4);
//		window.pack();
//		
		tbHost.setPosition((float) (Gdx.graphics.getWidth() / 2.5),	Gdx.graphics.getHeight() / 4);
		tbJoin.setPosition((float) (Gdx.graphics.getWidth() / 2.5),	Gdx.graphics.getHeight() / 6);
		fpsLabel.setPosition(0, Gdx.graphics.getHeight()-20);
		stage.addActor(tbHost);
		stage.addActor(tbJoin);
		stage.addActor(fpsLabel);

		music = Gdx.audio.newMusic(Gdx.files.internal("sound/CloudTopLoops.mp3"));
		music.play(); // play new sound and keep handle for further
		music.setVolume(0.5f);                 // sets the volume to half the maximum volume
		music.setLooping(true);                // will repeat playback until music.stop() is called
		music.stop();                          // stops the playback
		music.pause();                         // pauses the playback
		music.play();                          // resumes the playback

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
