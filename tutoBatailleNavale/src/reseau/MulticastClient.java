package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;

import m4ges.controllers.MyGame;
import m4ges.models.Personnage;
import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Necromancien;
import m4ges.models.classes.Pyromancien;
import m4ges.models.classes.Shaman;
import m4ges.util.Constants;

public class MulticastClient {

	// Socket
	MulticastSocket ms;
	// addresse
	InetSocketAddress msIp;
	// port
	public final static int PORT = 12345;
	// liste des joueurs
	ArrayList<Personnage> joueurs;
	// Permet de connaitre l'ordre du jeu
	boolean tocken;
	// liste de monstre
	ArrayList<Personnage> monstres;
	// Le jeu
	MyGame game;
	// Datagram
	DatagramPacket dp;

	public MulticastClient(MyGame g) {
		this.game = g;
		joueurs = new ArrayList<Personnage>();
		joueurs.add(game.player);
		monstres = new ArrayList<Personnage>();
		try {
			join();
			System.out.println("ok co");
			sendData(Constants.CONNEXION);
			System.out.println("ok send");
		} catch (IOException e) {
			System.err.println("Probleme lors de la jointure au ms ou de la "
					+ "transmission du pseudo");
		}
	}

	/**
	 * Permet d'envoyer les donnees
	 * 
	 * @param action
	 *            l'action a effectuer
	 * @throws IOException
	 */
	private void sendData(int action) throws IOException {
		byte[] data;
		// On switch sur l'action
		switch (action) {
		// Pour la connexion
		case Constants.CONNEXION:
			data = this.game.player.getBytes();
			dp = new DatagramPacket(data, data.length, msIp);
			System.out.println(ms.getInetAddress());
			ms.send(dp);
			break;

		default:
			break;
		}
	}

	/**
	 * Permet de rejoindre un groupe
	 * 
	 * @throws IOException
	 */
	private void join() throws IOException {
		ms = new MulticastSocket(PORT);
		msIp = new InetSocketAddress("228.5.6.7", PORT);
		// On recupere notre addresse
		NetworkInterface netif = NetworkInterface.getByInetAddress(InetAddress
				.getLocalHost());
		// Et on rejoind le groupe
		ms.joinGroup(msIp, netif);
		receive();
	}

	private void receive() {
		// Permet de recevoir les donnes
		Thread t = new Thread() {
			@Override
			public void run() {
				while (true) {
					// tableau de 1024octet
					byte[] data = new byte[1024];
					dp = new DatagramPacket(data, data.length);
					try {
						// recepetion
						ms.receive(dp);
						data = dp.getData();
						traiterData(data);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

	/**
	 * Traite les donnees
	 * 
	 * @param data
	 *            donnees a traiter
	 */
	private void traiterData(byte[] data) {
		int action = (int) data[0];
		switch (action) {
		case Constants.CONNEXION:
			// System.out.println("Nouveau joueur");
			// System.out.println(data[1]);
			String pseudo;
			pseudo = new String(data, 2, data.length);
			switch (data[1]) {
			case Personnage.AQUAMANCIEN:
				Aquamancien a = new Aquamancien();
				a.setName(pseudo);
				joueurs.add(a);
			case Personnage.NECROMANCIEN:
				Necromancien n = new Necromancien();
				n.setName(pseudo);
				joueurs.add(n);
			case Personnage.SHAMAN:
				Shaman s = new Shaman();
				s.setName(pseudo);
				joueurs.add(s);
			case Personnage.PYROMANCIEN:
				Pyromancien p = new Pyromancien();
				p.setName(pseudo);
				joueurs.add(p);
			}
			System.out.println("Nouveau joueur de classe : " + joueurs.get(joueurs.size()) 
					+ " et de pseudo : " + joueurs.get(joueurs.size()).getName());
			
			break;
		default:
			System.err.println("Action non reconnue");
			break;
		}
	}
}
