
package chat;

import gameMechanic.Personnage;
import gameMechanic.Shaman;

import java.awt.EventQueue;
import java.io.IOException;
import chat.Network.ChatMessage;
import chat.Network.ConstantOrder;
import chat.Network.PersonnageConnection;
import chat.Network.SkillNumber;
import chat.Network.TestConnection;
import chat.Network.UpdateNames;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.me.mygdxgame.ChatScreen;
import com.me.mygdxgame.MyGame;


public class ChatClient {
	public Client client;
	String name;
	public ChatWindow chatWindow;
	private Personnage personnage;
	private ChatScreen myVue;
	private MyGame game;

	public ChatClient (String adresse, String pseudo, Personnage perso, ChatScreen vue, MyGame myGame) {
		this.client = new Client();
		this.client.start();
		this.personnage=perso;
		this.myVue = vue;
		this.game = myGame;
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);
		
		Kryo kryo = client.getKryo();
	    kryo.register(Shaman.class);
	    
		this.client.addListener(new Listener() {

			@Override
			public void connected (Connection connection) {
				//				RegisterName registerName = new RegisterName();
				//				registerName.name = name;
				//				client.sendTCP(registerName);
				PersonnageConnection pc = new PersonnageConnection();
				pc.name = personnage.getName();
				client.sendTCP(pc);

			}

			@Override
			public void received (Connection connection, Object object) {
				if (object instanceof UpdateNames) {
					UpdateNames updateNames = (UpdateNames)object;
					chatWindow.setNames(updateNames.names);
					return;
				}

				if (object instanceof ChatMessage) {
					ChatMessage chatMessage = (ChatMessage)object;
					chatWindow.addMessage(chatMessage.text);
					return;
				}

				if(object instanceof SkillNumber){
					myVue.showSkillNumber=(SkillNumber) object;
					return;
				}

				if(object instanceof ConstantOrder){
					int ordre=((ConstantOrder)object).order;
					switch (ordre) {
					case ConstantOrder.STARTGAME:
						game.setScreen(game.beginingScreen);
						break;

					default:
						break;
					}
				}

//				if (object instanceof TestConnection) {
//					game.listHost.add(((TestConnection) object).test);
//					System.err.println("res"+((TestConnection) object).test);
//				}
			}

			public void disconnected (Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run () {

					}
				});
			}
		});
		if(personnage!=null)
			name=personnage.getName();
		else name=pseudo;

		final String host = adresse;

		chatWindow = new ChatWindow(host);
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
					client.connect(5000, host, Network.portTCP,Network.portUDP);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					//System.exit(1);
				}
			}
		}.start();

		

	}



}
