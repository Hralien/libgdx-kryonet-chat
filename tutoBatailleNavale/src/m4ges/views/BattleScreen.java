package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.Vague;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Premier ecran en début de partie
 * 
 * @author Florian
 * 
 */
public class BattleScreen extends AbstractScreen {

	private int numeroVague = 1;
	/**
	 * {@link Stage}
	 */
	private Stage stage;
	/**
	 * image de fond
	 */
	private TextureRegion battle_bg;
	private TextureRegion battle_info;
	private TextureRegion battle_info2;
	private TextureRegion battle_skill;
	private TextureRegion battle_arrow;
	/**
	 * liste des mobs
	 */
	private Vague currentVague;
	/**
	 * premier plan
	 */
	private Group fg;
	/**
	 * fenetre d'affichage du numero de la vague
	 */
	private Window winVagueInfo;
	/**
	 * label pour afficher un message
	 */
	private Label lb_info;
	/**
	 * personnage selectionner
	 */
	private Personnage selected;
	private Window selectWindow;

	/**
	 * 
	 * @param myGame
	 */
	public BattleScreen(MyGame myGame) {
		super(myGame);
		this.stage = new Stage(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT, true);
		this.stage.setCamera(cameraGUI);
		fg = new Group();

	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
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
		batch.setProjectionMatrix(stage.getCamera().combined);

		batch.begin();
		batch.draw(battle_bg, 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		if (selected != null) {
			batch.draw(battle_arrow, selected.getOriginX(),
					selected.getOriginY() + selected.getHeight());
		}
		batch.end();

		stage.act(delta);
		stage.draw();
		// Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);

		TextureAtlas atlas = MyGame.manager.get("ui/battleui.pack",
				TextureAtlas.class);

		battle_bg = new TextureRegion(atlas.findRegion("battle_background"));
		battle_info = new TextureRegion(atlas.findRegion("battle_ui"));
		battle_info2 = new TextureRegion(atlas.findRegion("battle_ui2"));
		battle_skill = new TextureRegion(atlas.findRegion("battle_ui_spell"));
		battle_arrow = new TextureRegion(atlas.findRegion("fleche"));

		currentVague = Vague.loadVague(numeroVague);
		lb_info = showMessage("Selectionner un monstre et lancer un sort");

		Stack stack = new Stack();
		stack.add(buildPersoLayer());
		stack.add(buildMonsterLayer());
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);

		this.stage.addActor(stack);
		this.stage.addActor(createMySkillWindows());
		this.stage.addActor(createMyInfoWindows());
		this.stage.addActor(fg);
		this.fg.addActor(lb_info);
		this.fg.addActor(buildVagueInfo());
		showVagueWindow();

	}

