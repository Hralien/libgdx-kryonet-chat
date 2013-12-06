package com.me.mygdxgame;

import java.util.ArrayList;

import gameMechanic.Personnage;
import chat.ChatServer;
import com.badlogic.gdx.Game;

/**
 * Ici on initialise tout les screen
 * Depart du jeu appelle dans les main
 * @author Florian
 *
 */
public class MyGame extends Game {


	public MenuPrincipalScreen mainMenuScreen;
	public CreateCharacterScreen createCharacterScreen;
	public ChatScreen chatScreen;
	public BeginingScreen beginingScreen;
	/**
	 * le personnage du joueur initialise lors de la creation
	 */
	public Personnage player;
	public UITrick androidUI;
	public ChatServer chatServer;
	public ArrayList<String> listHost ;
	public ArrayList<Personnage> playersConnected;
	
	public MyGame(UITrick actionResolver) {
		super();
		this.androidUI = actionResolver;
		listHost = new ArrayList<String>();
		playersConnected = new ArrayList<Personnage>();
	}



	@Override
	public void create() {
		mainMenuScreen = new MenuPrincipalScreen(this);
		createCharacterScreen = new CreateCharacterScreen(this);
		chatScreen = new ChatScreen(this);
		beginingScreen = new BeginingScreen(this);
		setScreen(mainMenuScreen);
//		Skill.initListSkill();
	}
}
