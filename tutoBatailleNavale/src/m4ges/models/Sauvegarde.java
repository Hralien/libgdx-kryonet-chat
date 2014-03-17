package m4ges.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import m4ges.models.classes.Joueur;
import m4ges.util.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class Sauvegarde {

	private ArrayList<Joueur> listJoueur;
	private int numVague;
	private int id;
	private String date;
	public 	static Locale locale = Locale.getDefault();

	public Sauvegarde() {
		super();
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		setDate(dateFormat.format(actuelle));
	}
	
	

	public Sauvegarde(ArrayList<Joueur> listJoueur, int numVague) {
		super();
		this.listJoueur = listJoueur;
		this.numVague = numVague;
		this.id = GamePreferences.instance.saveNumber;
	}



	public static Sauvegarde charger(){
		//creation d'un objet json
		Json json = new Json();
		GamePreferences prefs = GamePreferences.instance;
		//recuperation du fichier correspondant
		FileHandle file = Gdx.files.internal("files/saves/save_"+prefs.saveNumber+".txt" );
		//lecture du fichier
		String text = file.readString();
		//creation de la vague
		Sauvegarde vague = json.fromJson(Sauvegarde.class, text);
		for(Personnage it: vague.getListJoueur()){
			System.out.println(it);
		}
		return vague;
	}

	public void sauvegarder(){
		//creation d'un objet json
		Json json = new Json();
		//recup des preferences
		GamePreferences prefs = GamePreferences.instance;
		prefs.saveNumber++;
		//recuperation du fichier correspondant
		FileHandle file = Gdx.files.internal("files/saves/save_"+prefs.saveNumber+".txt" );
		prefs.save();
		//enregistrement 
		json.toJson(this, file);
	}

	public FileHandle[] getAllSave(){
		return Gdx.files.internal("files/saves").list();
	}
	public ArrayList<Joueur> getListJoueur() {
		return listJoueur;
	}

	public void setListJoueur(ArrayList<Joueur> listJoueur) {
		this.listJoueur = listJoueur;
	}

	public int getNumVague() {
		return numVague;
	}

	public void setNumVague(int numVague) {
		this.numVague = numVague;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
