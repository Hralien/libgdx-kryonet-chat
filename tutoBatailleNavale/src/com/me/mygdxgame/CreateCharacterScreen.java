package com.me.mygdxgame;

import gameMechanic.MageChaud;
import gameMechanic.MageFroid;
import gameMechanic.Necromancien;
import gameMechanic.Shaman;
import gameMechanic.Skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

/**
 * Menu principal selection de ce qu'on veut faire
 * 
 * @author Florian
 * 
 */
public class CreateCharacterScreen implements Screen {
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
	 * objet {@link MyGame} pour pouvoir changer d'ecran et recup des infos
	 * generales
	 */
	private MyGame game;
	/**
	 * objet requis pour dessiner {@link SpriteBatch}
	 */
	private SpriteBatch spriteBatch; // #6
	/**
	 * fenetre listant les skill
	 */
	private static Window classDescWindow;
	/**
	 * skill a afficher
	 */
	private Skill skillToRender;
	private int spriteFrameToRender;
	private boolean soundIsPlaying;

	public CreateCharacterScreen(MyGame myGame) {

		this.game = myGame;
		this.spriteBatch = new SpriteBatch();
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		this.stage = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), false);
		this.fpsLabel = new Label("fps:", skin);

	}

	private Window createClassDescWindows() {
		Label ldesc = new Label(game.player.getDesc(), skin);
		ldesc.setWrap(true);
		ScrollPane scrollPaneDesc = new ScrollPane(ldesc, skin);

		Window window = new Window("Description Perso", skin);
		window.setPosition(100, 200);
		window.add(scrollPaneDesc).minWidth(200).minHeight(200).colspan(4);
		window.row();
		for (final Skill it : game.player.getListSkills()) {
			TextButton skillButton = new TextButton(it.getSkillName()
					+ " cost:" + it.getSpCost(), skin);
			skillButton.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					try {
						skillToRender = (Skill) it.clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			window.add(skillButton).fillX();
			window.row();
		}
		window.pack();

		return window;
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

		// dit a l'objet SpriteBatch de se preparer a dessiner
		spriteBatch.begin();

		// Animation perso skill
		if (game.player != null) {
			game.player.setOrigin(100, 100);
			game.player.setVisible(true);
		}
		// si on a un skill a afficher
		if (skillToRender != null && !skillToRender.isSkillEffectEnded()) {
			// skillToRender.getEffect().draw(spriteBatch, delta);
			spriteBatch.draw(skillToRender.afficheSkill(), 50, 50); // #17

			if (!soundIsPlaying) {
				skillToRender.getSound().play();
				soundIsPlaying = true;
			}
			// si l'animation est finie on remets à null
			if (skillToRender.isSkillEffectEnded()) {
				soundIsPlaying = false;
				skillToRender = null;
			}
		}
		// dit à l'objet SpriteBatch qu'on a finit de dessiner
		spriteBatch.end();

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);
		TextButton tbvalidation = new TextButton("creer un perso", skin);
		TextButton tbConnecter = new TextButton("se connecter", skin);

		final TextField tfPseudo = new TextField("", skin);
		tfPseudo.setMessageText("Saisir un pseudo!");

		// creation d'un tableau pour stocker les classes
		String[] tabPersonnage = { "Shaman", "Necromencian", "Mage Chaud",
		"Mage Froid" };
		// creation d'une select box (appele List ici) avec le tableau ci dessus
		final List listClasses = new List(tabPersonnage, skin);
		// ajout de la List dans un scrollPane, pour pouvoir derouler,
		// descendre, monter
		ScrollPane scrollPaneClass = new ScrollPane(listClasses, skin);

		final TextButton fleche = new TextButton(">", skin);
		fleche.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if (game.player != null) {
					if(game.player.dessine()!=null){
						if (game.player.getFrameToDraw() + 1 == game.player.dessine().length)
							game.player.setFrameToDraw(0);
						else {
							game.player.setFrameToDraw(game.player.getFrameToDraw()+1);
						}
					}
				}
			}
		});

		// window.debug();
		Window window = new Window("Selection Perso", skin);
		window.getButtonTable().add(new TextButton("X", skin))
		.height(window.getPadTop());
		window.setPosition(650, 200);
		window.defaults().pad(20, 20, 20, 20);
		window.row().fill().expandX();
		window.add(tfPseudo).minWidth(100).expandX().fillX().colspan(4);
		window.row();
		window.add(scrollPaneClass).minWidth(100).expandX().fillX().colspan(4);
		window.row();
		window.add(tbvalidation);
		window.add(tbConnecter);
		window.row();
		window.add(fleche);
		window.row();
		window.add(fpsLabel).colspan(4);
		window.pack();

		// stage.addActor(new Button("Behind Window", skin));
		stage.addActor(window);

		tfPseudo.setTextFieldListener(new TextFieldListener() {
			public void keyTyped(TextField textField, char key) {
				if (key == '\n')
					textField.getOnscreenKeyboard().show(false);
			}
		});

		tbvalidation.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				switch (listClasses.getSelectedIndex()) {
				case 0:
					// initialisation du player
					game.player = new Shaman();
					break;
				case 1:
					// initialisation du player
					game.player = new Necromancien();
					break;
				case 2:
					// initialisation du player
					game.player = new MageChaud();
					break;
				case 3:
					// initialisation du player
					game.player = new MageFroid();
					break;
				default:
					System.err.println("switch personnage error");
				}
				game.player.setName(tfPseudo.getText());
				classDescWindow = createClassDescWindows();
				stage.addActor(classDescWindow);
				stage.addActor(game.player);
			}
		});
		tbConnecter.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if (game.player != null) {
					game.changeScreen(MyGame.CHATSCREEN);	
				} else
					game.androidUI.showAlertBox("Erreur",
							"Veuillez créer un personnage avant", "ok", stage);
			}
		});
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
