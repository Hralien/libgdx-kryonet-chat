package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import java.io.IOException;

import reseau.GameServer;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.util.AudioManager;
import m4ges.util.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
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
	 * {@link Stage}
	 */
	private Stage stage;
	/** 
	 * nb of players for serveur
	 */
	private int nbjoueur;
	/**
	 *  {@link AtlasRegion} to get the logo
	 */
	private Image imgTitle;
	
	private Animation bgAnimation; // #3
	private TextureRegion[] bgFrames; // #5 
	private float stateTime; // #8 
	private TextureRegion currentFrame; // #7

	//menu
	private Button btnMenuHost;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	// options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private CheckBox chkShowFpsCounter;
	private CheckBox chkUseMonochromeShader;
	//server setup
	private Window winServer;
	private TextField tfServerName;
	

	/**
	 * 
	 * @param myGame
	 */
	public MenuPrincipalScreen(MyGame myGame) {
		super(myGame);

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),false);
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
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

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stateTime += Gdx.graphics.getDeltaTime(); // #15
		currentFrame = bgAnimation.getKeyFrame(stateTime, true); // #16
		
		batch.begin();
		batch.draw(currentFrame, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
//		imgTitle.draw(batch, delta);
		batch.end();

		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);

	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		TextureAtlas atlas = MyGame.manager.get("ui/loading.pack", TextureAtlas.class);
		
		//Background initialisation
		buildBackgroundLayer();
		
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);

		stack.setSize(w,h);
		stack.add(buildTitleLayer(atlas));
		stack.add(buildControlLayer(atlas));
		stage.addActor(buildServerSetup(atlas));
		stage.addActor(buildOptionsWindowLayer());
		AudioManager.instance.play(Gdx.audio.newMusic(Gdx.files.internal("sound/CloudTopLoops.mp3")));


	}
	/**
	 * 
	 * @param atlas
	 * @return
	 */
	private Table buildServerSetup(TextureAtlas atlas) {
		winServer = new Window("Host a game", skin);
		Label lblServerName= new Label("Serveur name", skin); 
		tfServerName = new TextField("",skin);
		final TextButton valider = new TextButton("Valider", skin);
		winServer.add(lblServerName);
		winServer.add(tfServerName);
		winServer.row();
		winServer.add(valider);
		winServer.row();
		winServer.pack();
		winServer.setPosition((float) (Gdx.graphics.getWidth()/2 - winServer.getWidth()), Gdx.graphics.getHeight()/2);

		valider.addListener(new ChangeListener() {
			public void changed(ChangeEvent arg0, Actor arg1) {
				try {
					if (tfServerName.getText().length()>0) {
						game.chatServer = new GameServer(nbjoueur,
								tfServerName.getMessageText());
						game.androidUI.showAlertBox("Server",
								"Serveur created", "Ok",
								stage);
						winServer.remove();
						showMenuButtons(true);
					}else{ 
						game.androidUI.showAlertBox("Server",
								"Error : server's name invalid", "Ok",
								stage);
					}
				} catch (IOException e) {
					e.printStackTrace();
					game.androidUI.showAlertBox("Server",
							"Serveur already created",
							"Button text", stage);
					winServer.remove();
					showMenuButtons(true);
				}
			}
		});
		showServerWindow(false, false);
		return winServer;
	}
	/**
	 * 
	 * @param atlas
	 * @return
	 */
	private Table buildTitleLayer(TextureAtlas atlas){
		Table layer = new Table();
		imgTitle = new Image(atlas.findRegion("TitleM4ges"));
		imgTitle.setSize((float)(Gdx.graphics.getWidth()*.5),(float)(Gdx.graphics.getWidth()*.25));
		layer.add(imgTitle).width((float)(Gdx.graphics.getWidth()*.5)).height((float)(Gdx.graphics.getWidth()*.25));
		layer.left();
		layer.top();
		layer.padLeft((float)(Gdx.graphics.getWidth())/2-imgTitle.getWidth()/2);
		return layer;
	}
	/**
	 * 
	 * @param atlas
	 * @return
	 */
	private Table buildControlLayer(TextureAtlas atlas){
		Table layer = new Table();

		TextureRegion image = new TextureRegion(atlas.findRegion("magic_button2"));
		TextButtonStyle style = new TextButtonStyle();
		style.up = new TextureRegionDrawable(image);
		style.font = new BitmapFont();

		//buttons with style
		btnMenuHost =  buildBtnMenuHost(style);
		btnMenuPlay =  buildBtnMenuPlay(style);
		btnMenuOptions = buildBtnMenuOption(style);
		
	
		layer.add(btnMenuHost);
		layer.row();
		layer.add(btnMenuPlay);
		layer.row();
		layer.add(btnMenuOptions);
		layer.left();
		layer.padLeft((float)(Gdx.graphics.getWidth())/2-image.getRegionWidth()/2);
		layer.padTop((float)(Gdx.graphics.getHeight())/2-layer.getHeight());

		return layer;
	}
	/**
	 * 
	 * @param style
	 * @return
	 */
	private TextButton buildBtnMenuPlay(TextButtonStyle style) {
		TextButton tbJoin = new TextButton("Cr�er un perso", style);
		tbJoin.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				goToNewCharacter();
			}
		});
		tbJoin.setPosition((float) (Gdx.graphics.getWidth() / 2.5),	Gdx.graphics.getHeight() / 6);

		return tbJoin;
	}
	/**
	 * 
	 * @param style
	 * @return
	 */
	private TextButton buildBtnMenuOption(TextButtonStyle style) {
		TextButton tbOption = new TextButton("Options", style);
		tbOption.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				loadSettings();
				showMenuButtons(false);
				showOptionsWindow(true, true);
			}
		});
		return tbOption;
	}
	/**
	 * 
	 * @param style
	 * @return
	 */
	private TextButton buildBtnMenuHost(TextButtonStyle style) {
		TextButton tbHost = new TextButton("H�berger une partie", style);
		tbHost.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO:demander nb joueur
				nbjoueur = 2;
				if (nbjoueur != 0){
					showMenuButtons(false);
					showServerWindow(true, true);
				}
			}
		});
		tbHost.setPosition((float) (Gdx.graphics.getWidth() / 2.5),	Gdx.graphics.getHeight() / 4);
		return tbHost;
	}
	/**
	 * 
	 */
	private void buildBackgroundLayer() {
	 	Texture bg = new Texture(Gdx.files.internal("ui/bg.png"));
		int FRAME_COLS = 1; // #1
		int FRAME_ROWS = 90; // #2 
		TextureRegion[][] tmp = TextureRegion.split(bg, bg.getWidth() /	FRAME_COLS, bg.getHeight() / FRAME_ROWS); // #10
		bgFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0; 
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				bgFrames[index++] = tmp[i][j];
			}
		}
		bgAnimation = new Animation(0.1f, bgFrames); // #11
		bgAnimation.setPlayMode(Animation.LOOP_PINGPONG);
		stateTime = 0f;
	}
	/**
	 * 
	 * @return
	 */
	private Table buildOptWinAudioSettings () {
		Table tbl = new Table();
		// + Title: "Audio"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Audio", skin, "default-font", Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skin);
		tbl.add(chkSound);
		tbl.add(new Label("Sound", skin));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skin);
		tbl.add(sldSound);
		tbl.row();
		// + Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skin);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", skin));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skin);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}
	/**
	 * 
	 * @return
	 */
	private Table buildOptWinDebug () {
		Table tbl = new Table();
		// + Title: "Debug"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Debug", skin, "default-font", Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", skin);
		tbl.add(new Label("Show FPS Counter", skin));
		tbl.add(chkShowFpsCounter);
		tbl.row();
		// + Checkbox, "Use Monochrome Shader" label
		chkUseMonochromeShader = new CheckBox("", skin);
		tbl.add(new Label("Use Monochrome Shader", skin));
		tbl.add(chkUseMonochromeShader);
		tbl.row();
		return tbl;
	}
	/**
	 * 
	 * @return
	 */
	private Table buildOptWinButtons () {
		Table tbl = new Table();
		// + Separator
		Label lbl = null;
		lbl = new Label("", skin);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", skin);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skin.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		// + Save Button with event handler
		btnWinOptSave = new TextButton("Save", skin);
		tbl.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});
		// + Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancel", skin);
		tbl.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				onCancelClicked();
			}
		});
		return tbl;
	}
	/**
	 * 
	 * @return
	 */
	private Table buildOptionsWindowLayer() {
		winOptions = new Window("Options", skin);
		// + Audio Settings: Sound/Music CheckBox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row();
		// + Debug: Show FPS Counter
		winOptions.add(buildOptWinDebug()).row();
		// + Separator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);
		// Hide options window by default
		showOptionsWindow(false, false);
		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack();
		// Move options window to bottom right corner
		winOptions.setPosition(Gdx.graphics.getWidth()/2 - winOptions.getWidth() - 50, 50);
		return winOptions;
	}
	/**
	 * 
	 */
	private void onSaveClicked() {
		saveSettings();
		onCancelClicked();
		AudioManager.instance.onSettingsUpdated();
	}
	/**
	 * 
	 */
	private void onCancelClicked() {
		showMenuButtons(true);
		showOptionsWindow(false, true);
		AudioManager.instance.onSettingsUpdated();
	}
	/**
	 * 
	 */
	private void loadSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
		chkUseMonochromeShader.setChecked(prefs.useMonochromeShader);
	}
	/**
	 * 
	 */
	private void saveSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
		prefs.useMonochromeShader = chkUseMonochromeShader.isChecked();
		prefs.save();
	}
	/**
	 * 
	 * @param visible
	 */
	private void showMenuButtons (boolean visible) {
		float moveDuration = 1.0f;
		Interpolation moveEasing = Interpolation.swing;
		float delayOptionsButton = 0.25f;

		float moveX = 500 * (visible ? -1 : 1);
		float moveY = 0 * (visible ? -1 : 1);
		final Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		btnMenuHost.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
		btnMenuPlay.addAction(sequence(delay(delayOptionsButton), moveBy(moveX, moveY, moveDuration, moveEasing)));
		btnMenuOptions.addAction(sequence(delay(delayOptionsButton*2), moveBy(moveX, moveY, moveDuration, moveEasing)));

		SequenceAction seq = sequence();
		if (visible) seq.addAction(delay(delayOptionsButton + moveDuration));
		seq.addAction(run(new Runnable() {
			public void run () {
				btnMenuHost.setTouchable(touchEnabled);
				btnMenuPlay.setTouchable(touchEnabled);
				btnMenuOptions.setTouchable(touchEnabled);
			}
		}));
		stage.addAction(seq);
	}
	/**
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showOptionsWindow (boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		winOptions.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
	}
	/**
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showServerWindow (boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		winServer.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
	}	
	/**
	 * 
	 */
	private void goToNewCharacter(){
		AudioManager.instance.stopMusic();
		super.game.changeScreen(MyGame.NEWCHARACTERSCREEN);
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
