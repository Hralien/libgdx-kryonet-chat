package com.me.mygdxgame;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UITest2 extends GdxTest {


	Skin skin;
	Stage stage;
	SpriteBatch batch;
	Texture texture1;
	Texture texture2;
	Label fpsLabel;
	ChatClient cc;

	@Override
	public void create () {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		texture1 = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
		texture2 = new Texture(Gdx.files.internal("data/badlogic.jpg"));
		TextureRegion image = new TextureRegion(texture1);
		TextureRegion imageFlipped = new TextureRegion(image);
		imageFlipped.flip(true, true);
		TextureRegion image2 = new TextureRegion(texture2);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);

		// Group.debug = true;

		ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(image);
		style.imageDown = new TextureRegionDrawable(imageFlipped);
		ImageButton iconButton = new ImageButton(style);

		TextButton validation = new TextButton("se connecter", skin);


		Label myLabel = new Label("Pseudo", skin);
		myLabel.setWrap(true);

		Table t = new Table();
		t.row();
		t.add(myLabel);

		t.layout();

		CheckBox checkBox = new CheckBox("Memoriser", skin);
		final TextField tfPseudo = new TextField("", skin);
		tfPseudo.setMessageText("Saisir un pseudo!");
		final TextField tfHost = new TextField("", skin);
		tfHost.setMessageText("Saisir un host");
		fpsLabel = new Label("fps:", skin);


		// window.debug();
		Window window = new Window("Connexion", skin);
		window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(50, 200);
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.add(iconButton);
		window.row();
		window.add(checkBox);
		window.row();
		window.add(tfPseudo).minWidth(100).expandX().fillX().colspan(4);
		window.row();
		window.add(tfHost).minWidth(100).expandX().fillX().colspan(4);
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


		iconButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				new Dialog("Some Dialog", skin, "dialog") {
					protected void result (Object object) {
						System.out.println("Chosen: " + object);
					}
				}.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
				.key(Keys.ESCAPE, false).show(stage);
			}
		});

		validation.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				cc = new ChatClient(tfHost.getText(), tfPseudo.getText());
				stage.addActor(cc.chatWindow.getWindow());
			}
		});
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		
	

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize (int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose () {
		stage.dispose();
		skin.dispose();
		texture1.dispose();
		texture2.dispose();
	}
}

