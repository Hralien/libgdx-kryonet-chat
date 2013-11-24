package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Menu principal
 * selection de ce qu'on veut faire
 * @author Florian
 *
 */
public class CreateCharacterScreen implements Screen {

	Skin skin;
	Stage stage;
	SpriteBatch batch;
	Texture texture1;
	Texture texture2;
	Label fpsLabel;

	MyGame game;
	public CreateCharacterScreen(MyGame game){
		this.game=game;
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);

		// Group.debug = true;

		TextButton validation = new TextButton("creer un perso", skin);
		

		final TextField tfPseudo = new TextField("", skin);
		tfPseudo.setMessageText("Saisir un pseudo!");

		String[] tabPersonnage={"Soigneur","Barbare","Mage"};

		final List listClasses= new List(tabPersonnage, skin);
		ScrollPane scrollPaneClass = new ScrollPane(listClasses,skin);

		fpsLabel = new Label("fps:", skin);


		// window.debug();
		Window window = new Window("Selection Perso", skin);
		window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(650, 200);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.row();
		window.row();
		window.add(tfPseudo).minWidth(100).expandX().fillX().colspan(4);
		window.row();
		window.add(scrollPaneClass).minWidth(100).expandX().fillX().colspan(4);
		window.row();
		window.add(validation);
		window.row();
		window.add(fpsLabel).colspan(4);
		window.pack();

		// stage.addActor(new Button("Behind Window", skin));
		stage.addActor(window);

		tfPseudo.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField textField, char key) {
				if (key == '\n') textField.getOnscreenKeyboard().show(false);
			}
		});



		validation.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				switch(listClasses.getSelectedIndex()){
				case 0:
					System.out.println("new Soigneur");
					break;
				case 1:
					System.out.println("new Barbare");
					break;
				case 2:
					System.out.println("new Mage");
					break;
				default:
					System.err.println("switch personnage error");

				}
			}
		});
	}


	@Override
	public void resize (int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		if (Gdx.input.justTouched()) // use your own criterion here
            game.setScreen(game.anotherScreen);



		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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

