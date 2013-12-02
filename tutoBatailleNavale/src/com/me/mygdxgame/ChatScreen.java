package com.me.mygdxgame;

import gameMechanic.Skill;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chat.ChatClient;
import chat.Network;
import chat.Network.SkillNumber;

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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * lancement du chat apres saisie du pseudo et de l'adresse
 * 
 * @author Florian
 * 
 */
public class ChatScreen implements Screen {
	
	final TextField tfHost;
	//Permet de conna�tre l'ip du client
	String ipClient;
	Skin skin;
	Stage stage;
	SpriteBatch batch;

	Label fpsLabel;
	ChatClient cc;
	MyGame game;

	public SkillNumber showSkillNumber;

	public ChatScreen(MyGame myGame){
		this.game=myGame;
		this.batch = new SpriteBatch();
		this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		this.stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		tfHost = new TextField("", skin);
		//on recup l'adresse a laquelle on est conecter
		ArrayList<String> listIps=new ArrayList<String>();

		try {			
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					//					System.out.println(inetAddress.getHostAddress());
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
						listIps.add(inetAddress.getHostAddress().toString());
						ipClient = listIps.get(0);
					//	System.out.println(inetAddress.getHostAddress().toString());
					}

				}
			}
		} catch (SocketException ex) {
			System.err.println(ex.getMessage());
		}
		
//		for(String it:listIps)
//			System.out.println(it);


		//bouton de validation
		TextButton validation = new TextButton("se connecter", skin);
		TextButton search = new TextButton("Rechercher des serveurs", skin);

		//un label pour montrer les fps
		Label myLabel = new Label("Pseudo", skin);
		myLabel.setWrap(true);

		CheckBox checkBox = new CheckBox("Memoriser", skin);
		final TextField tfPseudo = new TextField("", skin);
		tfPseudo.setMessageText("Saisir un pseudo!");

//		final SelectBox sbIps = new SelectBox(listIps.toArray(),skin);
		if(! listIps.isEmpty())
			tfHost.setText(listIps.get(0));
		else 
			tfHost.setText("");
		tfHost.setMessageText("Saisir un host");
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
		window.row();
		window.add(checkBox);
		window.row();
		window.add(tfPseudo).minWidth((float) (width*.4)).expandX().fillX().colspan(6);
		window.row();
		window.add(tfHost).minWidth((float) (width*.4)).expandX().fillX().colspan(6);
		window.row();
		window.add(validation);
		window.row();
		window.add(search);
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



		final ChatScreen vue = this;
		validation.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if(game.player!=null)
				cc = new ChatClient(tfHost.getText(), tfPseudo.getText(),game.player,vue);
				else
					cc = new ChatClient(tfHost.getText(), tfPseudo.getText(),null,vue);
				stage.addActor(cc.chatWindow.getWindow());
			}
		});
		
		search.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				searchServ(ipClient);
			}
			
		});
	}

	/**
	 * Verifie que l'adresse est bien une adresse ip (et non mac) et qu'elle est
	 * diff�rente du localhost
	 * 
	 * @param ip
	 * @return
	 */
//	public boolean valideIpAdress(String ip) {
//		if (ip.equals("127.0.0.1"))
//			return false;
//		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
//		Matcher matcher = pattern.matcher(ip);
//		return matcher.matches();
//	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		fpsLabel.setText("fps: " + Gdx.graphics.getFramesPerSecond());
		batch.begin();
		if(showSkillNumber!=null){
			Skill.selectSkillFromSkillNumber(showSkillNumber).getEffect().draw(batch,delta);
			//si l'animation est finie on remets � null
			if(Skill.selectSkillFromSkillNumber(showSkillNumber).getEffect().isComplete()){
				showSkillNumber = null;
			}
		}
		batch.end();
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
//		Client client = new Client();
//		java.util.List<InetAddress> addresses = client.discoverHosts(Network.portUDP, 5000);
//		System.out.print(addresses); 
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
	
	/*
	 * Methode permettant de choisir un serveur
	 */
	public void searchServ(String ip){
		ArrayList<String> listServ = new ArrayList<String>();
		String[] s = ip.split("\\.");
		String test = new String();
		test = s[0].concat(".").concat(s[1]).concat(".").concat(s[2]).concat(".");
		Client c = new Client();
		c.start();
		/*
		 * Pour tout les adresses du r�seau
		 * (C'est degueu et ca marche mal mais au moins
		 * on peut choisir un reseau)
		 */
		for(int i = 0; i < 255; i++){
			String ipTest = null;
			InetAddress it;
			//On essaye de se co et si ca marche on l'ajoute
			try{
				ipTest = test.concat(Integer.toString(i));
				c.connect(200, ipTest, Network.portTCP, Network.portUDP);
		//		System.err.print(ipTest);
				listServ.add(ipTest);
			}catch (IOException ex){
			//	System.out.println(ip);
			}
		}
		//Si la liste est vide
		if(listServ.size()==0){
			game.androidUI.showAlertBox("No server found", "No server found", "OK", stage);
		}else{
			
			final Window choixServ = new Window("Choisissez votre Serveur", skin);
			choixServ.setPosition((float) (Gdx.graphics.getHeight()*0.5), 200);
			final List serv = new List(listServ.toArray(), skin);
			TextButton ok = new TextButton("ok", skin);

			choixServ.add(serv);
			choixServ.row();
			choixServ.add(ok);
			choixServ.row();
			choixServ.pack();
			
			stage.addActor(choixServ);
			
			ok.addListener(new ChangeListener(){

				public void changed(ChangeEvent event, Actor actor) {
					tfHost.setText(serv.getSelection());
					choixServ.remove();
				}
				
			});
				
		}
	}


}
