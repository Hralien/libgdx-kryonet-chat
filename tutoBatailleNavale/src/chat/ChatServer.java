
package chat;

import gameMechanic.Personnage;
import gameMechanic.Skill;

import java.io.IOException;
import java.util.ArrayList;
import chat.Network.ChatMessage;
import chat.Network.ConstantOrder;
import chat.Network.PersonnageConnection;
import chat.Network.RegisterName;
import chat.Network.TestConnection;
import chat.Network.UpdateNames;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ChatServer {
	Server server;
	public final static boolean DEBUG_PC=false;
	public ArrayList<PersonnageConnection> listPersonnage;
	private int nbjoueur;

	public ChatServer (int nb) throws IOException {
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new ChatConnection();
			}
		};
		this.nbjoueur = nb;
		this.listPersonnage= new ArrayList<PersonnageConnection>();
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);
		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				// We know all connections for this server are actually ChatConnections.
				ChatConnection connection = (ChatConnection)c;
				
				if (object instanceof TestConnection) {
					System.err.println("testConnect re�u");
					server.sendToAllTCP((TestConnection)object);
					return;
				}

				if (object instanceof RegisterName) {
					// Ignore the object if a client has already registered a name. This is
					// impossible with our client, but a hacker could send messages at any time.
					if (connection.name != null) return;
					// Ignore the object if the name is invalid.
					String name = ((RegisterName)object).name;
					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					// Store the name on the connection.
					connection.name = name;
					// Send a "connected" message to everyone except the new client.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = name + " connected.";
					server.sendToAllExceptTCP(connection.getID(), chatMessage);
					// Send everyone a new list of connection names.
					updateNames();
					return;
				}
				if (object instanceof PersonnageConnection) {
					if (connection.name !=null) return;
					String name = ((PersonnageConnection)object).name;

					if (name == null) return;
					name = name.trim();
					if (name.length() == 0) return;
					// Store the name on the connection.
					connection.name = name;
					if( listPersonnage.contains(object))
						return;
					else {
						listPersonnage.add((PersonnageConnection) object);
					}
					if(listPersonnage.size()>=nbjoueur){
						ConstantOrder co = new ConstantOrder();
						co.order=ConstantOrder.STARTGAME;
						server.sendToAllTCP(co);
					}
					else{
						// Send a "connected" message to everyone except the new client.
						ChatMessage chatMessage = new ChatMessage();
						chatMessage.text = name + " connected.";
						server.sendToAllExceptTCP(connection.getID(), chatMessage);
						// Send everyone a new list of connection names.
						updateNames();
					}
					System.err.println(name);
					return;
				}
				if (object instanceof ChatMessage) {
					System.err.println("cm");
					// Ignore the object if a client tries to chat before registering a name.
					if (connection.name == null) return;
					ChatMessage chatMessage = (ChatMessage)object;
					// Ignore the object if the chat message is invalid.
					String message = chatMessage.text;
					if (message == null) return;
					message = message.trim();
					if (message.length() == 0) return;
					// Prepend the connection's name and send to everyone.
					chatMessage.text = connection.name + ": " + message;
					server.sendToAllTCP(chatMessage);
					return;
				}

				
			}

			public void disconnected (Connection c) {
				ChatConnection connection = (ChatConnection)c;
				if (connection.name != null) {
					// Announce to everyone that someone (with a registered name) has left.
					ChatMessage chatMessage = new ChatMessage();
					chatMessage.text = connection.name + " disconnected.";
					server.sendToAllTCP(chatMessage);
					updateNames();
				}
			}
		});
		server.bind(Network.portTCP,Network.portUDP);
		server.start();
		//		Client client = new Client();
		//		List<InetAddress> address = client.discoverHosts(Network.portUDP, 5000);
		//		System.out.println("test"+address);

	}

	void updateNames () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList<String> names = new ArrayList<String>(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			ChatConnection connection = (ChatConnection)connections[i];
			names.add(connection.name);
		}
		// Send the names to everyone.
		UpdateNames updateNames = new UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAllTCP(updateNames);
	}

	void afficheSkill(Skill s){
		server.sendToAllTCP(s);
	}

	// This holds per connection state.
	static class ChatConnection extends Connection {
		public String name;
	}

}
