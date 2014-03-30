package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;

import java.io.IOException;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Bar;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.Vague;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Premier ecran en d�but de partie
 * 
 * @author Florian
 * 
 */
public class BattleScreen extends AbstractScreen {

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
	public Vague currentVague;
	/**
	 * label pour afficher un message
	 */
	private Label lb_info;
	private Label lb_vague;
	/**
	 * personnage selectionner
	 */
	private Personnage selected;
	private Window selectWindow;
	private Window skillWindow;

	/**
	 * 
	 * @param myGame
	 */
	public BattleScreen(MyGame myGame) {
		super(myGame);
		this.stage = new Stage(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT, true);
		this.stage.setCamera(cameraGUI);

	}

	@Override
	public void resize(int width, int height) {
		Vector2 size = Scaling.fit.apply(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT, width, height);
		int viewportX = (int) (width - size.x) / 2;
		int viewportY = (int) (height - size.y) / 2;
		int viewportWidth = (int) size.x;
		int viewportHeight = (int) size.y;
		Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
		stage.setViewport(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT, true, viewportX, viewportY,
				viewportWidth, viewportHeight);
		// stage.setViewport(width, height, true);
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

		// // Timer pour jouer
		// long time = TimeUtils.millis() - game.getMC().heureToken;
		// System.out.println(time);
		//
		// if(game.player.isToken() && time > 15000){
		// try {
		// game.getMC().passerToken();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }

		batch.begin();
		batch.draw(battle_bg, 0, 0, Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		if (selected != null) {
			batch.draw(battle_arrow,
					(float) (selected.getX() + selected.getWidth() / 2),
					selected.getY() + selected.getHeight());
		}
		batch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage
		Gdx.input.setInputProcessor(this.stage);
		// initilisation
		this.selected = null;
		// on retrouve les images pour construit l'UI
		TextureAtlas atlas = MyGame.manager.get("ui/battleui.pack",
				TextureAtlas.class);

		battle_info = new TextureRegion(atlas.findRegion("battle_ui"));
		battle_info2 = new TextureRegion(atlas.findRegion("battle_ui2"));
		battle_skill = new TextureRegion(atlas.findRegion("battle_ui_spell"));
		battle_arrow = new TextureRegion(atlas.findRegion("fleche"));

		// on charge la vague correspondante au niveau
		currentVague = Vague.loadVague(game.currentVagueIndex);
		// on passe au client la liste actuelle des monstres
		game.getMC().setMonstres(currentVague.getMonsters());

		// on recup la map correspondante a la vague
		TextureAtlas atlasMap = MyGame.manager.get("ui/maps.pack",
				TextureAtlas.class);
		battle_bg = new TextureRegion(atlasMap.findRegion(currentVague
				.getNameVague()));
		// on affiche
		lb_info = buildLabelMessage("Selectionner un monstre et lancer un sort");
		lb_vague = buildVagueInfo();
		// on construit les layers
		update();
		// on informe le joueur qu'il est dans la vague X
		this.stage.addActor(lb_vague);

	}

	/**
	 * methode qui maj tout les composants
	 */
	public void update() {
		// on remove les actor du stage inutiles
		for (Actor it : stage.getActors()) {
			if (it instanceof Personnage || it instanceof Window
					)
				stage.getActors().removeValue(it, true);
		}
		System.out.println("update appel�");
		this.stage.addActor(buildPersoLayer());
		this.stage.addActor(buildMonsterLayer());
		this.stage.addActor(createMySkillWindows());
		this.stage.addActor(createMyInfoWindows());
		this.stage.addActor(createSelectedWindows());
		this.stage.addActor(lb_info);
		// this.stage.addActor(lblVague);

	}

	/**
	 * affiche la liste des skill du player
	 * 
	 * @return
	 */
	private Table createMySkillWindows() {
		WindowStyle ws = new WindowStyle(skin.getFont("default-font"), Color.BLACK,
				new TextureRegionDrawable(battle_skill));
		skillWindow = new Window("", ws);
		int i = 0;
		for (final Skill it : super.game.player.getListSkills()) {

			TextButton skillButton = new TextButton(it.getSkillName() + " ("
					+ it.getSpCost() + ")", skin);
			if (!super.game.player.isToken()) {
				skillButton.setDisabled(true);
			}
			skillButton.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					it.resetAnimation();
					for (Actor it : stage.getActors()) {
						if (it instanceof Skill)
							stage.getActors().removeValue(it, true);
					}
					if (selected != null) {
//						it.setPosition(selected.getX() + selected.getWidth() / 2 - it.getWidth() /2,
//								selected.getY() - it.getHeight() / 2);
						// stage.addActor(it);
						try {
							game.mc.lancerSort(selected, it);
							stage.getActors().removeValue(selectWindow, true);
							stage.addActor(createSelectedWindows());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						lb_info.setText("Selectionner un monstre et lancer un sort");
					}
				}
			});
			skillWindow.add(skillButton);
			if (i % 2 == 1)
				skillWindow.row();
			i++;
		}
		skillWindow.setBounds((float) (battle_info.getRegionWidth()*0.95), 0,
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
		Bar bar = new Bar(game.player);
		Window infoWindow = new Window("", ws);
		infoWindow.add(new Label("hp:" + game.player.getHp(), skin));
		infoWindow.row();
		infoWindow.add(new Image(bar.getHpBar()));
		infoWindow.row();
		infoWindow.add(new Label("sp:" + game.player.getMana(), skin));
		infoWindow.row();
		infoWindow.add(new Image(bar.getSpBar()));
		infoWindow.pack();
		infoWindow.setBounds(0, 0, battle_info.getRegionWidth(),
				battle_info.getRegionHeight());
		return infoWindow;
	}

