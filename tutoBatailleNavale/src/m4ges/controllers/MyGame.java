package m4ges.controllers;

import java.util.ArrayList;
import java.util.Hashtable;

import m4ges.models.Personnage;
import m4ges.util.GamePreferences;
import m4ges.views.BattleScreen;
import m4ges.views.ChatScreen;
import m4ges.views.CreateCharacterScreen;
import m4ges.views.EncyclopedieScreen;
import m4ges.views.LoadingScreen;
import m4ges.views.MenuPrincipalScreen;
import m4ges.views.ResultScreen;
import reseau.GameClient;
import reseau.GameServer;
import reseau.MulticastClient;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;

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
	public final static int RESULTSCREEN = 5;
	public final static int DICOSCREEN = 6;


	/**
	 * le personnage du joueur initialise lors de la creation
	 */
	public Personnage player;
	public GameClient chatClient;

	public UITrick androidUI;
	public GameServer chatServer;
	public ArrayList<String> listHost ;
	public ArrayList<Personnage> playersConnected;
	public MulticastClient mc;

	public static AssetManager manager;
	
	private Stage currentStage;

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
		screenHashtable.put(BATTLESCREEN,new ResultScreen(this));
		screenHashtable.put(DICOSCREEN,new EncyclopedieScreen(this));
		changeScreen(0);
	}

	public void changeScreen(int nextScreen){
		if(screenHashtable.containsKey(nextScreen)){
			setScreen(screenHashtable.get(nextScreen));
		}
	}

	@Override
	public void dispose(){
		GamePreferences.instance.load();
		GamePreferences prefs = GamePreferences.instance;
		prefs.timePlayed += TimeUtils.millis() - AbstractScreen.getTimePlayed();
		prefs.save();
		System.err.println("fini:"+prefs.timePlayed/1000+"sec");
	}
	
	public void setMC(MulticastClient m){
		this.mc = m;
	}
	
	public MulticastClient getMC(){
		return this.mc;
	}
}
