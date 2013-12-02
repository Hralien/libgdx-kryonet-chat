
package chat;

import gameMechanic.Personnage;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import chat.Network.ChatMessage;
import chat.Network.RegisterName;
import chat.Network.UpdateNames;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class ChatClient {
	//	ChatFrame chatFrame;
	Client client;
	String name;
	public ChatWindow chatWindow;
	private Personnage personnage;
	public final static boolean DEBUG_PC=false;

	public ChatClient (String adresse, String pseudo, Personnage perso) {
		this.client = new Client();
		this.client.start();
		this.personnage=perso;
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		this.client.addListener(new Listener() {
			public void connected (Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
			}

			public void received (Connection connection, Object object) {
				if (object instanceof UpdateNames) {
					UpdateNames updateNames = (UpdateNames)object;
					//					if(DEBUG_PC)
					//						chatFrame.setNames(updateNames.names);
					//					else
					chatWindow.setNames(updateNames.names);
					return;
				}

				if (object instanceof ChatMessage) {
					ChatMessage chatMessage = (ChatMessage)object;
					//					if(DEBUG_PC)
					//						chatFrame.addMessage(chatMessage.text);
					//					else
					chatWindow.addMessage(chatMessage.text);
					return;
				}
			}

			public void disconnected (Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run () {
						// Closing the frame calls the close listener which will stop the client's update thread.
						//						if(DEBUG_PC)
						//							chatFrame.dispose();
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
