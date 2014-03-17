package m4ges.views;


import java.util.ArrayList;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Item;
import m4ges.models.Vague;
import m4ges.models.monster.Monstre;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

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

	public ResultScreen(MyGame myGame) {
		super(myGame);		
		this.stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), false);
		this.itemsObtenu = new ArrayList<Item>();
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
		super.render( delta );
		batch.begin();

		batch.end();
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		calculRecompenses();
		lastVague = Vague.loadVague(game.currentVague);
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

		stats.row();
		stats.add(validation);
		stats.row();
		stats.add(buildResultatLayer());
		stats.pack();
		
		Window recompenses = new Window("Résultat", skin);
		recompenses.getButtonTable().add(new TextButton("X", skin)).height(stats.getPadTop());
		recompenses.setPosition(300, 200);
		recompenses.defaults().pad(20, 20, 20, 20);
		recompenses.row();
		recompenses.add(buildRecompensesLayer());
		recompenses.pack();

		stage.addActor(stats);
		stage.addActor(recompenses);
		stage.addActor(game.mc.chatWindow.getWindow());

		validation.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.getMC().pretPourVagueSuivante();
			}
		});
	}

	private Table buildRecompensesLayer(){
		Table recompenses = new Table();
		for (Item it : itemsObtenu) {
			recompenses.add(new ImageButton(it.getImage().getDrawable()));
			recompenses.add(new Label(it.getName(),skin));
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
		float val = MathUtils.random(1);
		for (Monstre monster : lastVague.getMonsters()) {
			for (Item item : monster.getDropPossible()) {
				if(val>=item.getRate())
					itemsObtenu.add(item);
			}
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

}
