package m4ges.views;


import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Vague;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

	public ResultScreen(MyGame myGame) {
		super(myGame);		
		this.stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), false);
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
		// on recup l'adresse a laquelle on est conecter
		TextButton validation = new TextButton("Continuer", skin);

		// recuperation des dimensions de l'ecran
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		// window.debug();
		Window window = new Window("Résultat", skin);
		window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(width * 0, 200);
		window.defaults().pad(20, 20, 20, 20);

		window.row();
		window.add(validation);
		window.row();
		window.add(buildResultatLayer());
		window.pack();

		stage.addActor(window);
		stage.addActor(game.mc.chatWindow.getWindow());
		
		validation.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.getMC().pretPourVagueSuivante();
			}
		});


	}
	
	private Table buildResultatLayer(){
		Table resultat = new Table(skin);
		resultat.add(new Label("NomVague:"+lastVague.getNameVague(),skin));
		resultat.add(new Label("Vos points:512",skin));
		return resultat;
		
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
