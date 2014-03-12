package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Set;

import m4ges.controllers.MyGame;
import m4ges.models.MapPerso;
import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Joueur;
import m4ges.models.classes.Necromancien;
import m4ges.models.classes.Pyromancien;
import m4ges.models.classes.Shaman;
import m4ges.models.monster.Monstre;
import m4ges.util.Constants;
import m4ges.views.ChatWindow;

import com.badlogic.gdx.Gdx;

/**
 * Classe permettant l'envoye de donnees et de se connecter aux autres joueurs
 */
public class UnicastClient {

	/**
	 * Datagram socket permettant l'envoie
	 */
	private DatagramSocket ds;
	/**
	 * Datagram socket permettant la recepetion
	 */
	private DatagramSocket dsR;
	/**
	 * Port sur lequel les joueurs les vont communiquer
	 */
	public final static int PORT = 12345;
	/**
	 * Hashmap des joueurs de la forme <IP, JOUEURS>
	 */
	private MapPerso<String, Joueur> joueurs;
	/**
	 * Liste des monstres
	 */
	private ArrayList<Personnage> monstres;
	/**
	 * Le jeu
	 */
	public MyGame game;
	/**
	 * datagram packet permettant l'envoie
	 */
	private DatagramPacket dp;
	/**
	 * datagram packet permettant la recepetion
	 */
	private DatagramPacket dpr;
	/**
	 * ip d'un joueur
	 */
	private String ip;
	/**
	 * ip de l'user
	 */
	public String monIp;
	/**
	 * Chat window, utile pour le chat
	 */
	public ChatWindow chatWindow;
	/**
	 * Permet de passer sur le screen de battle
	 */
	public boolean estBattleScreen;
	
	/**
	 * Pseudo des joueurs
	 */
	ArrayList<String> pseudoJoueur;
	public static final int NB_JOUEUR_MINIMUM = 10;

	/**
	 * Constructeur
	 * 
	 * @param g
	 *            : Le jeu
	 */
	public UnicastClient(MyGame g) {
		// initialisation
		this.game = g;
		joueurs = new MapPerso<String, Joueur>();
		monstres = new ArrayList<Personnage>();
		pseudoJoueur = new ArrayList<String>();
		estBattleScreen = false;

		try {

			monIp = Inet4Address.getLocalHost().getHostAddress();
			joueurs.put(monIp, game.player);
			game.playersConnected.add(game.player);
			pseudoJoueur.add(game.player.getName());
			dsR = new DatagramSocket(PORT);
			ds = new DatagramSocket();
			ds.setBroadcast(true);
			receive();
			sendConnection(null, false);

		} catch (IOException e) {
			System.err
					.println("[MulticastClient]:Probleme lors de la jointure au ms/ds ou de la "
							+ "transmission du perso. Port possible occupee");
			e.printStackTrace();
		}
	}

	/**
	 * Permet de recevoir les donnees
	 */
	private void receive() {
		// Permet de recevoir les donnes
		game.androidUI.showAlertBox("title", "receiveOk", "ok", null);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// tableau de 1024octet au pif !
					byte[] data = new byte[1024];
					dpr = new DatagramPacket(data, data.length);
					// game.androidUI.showAlertBox("title", "data:" +
					// data.length,
					// "ok", null);
					try {
						// recepetion
						dsR.receive(dpr);
						System.out.println("RECU");
						// game.androidUI.showAlertBox("title", "data receive",
						// "ok", null);
						data = dpr.getData();
						traiterData(data);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
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
			System.err
					.println("[MulticastClient-DEFAULT]:Action non reconnue : "
							+ action);
			break;
		}
	}

