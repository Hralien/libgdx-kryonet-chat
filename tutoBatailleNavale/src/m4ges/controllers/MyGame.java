package m4ges.controllers;

import java.util.ArrayList;
import java.util.Hashtable;

import reseau.GameClient;
import reseau.GameServer;
import m4ges.models.Personnage;
import m4ges.views.BattleScreen;
import m4ges.views.ChatScreen;
import m4ges.views.CreateCharacterScreen;
import m4ges.views.EncyclopedieScreen;
import m4ges.views.LoadingScreen;
import m4ges.views.MenuPrincipalScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

/**
 * Ici on initialise tout les screen
 * Depart du jeu appelle dans les main
 * @author Florian
 *
 */
public class MyGame extends Game{


	private Hashtable<Integer,Screen> screenHashtable;

	public final static int LOADINGSCREEN = 0;
	public final static int MENUSCREEN = 1;
	public final static int NEWCHARACTERSCREEN = 2;
	public final static int CHATSCREEN = 3;
	public final static int BATTLESCREEN = 4;
	public final static int DICOSCREEN = 5;

	/**
	 * le personnage du joueur initialise lors de la creation
	 */
	public Personnage player;
	public GameClient chatClient;

	public UITrick androidUI;
	public GameServer chatServer;
	public ArrayList<String> listHost ;
	public ArrayList<Personnage> playersConnected;

	public static AssetManager manager;

	public MyGame(UITrick actionResolver) {
		super();
		this.androidUI = actionResolver;
		listHost = new ArrayList<String>();
		playersConnected = new ArrayList<Personnage>();
		manager = new AssetManager();

	}


	@Override
	public void create() {
		screenHashtable = new Hashtable<Integer, Screen>();
		screenHashtable.put(LOADINGSCREEN, new LoadingScreen(this));
		screenHashtable.put(MENUSCREEN, new MenuPrincipalScreen(this));
		screenHashtable.put(NEWCHARACTERSCREEN,new CreateCharacterScreen(this));
		screenHashtable.put(CHATSCREEN,new ChatScreen(this));
		screenHashtable.put(BATTLESCREEN,new BattleScreen(this));
		screenHashtable.put(DICOSCREEN,new EncyclopedieScreen(this));


		changeScreen(0);
	}

	public void changeScreen(int nextScreen){
		if(screenHashtable.containsKey(nextScreen))
			setScreen(screenHashtable.get(nextScreen));
	}


}
