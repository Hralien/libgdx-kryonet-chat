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

import com.badlogic.gdx.Gdx;

import m4ges.controllers.MyGame;
import m4ges.models.MapPerso;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Necromancien;
import m4ges.models.classes.Pyromancien;
import m4ges.models.classes.Shaman;
import m4ges.util.Constants;
import m4ges.views.ChatWindow;

/**
 * Classe permettant l'envoye de donnees et de se connecter aux autres joueurs
 */
public class MulticastClient {

	// Socket mutlicast
	private MulticastSocket ms;
	// addresse
	private InetSocketAddress msIp;
	// port attribue par defaut pour le multicast
	public final static int PORTMS = 12345;
	// liste des joueurs
	private MapPerso<String, Personnage> joueurs;
	// liste de monstre
	private ArrayList<Personnage> monstres;
	// Le jeu
	MyGame game;
	// Datagram
	private DatagramPacket dp;
	// ip d'un joueur (celui a qui on va renvoye son perso quand il se co
	private String ip;
	// mon ip
	public String monIp;
	// le chat window
	public ChatWindow chatWindow;

	public MulticastClient(MyGame g) {
		// initialisation
		this.game = g;
		joueurs = new MapPerso<String, Personnage>();
		monstres = new ArrayList<Personnage>();
		try {
			monIp = Inet4Address.getLocalHost().getHostAddress();
			joueurs.put(monIp, game.player);
			game.playersConnected.add(game.player);
			ms = new MulticastSocket(PORTMS);
			ms.setTimeToLive(1);
			msIp = new InetSocketAddress("228.5.6.7", PORTMS);
			join();
			// connexion + reception(thread) + envoie qu'on est la
			// DEBUG
			System.out.println("[MulticastClient]:ok co + receive");
			sendData(Constants.CONNEXION);
			System.out.println("ok send");
		} catch (IOException e) {
			System.err
					.println("[MulticastClient]:Probleme lors de la jointure au ms/ds ou de la "
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

	/**
	 * Permet de recevoir les donnees
	 */
	private void receive() {
		// Permet de recevoir les donnes
		Thread tMS = new Thread() {
			@Override
			public void run() {
			
				while (true) {
					// tableau de 1024octet au pif !
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
		// On recupere l'action de la data
		int action = (int) data[0];
		System.out.println("[MulticastClient-TraiterData]:Donnees recu  : "
				+ action);
		switch (action) {
		case Constants.CONNEXION:
		case Constants.NOUVEAU:
			actionTraiterNouveau(action, data);
			break;
		case Constants.LANCERSKILL:
			actionTraiterLancerSkill(data);
			break;
		case Constants.ATTAQUEMONSTRE:
			actionTraiterAttaqueMonstre(data);
			break;
		case Constants.TOKEN:
			actionToken(data);
			break;
		case Constants.MESSAGE:
			actionRecoit(data);
			break;
		default:
			System.err.println("[MulticastClient-DEFAULT]:Action non reconnue");
			break;
		}
	}

	/**
	 * Methode pour les messages
	 */
	public void actionRecoit(byte[] data) {
		System.out.println(data.length);
		String pseudoMsg = new String(data, 2, data[1]);
		String msg = new String(data, 2 + data[1], data.length - data[1] - 2);
		this.chatWindow.addMessage(pseudoMsg + " : " + msg);
	}

	/**
	 * methode appele en cas de creation de nouveau
	 * 
	 * @param action
	 * @param data
	 * @throws IOException
	 */
	public void actionTraiterNouveau(int action, byte[] data)
			throws IOException {
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
		// On récup l'ip (trim sert à enlever les char null
		ip = new String(data, data[2] + 3, data.length - data[2] - 3).trim();
		// Si l'ip est valide et qu'il n'est pas dans la map
		if (ip.length() > 0 && !joueurs.containsKey(ip)){
			game.playersConnected.add(p);
			joueurs.put(ip, p);
		}
		// si c'est une connexion, il faut donc renvoye une action 2 !
		if (action == Constants.CONNEXION)
			sendData(Constants.NOUVEAU);
		// DEBUG
		System.out.println("[Multicast]\n-- Affichage de(s) " + joueurs.size()
				+ " joueur(s) --");
		Set<String> key = joueurs.keySet();
		for (String it : key) {
			System.out.println("ip : " + it + " Pseudo : " + joueurs.get(it).getName());
		}
		if(joueurs.size() > 2){
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					game.changeScreen(MyGame.BATTLESCREEN);
				}
			});
		}
	}

	/**
	 * methode appele en cas de lancement de sorts
	 * 
	 * @param data
	 */
	public void actionTraiterLancerSkill(byte[] data) {
		Skill s = Skill.selectSkillFromSkillNumber(data[1]);
		// l'ip commence a 3 et la taille est de : Taille data - l'id du
		// monstre - action - id skill
		ip = new String(data, 3, data.length - 3).trim();
		// DEBUG
		System.out.println("[Multicast - LANCERSKILL]:Lancer skill : "
				+ s.getSkillName() + " ip : " + ip);
		/*
		 * On recupere la cible et l'attaquant Personnage cible =
		 * monstres.get(data[data.length-1]); Personnage attaquant =
		 * joueurs.get(ip);
		 */
		joueurs.get(ip).attaque(monstres.get(data[2]), s);
		// DEBUG
		System.out.println("[Multicast - LANCERSKILL]\n"
				+ joueurs.get(ip).getName() + " Attaque : "
				+ monstres.get(data[2]).getName() + " avec : "
				+ s.getSkillName());
	}

	/**
	 * action appele en cas d'attaque de monstre
	 * 
	 * @param data
	 */
	public void actionTraiterAttaqueMonstre(byte[] data) {
		// l'id du monstre
		int idMonstre = data[1];
		// DEBUG
		System.out
				.println("[Multicast - ATTAQUEMONSTRE]:monstre qui attaque : "
						+ monstres.get(idMonstre).getName());
		// l'ip de la cible
		ip = new String(data, 2, data.length - 2).trim();
		/*
		 * On a l'id du monstre a attaque et l'ip de la cible, on lance
		 * l'attaque
		 */
		monstres.get(idMonstre).attaque(joueurs.get(ip));
		// DEBUG
		System.out.println(monstres.get(idMonstre).getName() + " attaque "
				+ joueurs.get(ip).getName());
	}

	/**
	 * action appele en cas de passage du token
	 * 
	 * @param data
	 */
	public void actionToken(byte[] data) {
		// avant tout il faut l'enlever à celui qui l'a
		Set<String> key = joueurs.keySet();
		for (String it : key) {
			joueurs.get(it).setToken(false);
		}
		// on recupere l'ip de celui qui doit l'avoir
		ip = new String(data, 1, data.length - 1).trim();
		// et on lui met
		joueurs.get(ip).setToken(true);
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
		byte[] datatmp;
		// On switch sur l'action
		switch (action) {
		// Pour la connexion
		case Constants.CONNEXION:
		case Constants.NOUVEAU:
			datatmp = this.game.player.getBytes();
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
			if (action == Constants.NOUVEAU)
				data[0] = Constants.NOUVEAU;
			// System.err.println(new String(data));
			dp = new DatagramPacket(data, data.length, msIp);
			ms.send(dp);
			break;
		// Si quelqu'un vient de se co (Donc on a recu une requete d'action 1 on
		// envoie ca

		default:
			break;
		}
	}

	/**
	 * Permet d'envoyer un sort
	 * 
	 * @param mechant
	 *            : Le monstre a attaquer
	 * @param s
	 *            : Le skill qu'on lui lance
	 * @throws IOException
	 */
	public void lancerSort(Personnage mechant, Skill s) throws IOException {
		byte[] data = new byte[3 + monIp.length()];
		// action + skill's id + monstre
		data[0] = Constants.LANCERSKILL;
		data[1] = (byte) s.getId();
		data[2] = (byte) monstres.indexOf(mechant);
		// mon ip
		System.out.println(data.length);
		for (int i = 3; i < data.length; i++)
			data[i] = (byte) monIp.charAt(i - 3);

		dp = new DatagramPacket(data, data.length, msIp);
		ms.send(dp);
	}

	/**
	 * Permet d'envoyer un message
	 * 
	 * @param m
	 *            : le message
	 * @throws IOException
	 */
	public void envoieMessage(String m) throws IOException {
		// on prend le pseudo du type
		String pseudo = game.player.getName();
		byte[] data = new byte[2 + pseudo.length() + m.length()];
		data[0] = Constants.MESSAGE;
		// On joind la taille du pseudo pour le traitement
		data[1] = (byte) pseudo.length();
		for (int i = 2; i < pseudo.length() + 2; i++)
			data[i] = (byte) pseudo.charAt(i - 2);

		for (int i = 2 + pseudo.length(); i < data.length; i++)
			data[i] = (byte) m.charAt(i - 2 - pseudo.length());
		// et on envoie
		dp = new DatagramPacket(data, data.length, msIp);
		ms.send(dp);

	}

	/**
	 * Permet d'avertir les autres joueurs qu'un monstre a lance un sort a un
	 * joueur
	 * 
	 * @param mechant
	 *            : Le monstre qui attaque
	 * @param cible
	 *            : Le joueur attaque(ey)
	 * @throws IOException
	 */
	public void npcAttaque(Personnage mechant, Personnage cible)
			throws IOException {
		ip = joueurs.getKey(cible);
		// DEBUG
		if (ip == null)
			System.err.println("Erreur joueur inexistant");
		int idMonstre = monstres.indexOf(mechant);
		// Action, cible, sort, ip
		byte[] data = new byte[1 + 1 + ip.length()];
		data[0] = Constants.ATTAQUEMONSTRE;
		data[1] = (byte) idMonstre;
		for (int i = 2; i < data.length; i++)
			data[i] = (byte) ip.charAt(i - 2);
		dp = new DatagramPacket(data, data.length, msIp);
		ms.send(dp);
	}

	/**
	 * Permet de passer le token a un joueur
	 * 
	 * @param p
	 *            : le personnage qui aura le token
	 * @throws IOException
	 */
	public void passerToken(Personnage p) throws IOException {
		// on recupere l'ip du joueur
		ip = joueurs.getKey(p);
		byte[] data = new byte[ip.length() + 1];
		data[0] = Constants.TOKEN;
		for (int i = 1; i < data.length; i++) {
			data[i] = (byte) ip.charAt(i - 1);
		}
		// et on envoie ca
		dp = new DatagramPacket(data, data.length, msIp);
		ms.send(dp);
	}

	public InetSocketAddress getMsIp() {
		return msIp;
	}

	public void setMsIp(InetSocketAddress msIp) {
		this.msIp = msIp;
	}

	public MapPerso<String, Personnage> getJoueurs() {
		return joueurs;
	}

	public void setJoueurs(MapPerso<String, Personnage> joueurs) {
		this.joueurs = joueurs;
	}

	public ArrayList<Personnage> getMonstres() {
		return monstres;
	}

	public void setMonstres(ArrayList<Personnage> monstres) {
		this.monstres = monstres;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
