package m4ges.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import m4ges.models.classes.Aquamancien;
import m4ges.models.classes.Chamane;
import m4ges.models.classes.Joueur;
import m4ges.models.classes.Necromancien;
import m4ges.models.classes.Pyromancien;
import m4ges.util.GamePreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

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
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		setDate(dateFormat.format(actuelle));
	}



	public static Sauvegarde charger(String saveName){
		String nb = saveName.substring(5);
		System.err.println(nb);
		//creation d'un objet json
		Json json = new Json();
		//recuperation du fichier correspondant
		FileHandle file = Gdx.files.internal("files/saves/save_"+nb);
		//lecture du fichier
		String text = file.readString();
		//creation de la vague
		Sauvegarde vague = json.fromJson(Sauvegarde.class, text);
		for(Joueur it: vague.getListJoueur()){
			System.out.println(it);
		}
		return vague;
	}

	public void sauvegarder(){
		//creation d'un objet json
		Json json = new Json();
//		json.setSerializer(Joueur.class, new Json.Serializer<Joueur>() {
//			@Override
//			public void write (Json json, Joueur j, Class knownType) {
//				json.writeObjectStart(j.getNameClass());
//				json.writeField(j.name, "name");
//				json.writeField(j.hp,"hp");
//				json.writeField(j.hpMax,"hpMax");
//				json.writeField(j.mana,"mana");
//				json.writeField(j.manaMax,"manaMax");
//				json.writeField(j.strength,"strength");
//				json.writeField(j.speed,"speed");
//				json.writeField(j.intel,"intel");
//				json.writeObjectEnd();
//			}
//
//
//			@Override
//			public Joueur read(Json json, JsonValue jsonData, Class type) {
//				Joueur j = null;
//				switch (jsonData.child().asString()) {
//				case "Chamane":
//					// initialisation du player
//					j = new Chamane();
//					GamePreferences.instance.nbChamanPlayed++;
//					break;
//				case "Necromancien":
//					// initialisation du player
//					j = new Necromancien();
//					GamePreferences.instance.nbNecroPlayed++;
//					break;
//				case "Pyromancien":
//					// initialisation du player
//					j = new Pyromancien();
//					GamePreferences.instance.nbPyroPlayed++;
//					break;
//				case "Aquamancien":
//					// initialisation du player
//					j = new Aquamancien();
//					GamePreferences.instance.nbAquaPlayed++;
//					break;
//				default:
//					System.err.println("switch Joueur error");
//				}
//				j.name=jsonData.child().asString();
//				j.hp= jsonData.child().asInt();
//				j.hpMax= jsonData.child().asInt();
//				j.mana= jsonData.child().asInt();
//				j.manaMax= jsonData.child().asInt();
//				j.strength= jsonData.child().asInt();
//				j.speed= jsonData.child().asInt();
//				j.intel= jsonData.child().asInt();
//
//				return j;
//			}
//		});
//		json.setElementType(Sauvegarde.class, "listJoueur", Joueur.class);

		//recup des preferences
		GamePreferences prefs = GamePreferences.instance;
		prefs.saveNumber++;
		//recuperation du fichier correspondant
		FileHandle file = Gdx.files.local("files/saves/save_"+prefs.saveNumber);
		prefs.save();
		//enregistrement 
		json.toJson(this, file);
	}

	public static ArrayList<String> getAllSave(){
		ArrayList<String> listName = new ArrayList<String>();
		FileHandle[] files = Gdx.files.local("files/saves/").list();
		for(FileHandle file: files) {
			listName.add(file.name());
		}
		return listName;
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
