package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.color;
import java.io.IOException;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Bar;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.Vague;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
	 * label pour afficher un message
	 */
	private Label lb_info;
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
		this.stage = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);
		this.stage.setCamera(cameraGUI);

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
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage
		Gdx.input.setInputProcessor(this.stage);
		//initilisation
		this.selected = null;
		//on retrouve les images pour construit l'UI
		TextureAtlas atlas = MyGame.manager.get("ui/battleui.pack",
				TextureAtlas.class);

		battle_info = new TextureRegion(atlas.findRegion("battle_ui"));
		battle_info2 = new TextureRegion(atlas.findRegion("battle_ui2"));
		battle_skill = new TextureRegion(atlas.findRegion("battle_ui_spell"));
		battle_arrow = new TextureRegion(atlas.findRegion("fleche"));
		
		//on charge la vague correspondante au niveau
		currentVague = Vague.loadVague(game.currentVague);
		//on passe au client la liste actuelle des monstres
		game.getMC().setMonstres(currentVague.getMonsters());
		
		//on recup la map correspondante a la vague
		TextureAtlas atlasMap = MyGame.manager.get("ui/maps.pack",
				TextureAtlas.class);
		battle_bg = new TextureRegion(atlasMap.findRegion(currentVague
				.getNameVague()));
		//on affiche
		lb_info = buildLabelMessage("Selectionner un monstre et lancer un sort");
		//on construit les layers
		update();
		//on informe le joueur qu'il est dans la vague X
		this.stage.addActor(buildVagueInfo());

	}
	/**
	 * methode qui maj tout les composants
	 */
	public void update() {
		//on remove les actor du stage inutiles
		for (Actor it : stage.getActors()) {
			if (it instanceof Personnage || it instanceof Table)
				stage.getActors().removeValue(it, true);
		}		
		this.stage.addActor(buildPersoLayer());
		this.stage.addActor(buildMonsterLayer());
		this.stage.addActor(createMySkillWindows());
		this.stage.addActor(createMyInfoWindows());
		this.stage.addActor(createSelectedWindows());
		this.stage.addActor(lb_info);

	}




	/**
	 * affiche la liste des skill du player
	 * 
	 * @return
	 */
	private Table createMySkillWindows() {
		WindowStyle ws = new WindowStyle(new BitmapFont(), Color.BLACK,
				new TextureRegionDrawable(battle_skill));
		skillWindow = new Window("", ws);
		int i = 0;
		for (final Skill it : super.game.player.getListSkills()) {

			TextButton skillButton = new TextButton(it.getSkillName()
					+ " cost:" + it.getSpCost(), skin);
			if (!super.game.player.isToken()) {
				System.err.println("APPPELELELEE");
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
						it.setPosition(selected.getX()-it.getWidth()/2, selected.getY()-it.getHeight()/2);
						//						stage.addActor(it);
						try {
							game.mc.lancerSort(selected, it);
							stage.getActors().removeValue(selectWindow, true);
							stage.addActor(createSelectedWindows());
						} catch (IOException e) {
							e.printStackTrace();
						}
						update();

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
		skillWindow.setBounds(battle_info.getRegionWidth(), 0,
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
		infoWindow.row();
		infoWindow.add(new Image(bar.getHpBar()));
		infoWindow.add(new Label("hp:" + game.player.getHp(), skin));
		infoWindow.row();
		infoWindow.add(new Image(bar.getSpBar()));
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
		selectWindow.setBounds(Gdx.graphics.getWidth(), 0,
				battle_skill.getRegionWidth(), battle_skill.getRegionHeight());
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

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

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
					System.err.println("selected:" + selected);
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
			//creation du nom du joueur
			Label name = new Label(it.getNom(), skin);
			if (it.getState()==Personnage.MORT)
				name.setColor(Color.RED);
			else
				name.setColor(Color.GREEN);
			//placement
			name.setPosition( it.getOriginX()+100, it.getOriginY());
			//ajout
			layer.addActor(name);
			//reset

		}
		return layer;
	}

	/**
	 * 
	 * @return
	 */
	private Table buildMonsterLayer() {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		Table layer = new Table();
		int i = 0;
		for (final Personnage it : currentVague.getMonsters()) {
			it.clear();
			it.setVisible(true);
			it.setState(Personnage.WAIT);
			it.setOrigin(width / 4 + i, (float) (height / 4 + i*1.5));
			it.setBounds(it.getOriginX(), it.getOriginY(), it.getWidth(),
					it.getHeight());
			it.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getNom() + "]" + "down");
					selected = it;
					System.err.println("selected:" + selected);
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
		}
		return layer;
	}

	/**
	 * 
	 * @return
	 */
	private Actor buildVagueInfo() {
		Label lblVague = new Label("Vague " + game.currentVague, skin);
		lblVague.setPosition(Gdx.graphics.getWidth() / 2 - lblVague.getWidth()
				/ 2, (float) (Gdx.graphics.getHeight() / 2));
		lblVague.setOrigin(Gdx.graphics.getWidth() / 2 - lblVague.getWidth()
				/ 2, (float) (Gdx.graphics.getHeight() / 2));
		lblVague.pack();
		lblVague.addAction(sequence(Actions.fadeIn(0.0001f),
				Actions.fadeOut(3f)));
		return lblVague;
	}

	/**
	 * affiche le message
	 * 
	 * @param s
	 * @return
	 */
	private Label buildLabelMessage(String s) {
		TextureAtlas atlas = MyGame.manager.get("ui/loading.pack",
				TextureAtlas.class);
		TextureRegion image = new TextureRegion(
				atlas.findRegion("magic_button2"));
		LabelStyle style = new LabelStyle();
		style.background = new TextureRegionDrawable(image);
		style.font = new BitmapFont();
		lb_info = new Label(s, style);
		lb_info.setPosition(Gdx.graphics.getWidth() / 2 - lb_info.getWidth()
				/ 2, (float) (Gdx.graphics.getHeight() * 0.95));
		lb_info.setOrigin(Gdx.graphics.getWidth() / 2 - lb_info.getWidth() / 2,
				(float) (Gdx.graphics.getHeight() * 0.95));
		lb_info.pack();
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
		s.setPosition(cible.getX()-s.getWidth()/2, cible.getY()-s.getHeight()/3);

		lb_info.setText(lanceur.getNom() + " utilise " + s.getSkillName() + " sur "
				+ cible.getNom());
		final Label lb_dom= new Label(""+s.getDamage(),skin);
		float milieu_x = cible.getX()+cible.getWidth()/2;
		float milieu_y = cible.getY()+cible.getHeight()/2;
		lb_dom.setColor(Color.RED);
		//		Action t = new Action() {
		//			
		//			@Override
		//			public boolean act(float delta) {
		//				lb_dom.setFontScale(2f, 2f);
		//				lb_dom.setScale(2f, 2f);
		//				return true;
		//			}
		//		};
		Action act = new ParallelAction(sequence(moveTo(milieu_x, milieu_y), 
				moveBy((int)(30),(int) (30), 1.0f, Interpolation.linear)
				),
				sequence(color(Color.WHITE,1f),
						fadeOut(0)
						));
		lb_dom.addAction(act);
		stage.addActor(s);
		stage.addActor(lb_dom);
		
		/*
		 * Indispensable pour garder une certaine coherence dans le jeu
		 * OULALA
		 */
		try {
			while(!s.isAnimationFinished())
				Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	public void updateSkillWindow() {
		stage.getActors().removeValue(skillWindow, true);
		stage.addActor(createMySkillWindows());		
	}

}
