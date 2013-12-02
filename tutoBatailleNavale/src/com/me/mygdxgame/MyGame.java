package com.me.mygdxgame;

import gameMechanic.Personnage;
import gameMechanic.Skill;
import chat.ChatServer;
import com.badlogic.gdx.Game;

/**
 * Ici on initialise tout les screen
 * Depart du jeu appelle dans les main
 * @author Florian
 *
 */
public class MyGame extends Game {


	MenuPrincipalScreen mainMenuScreen;
	AnimationPlayerScreen animationScreen;
	CreateCharacterScreen createCharacterScreen;
	ChatScreen chatScreen;
	/**
	 * le personnage du joueur initialise lors de la creation
	 */
	public Personnage player;
	public ChatServer chatserver;
	UITrick androidUI;


	public MyGame(UITrick actionResolver) {
		super();
		this.androidUI = actionResolver;
		
	}



	@Override
	public void create() {
		mainMenuScreen = new MenuPrincipalScreen(this);
		animationScreen = new AnimationPlayerScreen(this);
		createCharacterScreen = new CreateCharacterScreen(this);
		chatScreen = new ChatScreen(this);
		setScreen(mainMenuScreen);
		Skill.initListSkill();
	}
}
