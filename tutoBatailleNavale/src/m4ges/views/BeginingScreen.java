package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import java.util.ArrayList;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.monster.Flower;
import m4ges.models.monster.Lutin;
import m4ges.models.monster.Phantom;
import m4ges.models.monster.Skeleton;

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
 * @author Florian
 *
 */
public class BeginingScreen extends AbstractScreen {

	/**
	 * {@link Stage}
	 */
	private Stage stage;
	/**
	 * fond
	 */
	private TextureRegion battle_bg;
	private TextureRegion battle_info;
	private TextureRegion battle_skill;
	/**
	 * 
	 */
	private ArrayList<Personnage> mobList;
	/**
	 * premier plan
	 */
	private Group fg;
	/**
	 * 
	 */
	private Window winVagueInfo;
	
	public BeginingScreen(MyGame myGame){
		super(myGame);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        fg = new Group();

	}



	@Override
	public void resize (int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
		batch.dispose();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		batch.setProjectionMatrix(stage.getCamera().combined);
		
		batch.begin();
		batch.draw(battle_bg, 0, battle_info.getRegionHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		stage.act(delta);
		stage.draw();
		//Table.drawDebug(stage);
	}

	@Override
	public void show() {
		//on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);

		TextureAtlas atlas = MyGame.manager.get("ui/battleui.pack", TextureAtlas.class);

        battle_bg = new TextureRegion(atlas.findRegion("battle_background"));
        battle_info = new TextureRegion(atlas.findRegion("battle_ui"));
        battle_skill = new TextureRegion(atlas.findRegion("battle_ui_spell"));

		mobList = new ArrayList<Personnage>();
		mobList.add(new Skeleton());
		mobList.add(new Flower());
		mobList.add(new Lutin());
		mobList.add(new Phantom());
		
		Stack stack = new Stack();
		stack.add(buildPersoLayer());
		stack.add(buildMonsterLayer());
		stack.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		this.stage.addActor(stack);
		this.stage.addActor(createMyInfoWindows());
		this.stage.addActor(createMySkillWindows());
		this.stage.addActor(fg);
		this.fg.addActor(buildVagueInfo());
		showVagueWindow();

	}





	private Table createMySkillWindows() {
		WindowStyle ws = new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(battle_skill));
		Window skillWindow = new Window("",ws);
		int i=0;
		for (final Skill it : super.game.player.getListSkills()) {
			TextButton skillButton = new TextButton(it.getSkillName()
					+ " cost:" + it.getSpCost(), skin);
			skillButton.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					fg.clear();
					it.resetAnimation();
					it.setSize(it.getCurrentFrame().getRegionWidth(), it.getCurrentFrame().getRegionHeight());
					fg.addActor(it);
				}
			});
			skillWindow.add(skillButton);
			if(i%2==1)
			skillWindow.row();
			i++;
		}
		skillWindow.setBounds(battle_info.getRegionWidth()-30, 0, battle_skill.getRegionWidth(),battle_skill.getRegionHeight());
		
		return skillWindow;
	}
	private Window createMyInfoWindows() {
		WindowStyle ws = new WindowStyle(new BitmapFont(), Color.BLACK, new TextureRegionDrawable(battle_info));
		Window infoWindow = new Window("", ws);
		infoWindow.row();
		infoWindow.add(new Label("hp:"+game.player.getHp(),skin));
		infoWindow.row();
		infoWindow.add(new Label("sp:"+game.player.getMana(),skin));
		infoWindow.pack();
		infoWindow.setBounds(0, 0, battle_info.getRegionWidth(),battle_info.getRegionHeight());
		return infoWindow;
	}
	private Table buildPersoLayer(){
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		Table layer=new Table();
		int i=0;
		for(final Personnage it : super.game.playersConnected){
			it.setVisible(true);
			it.setOrigin(100+width/3+i, 50+height/2+i);

			it.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"down");
					return true;
				}
				
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"up");
				}
			});
			i+=50;
			layer.addActor(it);
		}
		return layer;
	}
	private Table buildMonsterLayer(){
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		Table layer = new Table();
		int i=0;
		for(final Personnage it: mobList){
			it.setVisible(true);
			it.setOrigin(width/3+i, height/2+i);
			it.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"down");
					return true;
				}
				
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"up");
				}
			});
			i+=50;
			layer.addActor(it);
		}
		return layer;
	}
	private Window buildVagueInfo() {
		winVagueInfo = new Window("Info", skin);
		Label lblVague = new Label("vague 1", skin);
		winVagueInfo.add(lblVague);
		winVagueInfo.setSize(300,200);
		winVagueInfo.setPosition(((float) (Gdx.graphics.getWidth() * 0.5)-winVagueInfo.getWidth()/2),((float) (Gdx.graphics.getHeight() * 0.5)));
		return winVagueInfo;
	}
	/**
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showVagueWindow() {
		winVagueInfo.addAction(sequence(touchable(Touchable.disabled), alpha(0.8f, 2.5f)));
		winVagueInfo.addAction(sequence(touchable(Touchable.disabled), alpha(0.0f, 2.5f)));
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

