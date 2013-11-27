
package chat;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetAddress;

import chat.Network.ChatMessage;
import chat.Network.RegisterName;
import chat.Network.UpdateNames;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;



public class ChatClient {
	ChatFrame chatFrame;
	Client client;
	String name;
	public ChatWindow chatWindow;

	public final static boolean DEBUG_PC=false;

	public ChatClient (String adresse, String pseudo) {
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
			}

			public void received (Connection connection, Object object) {
				if (object instanceof UpdateNames) {
					UpdateNames updateNames = (UpdateNames)object;
					if(DEBUG_PC)
						chatFrame.setNames(updateNames.names);
					else
						chatWindow.setNames(updateNames.names);
					return;
				}

				if (object instanceof ChatMessage) {
					ChatMessage chatMessage = (ChatMessage)object;
					if(DEBUG_PC)
						chatFrame.addMessage(chatMessage.text);
					else
						chatWindow.addMessage(chatMessage.text);
					return;
				}
			}

			public void disconnected (Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run () {
						// Closing the frame calls the close listener which will stop the client's update thread.
						if(DEBUG_PC)
							chatFrame.dispose();
					}
				});
			}
		});

		name=pseudo;
		final String host = adresse;

		// Request the host from the user.
		//		String input = (String)JOptionPane.showInputDialog(null, "Host:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE,
		//				null, null, "localhost");
		//		if (input == null || input.trim().length() == 0) System.exit(1);
		//		final String host = input.trim();
		//
		//		// Request the user's name.
		//		input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE, null,
		//				null, "Test");
		//		if (input == null || input.trim().length() == 0) System.exit(1);
		//		name = input.trim();

		// All the ugly Swing stuff is hidden in ChatFrame so it doesn't clutter the KryoNet example code.
		if(DEBUG_PC){
			chatFrame = new ChatFrame(host);

			// This listener is called when the send button is clicked.
			chatFrame.setSendListener(new Runnable() {
				public void run () {
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = chatFrame.getSendText();
					client.sendTCP(chatMessage);
				}
			});
			// This listener is called when the chat window is closed.
			chatFrame.setCloseListener(new Runnable() {
				public void run () {
					client.stop();
				}
			});
			chatFrame.setVisible(true);
		}
		else{
			chatWindow = new ChatWindow(host);
			chatWindow.setSendListener(new Runnable() {
				public void run () {
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = chatWindow.getSendText();
					client.sendTCP(chatMessage);
				}
			});
		}

		// We'll do the connect on a new thread so the ChatFrame can show a progress bar.
		// Connecting to localhost is usually so fast you won't see the progress bar.
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, host, Network.port);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					//System.exit(1);
				}
			}
		}.start();
		
		InetAddress address = client.discoverHost(54555, 5000);
		System.out.println("test"+address);
	}

}
