
package chat;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.views.ChatScreen;

import chat.Network.ChatMessage;
import chat.Network.ConstantOrder;
import chat.Network.RegisterName;
import chat.Network.RequestName;
import chat.Network.SkillNumber;
import chat.Network.UpdateNames;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;


public class ChatClient {
	public Client client;
	public ChatWindow chatWindow;
	private ChatScreen myVue;
	private MyGame game;
	public 	String name;

	@SuppressWarnings("unchecked")
	public ChatClient (final String adresse, ChatScreen vue, MyGame myGame) {
		this.client = new Client();
		this.client.start();
		this.myVue = vue;
		this.game = myGame;
		this.name = game.player.getName();
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		//		game.playersConnected = (ArrayList<Personnage>) ObjectSpace.getRemoteObject(client, Network.PLAYER, gameMechanic.IPlayer.class);

		this.client.addListener(new Listener() {

			@Override
			public void connected (Connection connection) {
				//				RegisterName registerName = new RegisterName();
				//				registerName.name = game.player.getName();
				//				client.sendTCP(registerName);
				connection.setName(game.player.getName());
				//				client.sendTCP(new PersonnageConnection(game.player));
				//				client.sendTCP(new RequestName());
				client.sendTCP(game.player);

			}

			@Override
			public void received (Connection connection, Object object) {

				if(object instanceof RequestName){
					System.err.print("trolol");
				}

				if (object instanceof UpdateNames) {
					System.out.println("[client]: updateNames reçu");
					UpdateNames updateNames = (UpdateNames)object;
					chatWindow.setNames(updateNames.names);
					return;
				}

				if (object instanceof ChatMessage) {
					System.out.println("[client]: chat message reçu");
					ChatMessage chatMessage = (ChatMessage)object;
					chatWindow.addMessage(chatMessage.text);
					return;
				}

				if(object instanceof SkillNumber){
					myVue.showSkillNumber=(SkillNumber) object;
					return;
				}
				if(object instanceof ArrayList<?>){
					System.out.println("[client]: arraylist reçu");
					game.playersConnected = (ArrayList<Personnage>) object;
				}
				if(object instanceof ConstantOrder){
					System.out.println("[client]: ordre reçu");
					int ordre=((ConstantOrder)object).order;
					switch (ordre) {
					case ConstantOrder.STARTGAME:
						Gdx.app.postRunnable(new Runnable() {
							public void run() {
								game.changeScreen(MyGame.BEGINSCREEN);
							}
						});
						break;

					default:
						break;
						}
					}
				}

				public void disconnected (Connection connection) {
					EventQueue.invokeLater(new Runnable() {
						public void run () {

						}
					});
				}
			});


		chatWindow = new ChatWindow(adresse);
		chatWindow.setSendListener(new Runnable() {
			public void run () {
				ChatMessage chatMessage = new ChatMessage();
				chatMessage.text = chatWindow.getSendText();
				client.sendTCP(chatMessage);
			}
		});
		//		}

		// We'll do the connect on a new thread so the ChatFrame can show a progress bar.
		// Connecting to localhost is usually so fast you won't see the progress bar.
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, adresse, Network.portTCP,Network.portUDP);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					//System.exit(1);
				}
			}
		}.start();



		}



	}
