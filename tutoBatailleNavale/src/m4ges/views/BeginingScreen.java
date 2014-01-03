package m4ges.views;

import java.util.ArrayList;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Flower;
import m4ges.models.Lutin;
import m4ges.models.Personnage;
import m4ges.models.Phantom;
import m4ges.models.Skeleton;
import m4ges.models.Skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryonet.Client;

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
	private Sound sound;
	private TextureRegion battle_bg;
	private Skill skillToRender;
	private TextureRegion battle_info;
	private TextureRegion battle_skill;
	private ArrayList<Personnage> mobList;
	private Group fg;
	
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
		sound.dispose();
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
		this.stage.addActor(stack);
		this.stage.addActor(createMySkillWindows());
		this.stage.addActor(createMyInfoWindows());
		this.stage.addActor(fg);

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