	/**
	 * Methode pour les messages
	 */
	private void actionRecoit(byte[] data) {
		// System.out.println(data.length);
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
	private void actionTraiterNouveau(int action, byte[] data)
			throws IOException {
		System.out.println("NOUVEAU JOUEUR");
		String pseudo;
		pseudo = new String(data, 3, data[2]);
		Joueur p = null;
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
		ip = dpr.getAddress().toString().replace('/', '\0').trim();
		// Si l'ip est valide et qu'il n'est pas dans la map
		if (ip.length() > 0 && !joueurs.containsKey(ip) && !ip.equals("127.0.0.1")) {
			game.playersConnected.add(p);
			joueurs.put(ip, p);
			pseudoJoueur.add(p.getName());
			this.chatWindow.addName(p.getName());
		}
		// si c'est une connexion, il faut donc renvoye une action 2 !
		if (action == Constants.CONNEXION)
			sendConnection(ip, true);
		// DEBUG
		System.out.println("[Multicast]\n-- Affichage de(s) " + joueurs.size()
				+ " joueur(s) --");
		Set<String> key = joueurs.keySet();
		for (String it : key) {
			System.out.println("ip : " + it + " Pseudo : "
					+ joueurs.get(it).getName());
		}
		if (joueurs.size() >= NB_JOUEUR_MINIMUM && !estBattleScreen) {
			estBattleScreen = true;
			Gdx.app.postRunnable(new Runnable() {
				public void run() {
					game.changeScreen(MyGame.BATTLESCREEN);

				}
			});
		}
	}

	/**
	 * Permet d'envoyer un rapport de connection aux autres
	 * 
	 * @param ipNouveau
	 *            : lors de la co null mais permet de dire aux nouveaux connecte
	 *            qu'on est la
	 * @param nouveau
	 *            : lors de la co false mais permet de dire aux nouveaux
	 *            connecte qu'on est la
	 * @throws IOException
	 */
	public void sendConnection(String ipNouveau, boolean nouveau)
			throws IOException {
		byte[] data;

		data = ((Joueur) this.game.player).getBytes();
		// Si c'est un nouveau on ne repond qu'a lui

		if (nouveau) {
			System.err.println(ipNouveau.replace('/', '\0').trim());
			data[0] = Constants.NOUVEAU;
			if (ipNouveau.replace('/', '\0').trim().equals(monIp)
					|| ipNouveau.replace('/', '\0').trim().equals("127.0.0.1")) {

				return;
			}

			dp = new DatagramPacket(data, data.length);
			dp.setAddress(InetAddress.getByName(ipNouveau));
			dp.setPort(PORT);
			ds.send(dp);

			// Sinon on repond a tout le monde
		} else {

			data[0] = Constants.CONNEXION;
			String[] broadcastTab = this.monIp.split("\\.");
			String broadcast = broadcastTab[0]+"."+broadcastTab[1]+"."+broadcastTab[2]+".255";
			dp = new DatagramPacket(data, data.length,
					InetAddress.getByName(broadcast), PORT);
			ds.send(dp);

			dp = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), PORT);
			ds.send(dp);

		}
	}

	/**
	 * methode appele en cas de lancement de sorts
	 * 
	 * @param data
	 */
	private void actionTraiterLancerSkill(byte[] data) {
		Skill s = Skill.selectSkillFromSkillNumber(data[1]);
		// l'ip commence a 3 et la taille est de : Taille data - l'id du
		// monstre - action - id skill
		ip = dpr.getAddress().toString();
		// DEBUG
		System.out.println("[Multicast - LANCERSKILL]:Lancer skill : "
				+ s.getSkillName() + " ip : " + ip);
		/*
		 * On recupere la cible et l'attaquant Personnage cible =
		 * monstres.get(data[data.length-1]); Personnage attaquant =
		 * joueurs.get(ip);
		 */
		((Joueur) joueurs.get(ip)).attaque(monstres.get(data[2]), s);
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
	private void actionTraiterAttaqueMonstre(byte[] data) {
		// l'id du monstre
		int idMonstre = data[1];
		// DEBUG
		System.out
				.println("[Multicast - ATTAQUEMONSTRE]:monstre qui attaque : "
						+ monstres.get(idMonstre).getName());
		// l'ip de la cible
		ip = dpr.getAddress().toString();
		/*
		 * On a l'id du monstre a attaque et l'ip de la cible, on lance
		 * l'attaque
		 */
		((Monstre) monstres.get(idMonstre)).attaque(joueurs.get(ip));
		// DEBUG
		System.out.println(monstres.get(idMonstre).getName() + " attaque "
				+ joueurs.get(ip).getName());
	}

	/**
	 * action appele en cas de passage du token
	 * 
	 * @param data
	 */
	private void actionToken(byte[] data) {
		// avant tout il faut l'enlever à celui qui l'a
		Set<String> key = joueurs.keySet();
		for (String it : key) {
			joueurs.get(it).setToken(false);
		}
		// on recupere l'ip de celui qui doit l'avoir
		ip = dpr.getAddress().toString();
		// et on lui met
		joueurs.get(ip).setToken(true);
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

		for (int i = 3; i < data.length; i++)
			data[i] = (byte) monIp.charAt(i - 3);

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
		sendToAll(data);
	}

	/**
	 * quand tout les joueurs sont pret on peut continuer vers la vague suivante
	 */
	public void pretPourVagueSuivante() {

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
	public void npcAttaque(Personnage mechant, Joueur cible) throws IOException {
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

	}

	/**
	 * Permet de passer le token a un joueur
	 * 
	 * @param p
	 *            : le personnage qui aura le token
	 * @throws IOException
	 */
	public void passerToken(Joueur p) throws IOException {
		// on recupere l'ip du joueur
		ip = joueurs.getKey(p);
		byte[] data = new byte[ip.length() + 1];
		data[0] = Constants.TOKEN;
		for (int i = 1; i < data.length; i++) {
			data[i] = (byte) ip.charAt(i - 1);
		}

	}

	private void sendToAll(byte[] data) throws IOException {
		dp = new DatagramPacket(data, data.length);
		for (String ips : joueurs.keySet()) {
			// if (ips.equals(monIp))
			// dp.setAddress(InetAddress.getByName("127.0.0.1"));
			// else
			dp.setAddress(InetAddress.getByName(ips));
			dp.setPort(PORT);
			ds.send(dp);
		}
	}

	public MapPerso<String, Joueur> getJoueurs() {
		return joueurs;
	}

	public void setJoueurs(MapPerso<String, Joueur> joueurs) {
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
