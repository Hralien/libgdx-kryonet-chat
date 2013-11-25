package com.me.mygdxgame;

import gameMechanic.Personnage;

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
    public Personnage player;


   @Override
     public void create() {
             mainMenuScreen = new MenuPrincipalScreen(this);
             animationScreen = new AnimationPlayerScreen(this);
             createCharacterScreen = new CreateCharacterScreen(this);
             chatScreen = new ChatScreen(this);
             setScreen(mainMenuScreen);
     }
}