	/**
	 * affiche les infos du mob selectionn�
	 * 
	 * @return
	 */
	private Table createSelectedWindows() {
		WindowStyle ws = new WindowStyle(skin.getFont("default-font"),
				Color.BLACK, new TextureRegionDrawable(battle_info2));
		selectWindow = new Window("", ws);
		selectWindow.setBounds((float) ((skillWindow.getX()+skillWindow.getWidth())), 0,
				battle_info2.getRegionWidth(), battle_info2.getRegionHeight());
		selectWindow.pack();
		if (selected == null)
			return selectWindow;
		selectWindow.add(new Label("name:" + selected.getNom(), skin));
		selectWindow.row();
		selectWindow.add(new Label("hp:" + selected.getHp(), skin));
		selectWindow.row();
		selectWindow.add(new Label("sp:" + selected.getMana(), skin));

		selectWindow.pack();
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
			it.clear();
			it.setVisible(true);
			it.setOrigin(100 + width / 2 + i, 50 + height / 4 + i);
			it.setBounds(it.getOriginX(), it.getOriginY(), it.getWidth(),
					it.getHeight());
			it.setState(Personnage.WAIT);
			it.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getNom() + "]" + "down");
					selected = it;
					stage.getActors().removeValue(selectWindow, true);
					stage.addActor(createSelectedWindows());
					return true;
				}

				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getNom() + "]" + "up");
				}
			});
			i += 50;
			layer.addActor(it);
			// creation du nom du joueur
			Label name = new Label(it.getNom(), skin);
			if (it.getState() == Personnage.MORT)
				name.setColor(Color.RED);
			else
				name.setColor(Color.GREEN);
			// placement
			name.setPosition(it.getOriginX() + 100, it.getOriginY());
			// ajout
			layer.addActor(name);
			// reset

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
			it.clear();
			it.setVisible(true);
			it.setOrigin(width / 6 + i, (float) (height / 4 + i * 1.2));
			it.setBounds(it.getOriginX(), it.getOriginY(), it.getWidth(),
					it.getHeight());
			it.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getNom() + "]" + "down");
					selected = it;
					stage.getActors().removeValue(selectWindow, true);
					stage.addActor(createSelectedWindows());
					return true;
				}

				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getNom() + "]" + "up");
				}
			});
			if ((i % 2) == 0)
				i += 100;
			layer.addActor(it);
		}
		return layer;
	}

	/**
	 * 
	 * @return
	 */
	private Label buildVagueInfo() {
		float width = Constants.VIEWPORT_GUI_WIDTH;
		float height = Constants.VIEWPORT_GUI_HEIGHT;
		TextureAtlas atlas = MyGame.manager.get("ui/battleui.pack",
				TextureAtlas.class);
		NinePatchDrawable image = new NinePatchDrawable(
				atlas.createPatch("btn_placeholder"));
		LabelStyle style = new LabelStyle();
		style.background = image;
		style.font = skin.getFont("default-font");
		lb_vague = new Label("Vague " + game.currentVagueIndex, style);
		// lblVague.setWidth((float) (lblVague.getTextBounds().width*1.07));
		lb_vague.setPosition(width / 2 - lb_vague.getWidth() / 2,
				(float) (height / 2));
		lb_vague.setOrigin(width / 2 - lb_vague.getWidth() / 2,
				(float) (height / 2));
		// lblVague.addAction(sequence(moveTo(width+lblVague.getWidth(),
		// height/2-lblVague.getHeight(),0),
		// moveTo(width/2+lblVague.getWidth(), height/2-lblVague.getHeight(),
		// 2f),
		// delay(2f),
		// moveTo(0-lblVague.getWidth(), height/2-lblVague.getHeight(), 2f)));
		lb_vague.addAction(sequence(
				moveTo(width + lb_vague.getWidth() / 2,(float) (height / 2)),
		 moveTo(width/2-lb_vague.getWidth(),(float) (height / 2),2f,Interpolation.linear),
		 delay(1f),
		 moveTo(-lb_vague.getWidth(),(float) (height / 2),2f,Interpolation.linear)
		 ));
//		lb_vague.addAction(sequence(fadeIn(0.0001f),
//				fadeOut(3f)));
		lb_vague.pack();
		return lb_vague;
	}

	/**
	 * affiche le message
	 * 
	 * @param s
	 * @return
	 */
	private Label buildLabelMessage(String s) {
		float width = Constants.VIEWPORT_GUI_WIDTH;
		float height = Constants.VIEWPORT_GUI_HEIGHT;

		TextureAtlas atlas = MyGame.manager.get("ui/battleui.pack",
				TextureAtlas.class);
		NinePatchDrawable image = new NinePatchDrawable(
				atlas.createPatch("btn_placeholder"));
		LabelStyle style = new LabelStyle();
		style.background = image;
		style.font = skin.getFont("default-font");
		lb_info = new Label(s, style);
		lb_info.setWidth((float) (lb_info.getTextBounds().width * 1.07));
		lb_info.setPosition(width / 2 - lb_info.getWidth() / 2,
				(float) (height * 0.90));
		lb_info.setOrigin(width / 2 - lb_info.getWidth() / 2,
				(float) (height * 0.90));
		// lb_info.pack();
		return lb_info;

	}

	/**
	 * affiche sur l'ecran
	 * 
	 * @param s
	 *            le skill en question
	 * @param lanceur
	 *            le personnage lanceur du skill
	 * @param cible
	 *            le personnage qui recoit le skill
	 */
	public void afficheSkill(Skill s, Personnage lanceur, Personnage cible) {
		s.resetAnimation();
		for (Actor it : stage.getActors()) {
			if (it instanceof Skill)
				stage.getActors().removeValue(it, true);
		}
		s.setPosition(cible.getX() + cible.getWidth() / 2 - s.getWidth() /2,
				cible.getY() - s.getHeight() / 3);

		lb_info.setText(lanceur.getNom() + " utilise " + s.getSkillName()
				+ " sur " + cible.getNom());
		final Label lb_dom = new Label("" + s.getDamage(), skin);
		float milieu_x = cible.getX() + cible.getWidth() / 2;
		float milieu_y = cible.getY() + cible.getHeight() / 2;
		lb_dom.setColor(Color.RED);

		Action act = new ParallelAction(sequence(moveTo(milieu_x, milieu_y),
				moveBy((int) (30), (int) (30), 1.0f, Interpolation.linear)),
				sequence(color(Color.WHITE, 1f), fadeOut(0)));
		lb_dom.addAction(act);
		stage.addActor(s);
		stage.addActor(lb_dom);

		/*
		 * Indispensable pour garder une certaine coherence dans le jeu OULALA
		 */
		try {
			while (!s.isAnimationFinished())
				Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("appel update");

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				update();
			}
		});

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	public void clearScreen(){
		stage.clear();
	}
	public void updateSkillWindow() {
		stage.getActors().removeValue(skillWindow, true);
		stage.addActor(createMySkillWindows());
	}

}
