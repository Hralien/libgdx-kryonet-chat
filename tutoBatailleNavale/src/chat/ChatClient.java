
package chat;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


import chat.Network.ChatMessage;
import chat.Network.RegisterName;
import chat.Network.UpdateNames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


import com.esotericsoftware.minlog.Log;

public class ChatClient {
	ChatFrame chatFrame;
	Client client;
	String name;
	public ChatWindow chatWindow;

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
					//chatFrame.setNames(updateNames.names);
					chatWindow.setNames(updateNames.names);
					return;
				}

				if (object instanceof ChatMessage) {
					ChatMessage chatMessage = (ChatMessage)object;
					//chatFrame.addMessage(chatMessage.text);
					chatWindow.addMessage(chatMessage.text);
					return;
				}
			}

			public void disconnected (Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run () {
						// Closing the frame calls the close listener which will stop the client's update thread.
						//chatFrame.dispose();
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
		
//		chatFrame = new ChatFrame(host);
		
		// This listener is called when the send button is clicked.
//		chatFrame.setSendListener(new Runnable() {
//			public void run () {
//				ChatMessage chatMessage = new ChatMessage();
//				chatMessage.text = chatFrame.getSendText();
//				client.sendTCP(chatMessage);
//			}
//		});
		// This listener is called when the chat window is closed.
//		chatFrame.setCloseListener(new Runnable() {
//			public void run () {
//				client.stop();
//			}
//		});
//		chatFrame.setVisible(true);

		chatWindow = new ChatWindow(host);
		chatWindow.setSendListener(new Runnable() {
			public void run () {
				ChatMessage chatMessage = new ChatMessage();
				chatMessage.text = chatWindow.getSendText();
				client.sendTCP(chatMessage);
			}
		});

		// We'll do the connect on a new thread so the ChatFrame can show a progress bar.
		// Connecting to localhost is usually so fast you won't see the progress bar.
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, host, Network.port);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
	}


	public static void main (String[] args) {
		Log.set(Log.LEVEL_DEBUG);
		new ChatClient("localhost", "flo");
	}
}
