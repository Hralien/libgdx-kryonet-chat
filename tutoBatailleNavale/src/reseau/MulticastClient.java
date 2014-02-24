package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Set;

import m4ges.controllers.MyGame;
import m4ges.models.MapPerso;
import m4ges.models.Personnage;
import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Necromancien;
import m4ges.models.classes.Pyromancien;
import m4ges.models.classes.Shaman;
import m4ges.util.Constants;

//TODO token, sort 
public class MulticastClient {

	// Socket mutlicast
	MulticastSocket ms;
	// addresse
	InetSocketAddress msIp;
	// port attribue par defaut pour le multicast
	public final static int PORTMS = 12345;
	// liste des joueurs
	MapPerso<String, Personnage> joueurs;
	// Permet de connaitre l'ordre du jeu
	boolean token;
	// liste de monstre
	ArrayList<Personnage> monstres;
	// Le jeu
	MyGame game;
	// Datagram
	DatagramPacket dp;
	// ip d'un joueur (celui a qui on va renvoye son perso quand il se co
	String ip;
	// mon ip
	String monIp;

	public MulticastClient(MyGame g) {
		// initialisation
		this.game = g;
		joueurs = new MapPerso<String, Personnage>();
		monstres = new ArrayList<Personnage>();
		try {
			monIp = Inet4Address.getLocalHost().getHostAddress();
			joueurs.put(monIp, game.player);
			ms = new MulticastSocket(PORTMS);
			msIp = new InetSocketAddress("228.5.6.7", PORTMS);
			join();
			// connexion + reception(thread) + envoie qu'on est la
			//DEBUG
			System.out.println("ok co + receive");
			sendData(Constants.CONNEXION);
			System.out.println("ok send");
		} catch (IOException e) {
			System.err
					.println("Probleme lors de la jointure au ms/ds ou de la "
							+ "transmission du perso. Port possible occupee");
			e.printStackTrace();
		}
	}

	/**
	 * Permet de rejoindre un groupe
	 * 
	 * @throws IOException
	 */
	private void join() throws IOException {

		// On recupere notre addresse
		NetworkInterface netif = NetworkInterface.getByInetAddress(InetAddress
				.getLocalHost());
		// Et on rejoind le groupe
		ms.joinGroup(msIp, netif);
		receive();
	}

	private void receive() {
		// Permet de recevoir les donnes
		Thread tMS = new Thread() {
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

		tMS.start();
	}

	/**
	 * Traite les donnees
	 * 
	 * @param data
	 *            donnees a traiter
	 * @throws IOException
	 */
	private void traiterData(byte[] data) throws IOException {
		int action = (int) data[0];
		System.out.println("Donnees recu  : " + action);
		switch (action) {
		case Constants.CONNEXION:
		case Constants.NOUVEAU:
			String pseudo;
			pseudo = new String(data, 3, data[2]);
			Personnage p = null;
			switch (data[1]) {
			case Personnage.AQUAMANCIEN:
				p = new Aquamancien();
				p.setName(pseudo);
				break;
			case Personnage.NECROMANCIEN:
				p = new Necromancien();
				p.setName(pseudo);
				break;
			case Personnage.SHAMAN:
				p = new Shaman();
				p.setName(pseudo);
				break;
			case Personnage.PYROMANCIEN:
				p = new Pyromancien();
				p.setName(pseudo);
				break;
			}
			//On récup l'ip (trim sert à enlever les char null
			ip = new String(data, data[2] + 3, data.length - data[2] - 3).trim();
			//Si l'ip est valide et qu'il n'est pas dans la map
			if (ip.length() > 0 && !joueurs.containsKey(ip))
				joueurs.put(ip, p);
			// si c'est une connexion, il faut donc renvoye une action 2 !
			if (action == Constants.CONNEXION)
				sendData(Constants.NOUVEAU);
			
			break;
		default:
			System.err.println("Action non reconnue");
			break;
		}
		// DEBUG
		System.out.println("-- Affichage de(s) " + joueurs.size() + " joueurs --");
		Set<String> key = joueurs.keySet();
		for (String it : key) {
			System.out.println("ip : " + it + " Pseudo : " + joueurs.get(it));
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
			System.out.println("Envoie de la connection");
			byte[] datatmp = this.game.player.getBytes();
			// Il faut joindre l'ip

			// La taille total de data
			int j = datatmp.length + monIp.length();
			data = new byte[j];
			// Il faut initialiser data avec les infos qu on a deja
			for (int i = 0; i < datatmp.length; i++) {
				data[i] = datatmp[i];
			}
			// Maintenant on met l'ip
			for (int i = datatmp.length; i < j; i++) {
				data[i] = (byte) monIp.charAt(i - datatmp.length);
			}
			// System.err.println(new String(data));
			dp = new DatagramPacket(data, data.length, msIp);
			ms.send(dp);
			break;
		// Si quelqu'un vient de se co (Donc on a recu une requete d'action 1 on
		// envoie ca :
		case Constants.NOUVEAU:
			System.out.println("Connexion recu, reponse");
			// On recupere l'ip
			data = this.game.player.getBytes();
			data[0] = 2;
			dp = new DatagramPacket(data, data.length, msIp);
			dp.setAddress(InetAddress.getByName(ip));
			ms.send(dp);
			break;

		default:
			break;
		}
	}
}
