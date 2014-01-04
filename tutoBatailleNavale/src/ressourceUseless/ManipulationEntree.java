package ressourceUseless;


import reseau.GameClient;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ManipulationEntree implements ApplicationListener {
	private SpriteBatch batch ;
	private BitmapFont font ;
	private String texteInitial;
	private String titreBoiteDialogue;
	private String message;
	private boolean afficher,connection;
	private GameClient cc;
	private String pseudo;
	private String host;

	@Override
	public void create() {
		afficher = false;
		batch = new SpriteBatch();
		font = new BitmapFont();
		Gdx.input.getTextInput(new TextInputListener() {
			@Override
			public void input(String texteSaisi) {
				pseudo = texteSaisi;
				message = "Connexion"+pseudo;

			}
			@Override
			public void canceled() {
				message = "Au revoir Monsieur et tant pis pour vous";
				afficher =true;
			}
		}, "Host", "Entrer l'adresse de l'host");
		Gdx.input.getTextInput(new TextInputListener() {
			@Override
			public void input(String texteSaisi) {
				host=texteSaisi;
				afficher =true;
				connection =true;
				message = "host"+host;

			}
			@Override
			public void canceled() {
				message = "Au revoir Monsieur et tant pis pour vous";
				afficher =true;
			}
		}, "Pseudo", "Entrer votre pseudo");
		
	}
	@Override
	public void dispose() {
	}
	@Override
	public void pause() {
	}
	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if(afficher)
		{
			batch.begin();
			font.draw(batch, message, 10, 200);
			batch.end();
		}
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
//			cc = new ChatClient(pseudo, host,null);
		}
	}
	@Override
	public void resize(int arg0, int arg1) {
	}
	@Override
	public void resume() {
	}
}