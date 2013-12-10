package m4ges.views;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Aquamancien;
import m4ges.models.Necromancien;
import m4ges.models.Pyromancien;
import m4ges.models.Shaman;
import m4ges.models.Skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
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
public class CreateCharacterScreen extends AbstractScreen {
	/**
	 * {@link Stage}
	 */
	private Stage stage;

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
	private boolean soundIsPlaying;
	private boolean renderASkill;

	Group fg = new Group();
	Group bg = new Group();

	public CreateCharacterScreen(MyGame myGame) {
		super(myGame);
		this.spriteBatch = new SpriteBatch();
		this.fg = new Group();

		this.stage = new Stage(Gdx.graphics.getWidth(),	Gdx.graphics.getHeight(), false);
		this.stage.addActor(fg);
		this.stage.addActor(bg);


	}

	private Window createClassDescWindows() {
		Label ldesc = new Label(super.game.player.getDesc(), skin);
		ldesc.setWrap(true);
		ScrollPane scrollPaneDesc = new ScrollPane(ldesc, skin);

		Window classWindow = new Window("Description Perso", skin);
		classWindow.setPosition(100, 200);
		classWindow.add(scrollPaneDesc).minWidth(200).minHeight(200).colspan(4);
		classWindow.row();
		for (final Skill it : super.game.player.getListSkills()) {
			TextButton skillButton = new TextButton(it.getSkillName()
					+ " cost:" + it.getSpCost(), skin);
			skillButton.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					skillToRender=it;
					it.resetAnimation();
					it.setSize(it.getCurrentFrame().getRegionWidth(), it.getCurrentFrame().getRegionHeight());
					fg.addActor(it);
				}
			});
			classWindow.add(skillButton).fillX();
			classWindow.row();
		}
		classWindow.pack();

		return classWindow;
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		spriteBatch.dispose();

	}

	@Override
	public void render(float delta) {
		super.render(delta);
		// Animation perso skill
		if (super.game.player != null) {
			super.game.player.setOrigin(100, 100);
			super.game.player.setVisible(true);
		}
		if(skillToRender!=null && skillToRender.isAnimationFinished()){
			fg.clear();
		}

		stage.act();
		stage.draw();
		//		Table.drawDebug(stage);
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
		String[] tabPersonnage = { "Chamane", "Necromancien", "Pyromancien",	"Aquamancien" };
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
		window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
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
				stage.getRoot().removeActor(game.player);
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
					game.player = new Pyromancien();
					break;
				case 3:
					// initialisation du player
					game.player = new Aquamancien();
					break;
				default:
					System.err.println("switch personnage error");
				}
				game.player.setName(tfPseudo.getText());
				bg.clear();
				classDescWindow = createClassDescWindows();
				bg.addActor(classDescWindow);
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
							"Veuillez cr�er un personnage avant", "ok", stage);
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
