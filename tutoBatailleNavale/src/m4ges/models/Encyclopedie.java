package m4ges.models;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * liste des monstres déjà rencontré
 * @author Florian
 *
 */
public class Encyclopedie {

	private ArrayList<Personnage> monsters;
	private Encyclopedie(){

	}

	public static Encyclopedie loadEncyclopedie(){
		//creation d'un objet json
		Json json = new Json();
		//recuperation du fichier correspondant
		FileHandle file = Gdx.files.internal("files/encyclopedie.txt" );
		//lecture du fichier
		String text = file.readString();
		//creation de la vague
		Encyclopedie dico = json.fromJson(Encyclopedie.class, text);
		System.out.println(dico);
		for(Personnage it: dico.monsters){
			System.out.println(it);
		}
		return dico;
	}

	public ArrayList<Personnage> getMonsters() {
		return monsters;
	}

	public void setMonsters(ArrayList<Personnage> monsters) {
		this.monsters = monsters;
	}

}
