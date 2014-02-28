package m4ges.views;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;
import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Encyclopedie;
import m4ges.models.Personnage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Menu principal selection de ce qu'on veut faire
 * 
 * @author Florian
 * 
 */
public class EncyclopedieScreen extends AbstractScreen {
	/**
	 * {@link Stage}
	 */
	private Stage stage;


	//window new player
	private Window winListMonster;
	/**
	 * la liste des mobs déjà rencontré
	 */
	private Encyclopedie dico;
	/**
	 * le personnage (monstre) selectionné
	 */
	private Personnage selected;
	
	public EncyclopedieScreen(MyGame myGame) {
		super(myGame);
		this.stage = new Stage(Gdx.graphics.getWidth(),	Gdx.graphics.getHeight(), false);

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
		batch.setProjectionMatrix(stage.getCamera().combined);

		stage.act(delta);
		stage.draw();
		//		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// on dit a l'appli d'ecouter ce stage quand la methode show est appelee
		Gdx.input.setInputProcessor(stage);

		dico = Encyclopedie.loadEncyclopedie();
		Stack stack = new Stack();
		stack.add(buildMonsterLayer());
		stack.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		this.stage.clear();
		this.stage.addActor(stack);


	}
	private Window buildCreateCharacterWindow(){

		winListMonster = new Window("Selection Perso", skin);
		winListMonster.setPosition(650, 200);
		winListMonster.defaults().pad(20, 20, 20, 20);
		winListMonster.row().fill().expandX();
		winListMonster.add(buildMonsterLayer());
		winListMonster.row();
		winListMonster.pack();
		showNewCharacterWindow(true, true);
		return winListMonster;
	}
	/**
	 * 
	 * @return
	 */
	private Table buildMonsterLayer(){
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		Table layer = new Table();
		int i=0;
		for(final Personnage it: dico.getMonsters()){
			it.setVisible(true);
			it.setOrigin(width/6+i, 50+height/6+i);
			it.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"down");
					selected = it;
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

	/**
	 * 
	 * @param visible
	 * @param animated
	 */
	private void showNewCharacterWindow (boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		winListMonster.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
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
