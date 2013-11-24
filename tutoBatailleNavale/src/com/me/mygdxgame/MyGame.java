package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

/**
 * Ici on initialise tout les screen
 * Depart du jeu appelle dans les main
 * @author Florian
 *
 */
public class MyGame extends Game implements ApplicationListener{
	 

    MenuPrincipalScreen mainMenuScreen;
    AnimationPlayerScreen anotherScreen;


   @Override
     public void create() {
             mainMenuScreen = new MenuPrincipalScreen(this);
             anotherScreen = new AnimationPlayerScreen(this);
             setScreen(mainMenuScreen);              
     }
}
