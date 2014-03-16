package ressourceUseless;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;
import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Encyclopedie;
import m4ges.models.Personnage;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
		this.stage = new Stage(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT, true);
		this.stage.setCamera(cameraGUI);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width,height, true);
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
		Label lblMonsterDesc = new Label("Desc:"+selected.getDesc(), skin);
		Label lblMonsterHp = new Label("Hp:"+selected.getHp(), skin);
		winListMonster = new Window("Information"+selected.getName(), skin);
		winListMonster.pad(20,20,20,20);
		winListMonster.add(lblMonsterDesc);
		winListMonster.row();
		winListMonster.add(lblMonsterHp);
		winListMonster.pack();
		winListMonster.setPosition(Constants.VIEWPORT_GUI_WIDTH/2 - winListMonster.getWidth(), 50);
		showNewCharacterWindow(true, true);
		return winListMonster;
	}
	/**
	 * 
	 * @return
	 */
	private Table buildMonsterLayer(){
		Table layer = new Table();
		int i=0;
		for(final Personnage it: dico.getMonsters()){
			it.setState(Personnage.WAIT);
			it.setVisible(true);
			it.setPosition(100*i, it.getHeight()/2);
			it.setBounds(100*i,it.getHeight()/2, it.getWidth(), it.getHeight());
			it.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"down");
					selected = it;
					stage.getActors().removeValue(winListMonster, true);
					stage.addActor(buildCreateCharacterWindow());
					return true;
				}

				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("["+it.getName()+"]"+"up");
				}
			});
			i+=1;
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
