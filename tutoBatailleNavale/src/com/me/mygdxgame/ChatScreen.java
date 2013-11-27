package com.me.mygdxgame;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chat.ChatClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * lancement du chat apres saisie du pseudo et de l'adresse
 * 
 * @author Florian
 * 
 */
public class ChatScreen implements Screen {

	Skin skin;
	Stage stage;
	SpriteBatch batch;
	Texture texture1;
	Texture texture2;
	Label fpsLabel;
	ChatClient cc;
	MyGame game;
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public ChatScreen(MyGame game){
		this.game=game;
		this.batch = new SpriteBatch();
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		this.texture1 = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
		TextureRegion image = new TextureRegion(texture1);
		TextureRegion imageFlipped = new TextureRegion(image);
		imageFlipped.flip(true, true);
		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

		//on recup l'adresse a laquelle on est conecter
		ArrayList<String> listIps=new ArrayList<String>();
		//		try {
		//			listIps.add(InetAddress.getLocalHost().getHostAddress());
		//		} catch (UnknownHostException e1) {
		//			// TODO Auto-generated catch block
		//			e1.printStackTrace();
		//		}
		//		try {
		//			InetAddress[] allByName = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
		//
		//			
		//			for (int i = 0; i < allByName.length; i++) {
		//				//test si != localhost et n'est pas une adresse mac
		//				if(valideIpAdress(allByName[i].getHostAddress())){
		//					listIps.add(allByName[i].getHostAddress());
		//				}
		//			}
		//		} catch (UnknownHostException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		System.out.println("listIps"+listIps.size());

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
//					System.out.println(inetAddress.getHostAddress());
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
						listIps.add(inetAddress.getHostAddress().toString());
					}

				}
			}
		} catch (SocketException ex) {
			System.err.println(ex.toString());
		}

		
		
		//bouton avec image inutile
		ImageButtonStyle style = new ImageButtonStyle(skin.get(ButtonStyle.class));
		style.imageUp = new TextureRegionDrawable(image);
		style.imageDown = new TextureRegionDrawable(imageFlipped);
		ImageButton iconButton = new ImageButton(style);

		//bouton de validation
		TextButton validation = new TextButton("se connecter", skin);

		//un label pour montrer les fps
		Label myLabel = new Label("Pseudo", skin);
		myLabel.setWrap(true);

		CheckBox checkBox = new CheckBox("Memoriser", skin);
		final TextField tfPseudo = new TextField("", skin);
		tfPseudo.setMessageText("Saisir un pseudo!");

		final SelectBox sbIps = new SelectBox(listIps.toArray(),skin);
		//		final TextField tfHost = new TextField("", skin);
		//		tfHost.setText(localHost);
		//		tfHost.setMessageText("Saisir un host");
		fpsLabel = new Label("fps:", skin);

		//recuperation des dimensions de l'ecran
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();


		// window.debug();
		Window window = new Window("Connexion", skin);
		window.getButtonTable().add(new TextButton("X", skin)).height(window.getPadTop());
		window.setPosition(width*0, 200);
		window.defaults().spaceBottom(10);
		window.row().minWidth((float) (width*.4)).fill().expandX();
		window.add(iconButton);
		window.row();
		window.add(checkBox);
		window.row();
		window.add(tfPseudo).minWidth((float) (width*.4)).expandX().fillX().colspan(6);
		window.row();
		window.add(sbIps).minWidth((float) (width*.4)).expandX().fillX().colspan(6);
		window.row();
		window.add(validation);
		window.row();
		window.add(fpsLabel).colspan(4);
		window.pack();

		// stage.addActor(new Button("Behind Window", skin));
		stage.addActor(window);

		//ajouts des listeners sur nos boutons
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
				cc = new ChatClient(sbIps.getSelection(), tfPseudo.getText());
				stage.addActor(cc.chatWindow.getWindow());
			}
		});
	}

	/**
	 * Verifie que l'adresse est bien une adresse ip (et non mac) et qu'elle est
	 * différente du localhost
	 * 
	 * @param ip
	 * @return
	 */
	public boolean valideIpAdress(String ip) {
		if (ip.equals("127.0.0.1"))
			return false;
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		texture1.dispose();
		texture2.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);

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
