package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
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

//TODO
//DatagramSocket, boucle infini lors de l'inscription, affectation id etc. 
public class MulticastClient {

	// Socket mutlicast
	MulticastSocket ms;
	// Socket pour les donnees pour une cible
	DatagramSocket ds;
	// addresse
	InetSocketAddress msIp;
	// port attribue par defaut pour le multicast
	public final static int PORTMS = 12345;
	// port attribue par defaut pour unicast
	public final static int PORTUS = 12346;
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
	//ip d'un joueur (celui a qui on va renvoye son perso quand il se co
	String ip;
	//mon ip
	String monIp;
	
	public MulticastClient(MyGame g) {
		// initialisation
		this.game = g;
		joueurs = new ArrayList<Personnage>();
		joueurs.add(game.player);
		monstres = new ArrayList<Personnage>();
		try {
			monIp = Inet4Address.getLocalHost().getHostAddress();
			ds = new DatagramSocket(PORTUS);
			ms = new MulticastSocket(PORTMS);
			msIp = new InetSocketAddress("228.5.6.7", PORTMS);
			join();
			// connexion + reception(thread) + envoie qu'on est la
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
						System.out.println("recu qqch");
						data = dp.getData();
						traiterData(data);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread tUS = new Thread() {
			@Override
			public void run() {
				while (true) {
					// tableau de 1024octet
					byte[] data = new byte[1024];
					dp = new DatagramPacket(data, data.length);
					try {
						ds.receive(dp);
						data = dp.getData();
						traiterData(data);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		};
		tUS.start();
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
		switch (action) {
		case Constants.CONNEXION:
		case Constants.NOUVEAU:
			// System.out.println("Nouveau joueur");
			// System.out.println(data[1]);
			String pseudo;
			pseudo = new String(data, 3, data[2]);
			System.out.println(pseudo);
			switch (data[1]) {
			case Personnage.AQUAMANCIEN:
				Aquamancien a = new Aquamancien();
				a.setName(pseudo);
				joueurs.add(a);
				break;
			case Personnage.NECROMANCIEN:
				Necromancien n = new Necromancien();
				n.setName(pseudo);
				joueurs.add(n);
				break;
			case Personnage.SHAMAN:
				Shaman s = new Shaman();
				s.setName(pseudo);
				joueurs.add(s);
				break;
			case Personnage.PYROMANCIEN:
				Pyromancien p = new Pyromancien();
				p.setName(pseudo);
				joueurs.add(p);
				break;
			}
			// System.out.println("Nouveau joueur de classe : " +
			// joueurs.get(joueurs.size()-1)
			// + " et de pseudo : " + joueurs.get(joueurs.size()-1).getName());
//			System.out.println(joueurs.size());
			ip = new String(data, data[2]+3, data.length-data[2]-3);
			System.out.println(ip);
			// si c'est une connexion, il faut donc renvoye une action 2 !
			if (action == Constants.CONNEXION) {
				sendData(Constants.NOUVEAU);
			}
			break;

		default:
			System.err.println("Action non reconnue");
			break;
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
			byte [] datatmp = this.game.player.getBytes();
			//Il faut joindre l'ip 
			
			// La taille total de data
			int j = datatmp.length + monIp.length();
			data = new byte[j];
			//Il faut initialiser data avec les infos qu on a deja
			for(int i = 0; i < datatmp.length; i++){
				data[i] = datatmp[i];
			}
			//Maintenant on met l'ip
			for(int i = datatmp.length; i < j; i++){
				data[i] = (byte)monIp.charAt(i-datatmp.length);
			}
			System.err.println(new String(data));
			dp = new DatagramPacket(data, data.length, msIp);
			ms.send(dp);
			break;
		// Si quelqu'un vient de se co (Donc on a recu une requete d'action 1 on
		// envoie ca :
		case Constants.NOUVEAU:
			//On recupere l'ip
			data = this.game.player.getBytes();
			data[0] = 2;
			dp = new DatagramPacket(data, data.length);
			dp.setAddress(InetAddress.getByName(ip));
			// destine a une personne : DatagramSocket !
			dp.setPort(PORTMS);
			ds.send(dp);
			break;

		default:
			break;
		}
	}
}