	/**
	 * affiche la liste des skill du player
	 * 
	 * @return
	 */
	private Table createMySkillWindows() {
		WindowStyle ws = new WindowStyle(new BitmapFont(), Color.BLACK,
				new TextureRegionDrawable(battle_skill));
		Window skillWindow = new Window("", ws);
		int i = 0;
		for (final Skill it : super.game.player.getListSkills()) {
			TextButton skillButton = new TextButton(it.getSkillName()
					+ " cost:" + it.getSpCost(), skin);
			skillButton.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					fg.clear();
					it.resetAnimation();
					it.setSize(it.getCurrentFrame().getRegionWidth(), it
							.getCurrentFrame().getRegionHeight());
					fg.addActor(it);
				}
			});
			skillWindow.add(skillButton);
			if (i % 2 == 1)
				skillWindow.row();
			i++;
		}
		skillWindow.setBounds(battle_info.getRegionWidth() - 30, 0,
				battle_skill.getRegionWidth(), battle_skill.getRegionHeight());

		return skillWindow;
	}

	/**
	 * affiche les infos du player
	 * 
	 * @return
	 */
	private Window createMyInfoWindows() {
		WindowStyle ws = new WindowStyle(new BitmapFont(), Color.BLACK,
				new TextureRegionDrawable(battle_info));
		Window infoWindow = new Window("", ws);
		infoWindow.row();
		infoWindow.add(new Label("hp:" + game.player.getHp(), skin));
		infoWindow.row();
		infoWindow.add(new Label("sp:" + game.player.getMana(), skin));
		infoWindow.pack();
		infoWindow.setBounds(0, 0, battle_info.getRegionWidth(),
				battle_info.getRegionHeight());
		return infoWindow;
	}

	/**
	 * affiche les infos du mob selectionné
	 * 
	 * @return
	 */
	private Window createSelectedWindows() {
		WindowStyle ws = new WindowStyle(new BitmapFont(), Color.BLACK,
				new TextureRegionDrawable(battle_info2));
		selectWindow = new Window("", ws);
		if (selected == null)
			return selectWindow;
		selectWindow.add(new Label("name:" + selected.getName(), skin));
		selectWindow.row();
		selectWindow.add(new Label("hp:" + selected.getHp(), skin));
		selectWindow.row();
		selectWindow.add(new Label("sp:" + selected.getMana(), skin));
		selectWindow.pack();
		selectWindow.setBounds(Constants.VIEWPORT_GUI_WIDTH, 0,
				battle_skill.getRegionWidth(), battle_skill.getRegionHeight());
		return selectWindow;
	}

	/**
	 * affiche tout les perso
	 * 
	 * @return
	 */
	private Table buildPersoLayer() {
		float width = Constants.VIEWPORT_GUI_WIDTH;
		float height = Constants.VIEWPORT_GUI_HEIGHT;

		Table layer = new Table();
		int i = 0;
		for (final Personnage it : super.game.playersConnected) {
			it.setVisible(true);
			it.setOrigin(100 + width / 3 + i, 50 + height / 4 + i);
			it.setBounds(it.getOriginX(), it.getOriginY(), it.getWidth(),
					it.getHeight());
			it.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getName() + "]" + "down");
					selected = it;
					stage.getActors().removeValue(selectWindow, true);
					stage.addActor(createSelectedWindows());
					return true;
				}

				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getName() + "]" + "up");
				}
			});
			i += 50;
			layer.addActor(it);
		}
		return layer;
	}

	/**
	 * 
	 * @return
	 */
	private Table buildMonsterLayer() {
		float width = Constants.VIEWPORT_GUI_WIDTH;
		float height = Constants.VIEWPORT_GUI_HEIGHT;

		Table layer = new Table();
		int i = 0;
		for (final Personnage it : currentVague.getMonsters()) {
			it.setVisible(true);
			it.setOrigin(width / 3 + i, height / 4 + i);
			it.setBounds(it.getOriginX(), it.getOriginY(), it.getWidth(),
					it.getHeight());
			it.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getName() + "]" + "down");
					selected = it;
					stage.getActors().removeValue(selectWindow, true);
					stage.addActor(createSelectedWindows());
					return true;
				}

				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getName() + "]" + "up");
				}
			});
			i += 50;
			layer.addActor(it);
		}
		return layer;
	}

	/**
	 * 
	 * @return
	 */
	private Window buildVagueInfo() {
		winVagueInfo = new Window("Info", skin);
		Label lblVague = new Label("Vague " + numeroVague, skin);
		winVagueInfo.add(lblVague);
		winVagueInfo.setSize(300, 200);
		winVagueInfo.setPosition(Constants.VIEWPORT_GUI_WIDTH / 2
				- winVagueInfo.getWidth(),
				(float) (Constants.VIEWPORT_GUI_HEIGHT * .35));
		return winVagueInfo;
	}

	/**
	 * affiche la fenetre d'info de la vague
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showVagueWindow() {
		winVagueInfo.addAction(sequence(touchable(Touchable.disabled),
				alpha(0.8f, 2.5f)));
		winVagueInfo.addAction(sequence(touchable(Touchable.disabled),
				alpha(0.0f, 2.5f)));
	}

	/**
	 * affiche le message
	 * @param s
	 * @return
	 */
	private Label showMessage(String s) {
		lb_info = new Label(s, skin);
		lb_info.setBounds(
				Constants.VIEWPORT_GUI_WIDTH / 2 - lb_info.getWidth(),
				(float) (Constants.VIEWPORT_GUI_HEIGHT * .7), 60, 20);
		return lb_info;

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

	public int getNumeroVague() {
		return numeroVague;
	}

	public void setNumeroVague(int numeroVague) {
		this.numeroVague = numeroVague;
	}

}
