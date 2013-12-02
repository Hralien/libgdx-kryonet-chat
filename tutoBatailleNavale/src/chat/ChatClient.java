
package chat;

import gameMechanic.Personnage;
import gameMechanic.Skill;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import chat.Network.ChatMessage;
import chat.Network.PersonnageConnection;
import chat.Network.RegisterName;
import chat.Network.SkillNumber;
import chat.Network.UpdateNames;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.me.mygdxgame.ChatScreen;


public class ChatClient {
	//	ChatFrame chatFrame;
	Client client;
	String name;
	public ChatWindow chatWindow;
	private Personnage personnage;
	private ChatScreen myVue;
	
	public ChatClient (String adresse, String pseudo, Personnage perso, ChatScreen vue) {
		this.client = new Client();
		this.client.start();
		this.personnage=perso;
		this.myVue = vue;
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		this.client.addListener(new Listener() {
			
			@Override
			public void connected (Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
				PersonnageConnection pc = new PersonnageConnection();
				pc.name = personnage.name;
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
					System.err.println("chatMessage");
					return;
				}
				
				if(object instanceof SkillNumber){
					System.err.println("skillNumber");
					myVue.showSkillNumber=(SkillNumber) object;
					return;
				}
			}

			public void disconnected (Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run () {

					}
				});
			}
		});
		if(personnage!=null)
			name=personnage.name;
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

		//		List<InetAddress> address = client.discoverHosts(Network.portUDP, 5000);
		//		System.out.println("test"+address);

	}

}
