package com.me.mygdxgame;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationPlayerScreen implements Screen{

	private static final int nb_colone = 4;
	private static final int nb_ligne = 4;
	
	private static final int nombre_image_animation = 4;
	
	private static final float duree_animation = 1f;
	private  float vitesse_lapin = 1f;

	private SpriteBatch batch;
	private Texture sheetTexture;
	private Animation animationLapin[];
	private TextureRegion regionCourante,regionInitial;

	private  int largeur_texture ;
	private  int hauteur_texture;

	private  float   temps;

	private float xPosLapin;
	private float yPosLapin;

	private int type_animation;
	private boolean animation_stop;

	private int type_application;
	
	private MyGame game;
	
	public AnimationPlayerScreen(MyGame game){
		this.game=game;
		// Type d'application
		ApplicationType type = Gdx.app.getType();
		type_application= type.compareTo(ApplicationType.Desktop);

		// Si Application Android Augmenter la vitesse du lapin
		if(type_application!=0)
			vitesse_lapin*=10;         

		// Initialisation  
		batch = new SpriteBatch();
		sheetTexture = new Texture(Gdx.files.internal("red.png"));
		largeur_texture = sheetTexture.getWidth();
		hauteur_texture = sheetTexture.getHeight();
		animation_stop = false;
		animationLapin = new Animation[8] ;
		type_animation=0;
		temps=0.0f;

		// Construction du tableau d'images constituants l'animation
		TextureRegion[][] tmp = TextureRegion.split(sheetTexture, largeur_texture/nb_colone, hauteur_texture/nb_ligne);
		regionInitial = tmp[0][1];

		// Positionner le Lapin en milieu
		xPosLapin= Gdx.graphics.getWidth()*0.5f;
		yPosLapin= Gdx.graphics.getHeight()*0.5f;

		// Instancier l'animation
		for(int i=0;i<4;i++)
			animationLapin[i] = new Animation(duree_animation/nombre_image_animation, tmp[i]);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);   
		Gdx.gl.glClearColor(0,0.5f,0.2f,0);


		// Fixer lapin
		if(!animation_stop) // si aucune manipulation en cours
		{
			temps += Gdx.graphics.getDeltaTime();                   
			System.err.println(type_animation);
			regionCourante = animationLapin[type_animation].getKeyFrame(temps/1, true);  
		}

		// Dessiner Lapin
		batch.begin();
		batch.draw(regionCourante, xPosLapin, yPosLapin);                    
		batch.end();

		// Manipuler Lapin
		if(type_application == 0)  // application Desktop
			ManipulerLapin_clavier();
		if(type_application == -1)  // application Android
			ManipulerLapin_accelerometre();
	}

	@Override
	public void resize(int arg0, int arg1) {
	}
	@Override
	public void resume() {
	}

	//*********************** manipulation entrées accéléromètre******************************//
	private void ManipulerLapin_accelerometre() // => destinée Android
	{
		// Les directions principales
		if(Gdx.input.getAccelerometerY()>1)
		{
			xPosLapin+=vitesse_lapin;
			type_animation=3;
		}
		if(Gdx.input.getAccelerometerY()<-1)
		{
			xPosLapin-=vitesse_lapin;
			type_animation=0;
		}
		if(Gdx.input.getAccelerometerX()>1)
		{
			yPosLapin-=vitesse_lapin;
			type_animation=2;
		}
		if(Gdx.input.getAccelerometerX()<-1)
		{
			yPosLapin+=vitesse_lapin;
			type_animation=1;
		}

		// La direction initial du Lapin
		if(Gdx.input.getAccelerometerX()<1 && Gdx.input.getAccelerometerY()<1 && Gdx.input.getAccelerometerX()>-1 && Gdx.input.getAccelerometerY()>-1 )
		{
			animation_stop = true;
			regionCourante = regionInitial ;
		}else
		{
			animation_stop = false;
		}
	}

	//****************** Manipulation entrées accéléromètre***********************************//

	private void ManipulerLapin_clavier() // => Destinée pour Desktop
	{

		// Les directions principales

		if(Gdx.input.isKeyPressed(Keys.UP))
		{
			yPosLapin+=vitesse_lapin;
			type_animation=3;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN))
		{
			yPosLapin-=vitesse_lapin;
			type_animation=0;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT) )
		{
			xPosLapin+=vitesse_lapin;
			type_animation=2;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT) )
		{
			xPosLapin-=vitesse_lapin;
			type_animation=1;
		}

		// Les directions secondaires
//		if(Gdx.input.isKeyPressed(Keys.UP) && Gdx.input.isKeyPressed(Keys.RIGHT) )
//		{
//			yPosLapin+=vitesse_lapin*0.5f;
//			xPosLapin+=vitesse_lapin*0.5f;
//			type_animation=2;
//		}
//		if(Gdx.input.isKeyPressed(Keys.UP) && Gdx.input.isKeyPressed(Keys.LEFT))
//		{
//			yPosLapin+=vitesse_lapin*0.5f;
//			xPosLapin-=vitesse_lapin*0.5f;
//			type_animation=6;
//		}
//		if(Gdx.input.isKeyPressed(Keys.DOWN) && Gdx.input.isKeyPressed(Keys.RIGHT))
//		{
//			yPosLapin-=vitesse_lapin*0.5f;
//			xPosLapin+=vitesse_lapin*0.5f;
//			type_animation=5;
//		}
//		if(Gdx.input.isKeyPressed(Keys.DOWN) && Gdx.input.isKeyPressed(Keys.LEFT))
//		{
//			yPosLapin-=vitesse_lapin*0.5f;
//			xPosLapin-=vitesse_lapin*0.5f;
//			type_animation=1;
//		}

		// La direction initial du Lapin
		if(!Gdx.input.isKeyPressed(Keys.ANY_KEY))
		{
			regionCourante = regionInitial;
			animation_stop=true;
		}
		else
			animation_stop=false;
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}