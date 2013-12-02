package com.me.mygdxgame;

import gameMechanic.Personnage;
import gameMechanic.Skill;
import chat.ChatServer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * Ici on initialise tout les screen
 * Depart du jeu appelle dans les main
 * @author Florian
 *
 */
public class MyGame extends Game {


	public MenuPrincipalScreen mainMenuScreen;
	public AnimationPlayerScreen animationScreen;
	public CreateCharacterScreen createCharacterScreen;
	public ChatScreen chatScreen;
	public BeginingScreen beginingScreen;
	/**
	 * le personnage du joueur initialise lors de la creation
	 */
	public Personnage player;
	public UITrick androidUI;
	public ChatServer chatServer;


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
		beginingScreen = new BeginingScreen(this);
		setScreen(mainMenuScreen);
		Skill.initListSkill();
	}
}
