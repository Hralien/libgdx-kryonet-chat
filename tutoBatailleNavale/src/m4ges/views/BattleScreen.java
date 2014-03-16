package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.io.IOException;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Bar;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.Vague;
import m4ges.models.classes.Joueur;
import m4ges.models.monster.Monstre;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
		this.fg = new Group();

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
		batch.draw(battle_bg, 0, 0, Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);
		if (selected != null) {
			batch.draw(battle_arrow, selected.getOriginX(),
					selected.getOriginY() + selected.getHeight());
		}
		batch.end();
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			int total=0;
			for (final Personnage it : currentVague.getMonsters()) {

				System.out.println("name"+(Personnage)it+"o X"+it.getX()+"o Y"+it.getOriginY()+"\np X"+it.getX()+"p Y"+it.getY());
				System.err.println("nb listener"+it.getListeners().size);
			}
			for (final Personnage it : super.game.playersConnected) {
				System.out.println("name"+(Personnage)it+"o X"+it.getX()+"o Y"+it.getOriginY()+"\np X"+it.getX()+"p Y"+it.getY());
				System.err.println("nb listener"+it.getListeners().size);

			}
			//			for(Actor it: stage.getActors()){
			//				if(it instanceof Personnage){
			//					System.out.println("name"+(Personnage)it+"o X"+it.getX()+"o Y"+it.getOriginY()+"\np X"+it.getX()+"p Y"+it.getY());
			//					total+=1;
			//				}
			//			}
			System.err.println("total-listener");
		}
		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(this.stage);
		currentVague = Vague.loadVague(numeroVague);

		game.getMC().setMonstres(currentVague.getMonsters());

		TextureAtlas atlasMap = MyGame.manager.get("ui/maps.pack",
				TextureAtlas.class);
		battle_bg = new TextureRegion(atlasMap.findRegion(currentVague.getNameVague()));

		TextureAtlas atlas = MyGame.manager.get("ui/battleui.pack",
				TextureAtlas.class);

		battle_info = new TextureRegion(atlas.findRegion("battle_ui"));
		battle_info2 = new TextureRegion(atlas.findRegion("battle_ui2"));
		battle_skill = new TextureRegion(atlas.findRegion("battle_ui_spell"));
		battle_arrow = new TextureRegion(atlas.findRegion("fleche"));

		lb_info = buildLabelMessage("Selectionner un monstre et lancer un sort");


		update();
		this.stage.addActor(buildVagueInfo());


	}

	public void update(){

		stage.clear();
		this.stage.addActor(buildPersoLayer());
		this.stage.addActor(buildMonsterLayer());
		this.stage.addActor(createMySkillWindows());
		this.stage.addActor(createMyInfoWindows());
		this.stage.addActor(createSelectedWindows());
		this.stage.addActor(lb_info);
		//this.stage.addActor(fg);

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
					it.resetAnimation();
					for (Actor it : stage.getActors()) {
						if(it instanceof Skill)
							stage.getActors().removeValue(it, true);
					}
					if(selected!=null){
						it.setPosition(selected.getX(), selected.getY());
						update();
						stage.addActor(it);
						try {
							game.mc.lancerSort(selected, it);
							stage.getActors().removeValue(selectWindow, true);
							stage.addActor(createSelectedWindows());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else{
						lb_info.setText("Selectionner un monstre et lancer un sort");
					}
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
		selectWindow.setBounds(Constants.VIEWPORT_GUI_WIDTH, 0,battle_skill.getRegionWidth(), battle_skill.getRegionHeight());
		if (selected == null)
			return selectWindow;
		selectWindow.add(new Label("name:" + selected.getName(), skin));
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
			it.setOrigin(100 + width / 3 + i, 50 + height / 4 + i);
			it.setBounds(it.getOriginX(), it.getOriginY(), it.getWidth(),
					it.getHeight());
			it.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
					System.out.println("[" + it.getName() + "]" + "down");
					selected = it;
					System.err.println("selected:"+selected);
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
			it.clear();
			it.setVisible(true);
			it.setOrigin(width / 3 + i, height / 4 + i);
			it.setBounds(it.getOriginX(), it.getOriginY(), it.getWidth(),
					it.getHeight());
			it.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					System.out.println("[" + it.getName() + "]" + "down");
					selected = it;
					System.err.println("selected:"+selected);
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
	private Actor buildVagueInfo() {
		Label lblVague = new Label("Vague " + numeroVague, skin);
		lblVague.setPosition(Constants.VIEWPORT_GUI_WIDTH / 2 - lblVague.getWidth()/2,	(float) (Constants.VIEWPORT_GUI_HEIGHT /2  ));
		lblVague.setOrigin(Constants.VIEWPORT_GUI_WIDTH / 2 - lblVague.getWidth()/2,	(float) (Constants.VIEWPORT_GUI_HEIGHT /2  ));
		lblVague.pack();
		lblVague.addAction(sequence(Actions.fadeIn( 0.0001f ), Actions.fadeOut( 3f )));
		return lblVague;
	}

	/**
	 * affiche le message
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
		lb_info.setPosition(Constants.VIEWPORT_GUI_WIDTH /2 - lb_info.getWidth()/2,	(float) (Constants.VIEWPORT_GUI_HEIGHT*0.95 ));
		lb_info.setOrigin(Constants.VIEWPORT_GUI_WIDTH / 2 - lb_info.getWidth()/2,	(float) (Constants.VIEWPORT_GUI_HEIGHT*0.95 ));
		lb_info.pack();
		return lb_info;

	}
	/**
	 * affiche sur l'ecran 
	 * @param s le skill en question
	 * @param lanceur le personnage lanceur du skill
	 * @param cible le personnage qui recoit le skill
	 */
	private void afficheSkill(Skill s, Personnage lanceur, Personnage cible){
		s.resetAnimation();
		for (Actor it : stage.getActors()) {
			if(it instanceof Skill)
				stage.getActors().removeValue(it, true);
		}
		s.setPosition(selected.getX(), selected.getY());
		lb_info.setText(lanceur.getName()+" utilise "+s.getName()+" sur "+cible.getName());
		stage.addActor(s);
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
