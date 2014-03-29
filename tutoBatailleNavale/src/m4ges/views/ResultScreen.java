package m4ges.views;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.io.IOException;
import java.util.ArrayList;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Item;
import m4ges.models.Vague;
import m4ges.models.monster.Monstre;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;

/**
 * lancement du chat apres saisie du pseudo et de l'adresse
 * 
 * @author Florian
 * 
 */
public class ResultScreen extends AbstractScreen {

	Stage stage;

	private Vague lastVague;
	private ArrayList<Item> itemsObtenu;
	private Image scrollingImage;

	public ResultScreen(MyGame myGame) {
		super(myGame);		
		this.stage = new Stage(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, true);
		this.itemsObtenu = new ArrayList<Item>();
	}

	@Override
	public void resize(int width, int height) {
		Vector2 size = Scaling.fit.apply(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;
        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, true, viewportX, viewportY, viewportWidth, viewportHeight);	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();

	}

	@Override
	public void render(float delta) {
		super.render( delta );

		stage.act();
		stage.draw();
		//Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		lastVague = Vague.loadVague(game.currentVague);
		
		calculRecompenses();
		// on recup l'adresse a laquelle on est conecter
		TextButton validation = new TextButton("Continuer", skin);

		// recuperation des dimensions de l'ecran
		float width = Gdx.graphics.getWidth();
		//float height = Gdx.graphics.getHeight();

		// window.debug();
		Window stats = new Window("Résultat", skin);
		stats.getButtonTable().add(new TextButton("X", skin)).height(stats.getPadTop());
		stats.setPosition(width * 0, 200);
		stats.defaults().pad(20, 20, 20, 20);
		stats.add(buildResultatLayer());
		stats.row();
		stats.add(validation);
		stats.pack();

		Window recompenses = new Window("Récompenses", skin);
		recompenses.getButtonTable().add(new TextButton("X", skin)).height(stats.getPadTop());
		recompenses.setPosition(300, 200);
		recompenses.defaults().pad(20, 20, 20, 20);
		recompenses.row();
		recompenses.add(buildRecompensesLayer());
		recompenses.pack();

		stage.addActor(buildBackgroundLayer());
		stage.addActor(stats);
		stage.addActor(recompenses);
		stage.addActor(game.mc.chatWindow.getWindow());

		validation.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				try {
					game.getMC().estPret();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Table buildRecompensesLayer(){
		Table recompenses = new Table();
		if(itemsObtenu.isEmpty()){
			recompenses.add(new Label("Vous n'avez rien gagné", skin));
		}
		else{
			for (Item it : itemsObtenu) {
				recompenses.add(new ImageButton(it.getImage().getDrawable()));
				recompenses.add(new Label(it.getName(),skin));
			}
		}
		return recompenses;
	}
	private Table buildResultatLayer(){
		Table resultat = new Table(skin);
		resultat.add(new Label("NomVague:"+lastVague.getNameVague(),skin));
		resultat.add(new Label("Vos points:512",skin));
		return resultat;

	}

	private void calculRecompenses() {
		itemsObtenu.clear();
		//on prend un nombre au hasard
		float val = MathUtils.random(1)*100;
		//on parcourt la liste de monstre de la vague precedante
		System.out.println(lastVague.getNameVague());
		for (Monstre monster : lastVague.getMonsters()) {
			System.out.println(monster.getName());
			System.out.println(monster.getDropPossible());
			//on parcout les items qui peuvent etre drop
			for (Item item : monster.getDropPossible()) {
				//si notre valeur est <= alors on gagne l'item
				if(val<=item.getRate())
					itemsObtenu.add(item);
			}
		}
	}

	/**
	 * 
	 */
	private Image buildBackgroundLayer() {
		TextureAtlas atlas = MyGame.manager.get("ui/scroll.pack",
				TextureAtlas.class);
		scrollingImage = new Image(atlas.findRegion("Scroll_desert"));
		scrollingImage.setPosition(0, 0);
		scrollingImage.setHeight(Gdx.graphics.getHeight());
		RepeatAction ra = new RepeatAction();
		ra.setAction(sequence(moveTo(0, 0), moveBy((int)(-scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear),
				moveBy((int)(scrollingImage.getWidth()*.6), 0, 20.0f, Interpolation.linear)));
		ra.setCount(RepeatAction.FOREVER);
		scrollingImage.addAction(ra);
		return scrollingImage;
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
