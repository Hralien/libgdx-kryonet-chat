package m4ges.views;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import reseau.GameClient;
import reseau.Network;
import reseau.Network.SkillNumber;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import m4ges.models.Skill;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.kryonet.Client;

/**
 * lancement du chat apres saisie du pseudo et de l'adresse
 * 
 * @author Florian
 * 
 */
public class ChatScreen extends AbstractScreen {

	final TextField tfHost;
	// Permet de connaître l'ip du client
	String ipClient;
	Stage stage;

	public SkillNumber showSkillNumber;

	public ChatScreen(MyGame myGame) {
		super(myGame);
		this.batch = new SpriteBatch();
		this.stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), false);
		this.tfHost = new TextField("", skin);
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
		if (showSkillNumber != null) {
			stage.addActor(Skill.selectSkillFromSkillNumber(showSkillNumber));
			// si l'animation est finie on remets à null
			if (Skill.selectSkillFromSkillNumber(showSkillNumber).isAnimationFinished()) {
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
		// on recup l'adresse a laquelle on est conecter
		ArrayList<String> listIps = new ArrayList<String>();

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					// System.out.println(inetAddress.getHostAddress());
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						listIps.add(inetAddress.getHostAddress().toString());
						ipClient = listIps.get(0);
					}

				}
			}
		} catch (SocketException ex) {
			System.err.println(ex.getMessage());
		}

		// for(String it:listIps)
		// System.out.println(it);

		// bouton de validation
		TextButton validation = new TextButton("se connecter", skin);
		TextButton search = new TextButton("Rechercher des serveurs", skin);

		Label lblHost = new Label("Host", skin);
		lblHost.setWrap(true);

		// final SelectBox sbIps = new SelectBox(listIps.toArray(),skin);
		if (!listIps.isEmpty())
			tfHost.setText(listIps.get(0));
		else
			tfHost.setText("");
		tfHost.setMessageText("Saisir un host");

		// recuperation des dimensions de l'ecran
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		// window.debug();
		Window window = new Window("Connexion", skin);
		window.getButtonTable().add(new TextButton("X", skin))
		.height(window.getPadTop());
		window.setPosition(width * 0, 200);
		window.defaults().pad(20, 20, 20, 20);
		window.add(lblHost);
		window.add(tfHost);
		window.row();
		window.add(validation);
		window.row();
		window.add(search);
		window.pack();

		// stage.addActor(new Button("Behind Window", skin));
		stage.addActor(window);

		final ChatScreen vue = this;
		validation.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.chatClient = new GameClient(tfHost.getText(), vue, game);

				stage.addActor(game.chatClient.chatWindow.getWindow());
			}
		});

		search.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							Gdx.app.postRunnable(new Runnable() {
								public void run() {
									Client client = new Client();
									final List<InetAddress> address = client.discoverHosts(
											Network.portUDP, 5000);
									for (InetAddress it : address)
										game.listHost.add(it.toString().substring(1,it.toString().length()));
									if (game.listHost.size() != 0) {
										final Window choixServ = new Window(
												"Choisissez votre Serveur",
												skin);
										choixServ.setPosition(
												(float) (Gdx.graphics
														.getHeight() * 0.5),
														200);
										final com.badlogic.gdx.scenes.scene2d.ui.List serv = new com.badlogic.gdx.scenes.scene2d.ui.List(
												game.listHost.toArray(), skin);
										TextButton ok = new TextButton("ok",
												skin);
										choixServ.add(serv);
										choixServ.row();
										choixServ.add(ok);
										choixServ.row();
										choixServ.pack();
										stage.addActor(choixServ);
										ok.addListener(new ChangeListener() {
											public void changed(
													ChangeEvent event,
													Actor actor) {
												tfHost.setText(serv
														.getSelection());
												choixServ.remove();
											}
										});
									}
								}
							});
						}
					}).start();
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}
		});
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
