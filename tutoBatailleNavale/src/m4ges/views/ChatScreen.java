package m4ges.views;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.io.IOException;

import m4ges.controllers.AbstractScreen;
import m4ges.controllers.MyGame;
import reseau.UnicastClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
public class ChatScreen extends AbstractScreen {

//	final TextField tfHost;
	// Permet de connaître l'ip du client
	String ipClient;
	Stage stage;
	private Image scrollingImage;


	public ChatScreen(MyGame myGame) {
		super(myGame);
		this.batch = new SpriteBatch();
		this.stage = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), false);
//		this.tfHost = new TextField("", skin);
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

		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		stage.addActor(buildBackgroundLayer());

		// on recup l'adresse a laquelle on est conecter
//		ArrayList<String> listIps = new ArrayList<String>();
//
//		try {
//			for (Enumeration<NetworkInterface> en = NetworkInterface
//					.getNetworkInterfaces(); en.hasMoreElements();) {
//				NetworkInterface intf = en.nextElement();
//				for (Enumeration<InetAddress> enumIpAddr = intf
//						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//					InetAddress inetAddress = enumIpAddr.nextElement();
//					// System.out.println(inetAddress.getHostAddress());
//					if (!inetAddress.isLoopbackAddress()
//							&& !inetAddress.isLinkLocalAddress()
//							&& inetAddress.isSiteLocalAddress()) {
//						listIps.add(inetAddress.getHostAddress().toString());
//						ipClient = listIps.get(0);
//					}
//
//				}
//			}
//		} catch (SocketException ex) {
//			System.err.println(ex.getMessage());
//		}

		// for(String it:listIps)
		// System.out.println(it);

		// bouton de validation
		final TextButton validation = new TextButton("se connecter", skin);
		//bouton pour signaler qu'on est pret a debuter la partie
		final TextButton pret = new TextButton("Prêt", skin);
//		TextButton search = new TextButton("Rechercher des serveurs", skin);

//		Label lblHost = new Label("Host", skin);
//		lblHost.setWrap(true);

		// final SelectBox sbIps = new SelectBox(listIps.toArray(),skin);
//		if (!listIps.isEmpty())
//			tfHost.setText(listIps.get(0));
//		else
//			tfHost.setText("");
//		tfHost.setMessageText("Saisir un host");

		// recuperation des dimensions de l'ecran
		float width = Gdx.graphics.getWidth();
		//float height = Gdx.graphics.getHeight();

		// window.debug();
		final Window window = new Window("Connexion", skin);
		window.getButtonTable().add(new TextButton("X", skin))
		.height(window.getPadTop());
		window.setPosition(width * 0, 200);
		window.defaults().pad(20, 20, 20, 20);
//		window.add(lblHost);
//		window.add(tfHost);
		window.row();
		window.add(validation);
		window.row();
//		window.add(search);
		window.pack();

		// stage.addActor(new Button("Behind Window", skin));
		stage.addActor(window);
		validation.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
//				game.chatClient = new GameClient(tfHost.getText(), vue, game);
				ChatWindow cw = new ChatWindow(game);
				final UnicastClient uc = new UnicastClient(game);
				uc.chatWindow = cw;
		
				game.setMC(uc);
				try {
					uc.lancerClient();
				} catch (IOException e) {
					e.printStackTrace();
				}
				window.removeActor(validation);
				window.add(pret);
				window.row();
				/*
				 * J'ai mit un listener dans un autre
				 * c'est degueu, si tu veux changer vas y
				 * mais faut ajouter UnicastClient en param du coup
				 */
				pret.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						try {
							uc.estPret();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				
				stage.addActor(cw.getWindow());
//				
			}
		});


	}
	/**
	 * 
	 */
	private Image buildBackgroundLayer() {
		TextureAtlas atlas = MyGame.manager.get("ui/scroll.pack",
				TextureAtlas.class);
		scrollingImage = new Image(atlas.findRegion("Scroll_balcon"));
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
