package m4ges.models;

import java.util.ArrayList;
import java.util.Random;

import m4ges.models.monster.Flower;
import m4ges.models.monster.Lutin;
import m4ges.models.monster.Phantom;
import m4ges.models.monster.Skeleton;

import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.jsonbeans.Test.Person;

public class Vague {

	private String bgName;
	private ArrayList<Personnage> listMonstrePossible;
	private ArrayList<Personnage> listMonstrePresent;
	
	public Vague(String bgName, ArrayList<Personnage> listMonstrePossible,
			ArrayList<Personnage> listMonstrePresent) {
		super();
		this.bgName = bgName;
		this.listMonstrePossible = listMonstrePossible;
		this.listMonstrePresent = listMonstrePresent;
	}
	
	public static Vague loadVague(int number){
		Json json = new Json();
		ArrayList<Personnage> l1= new ArrayList<Personnage>();
		l1.add(new Skeleton());
		l1.add(new Flower());
		l1.add(new Flower());
		l1.add(new Lutin());
		l1.add(new Lutin());
		l1.add(new Phantom());
		
		ArrayList<Personnage> l2 = new ArrayList<Personnage>();
		l2.add(l1.get(randInt(0, l1.size())));

		
		Vague v = new Vague("battle_background", l1, l2);
		
		
		String text = json.toJson(v);
		System.out.println(json.prettyPrint(v));

		Vague v2 = json.fromJson(Vague.class, text);
		return null;
	}
	
	public static void main(String[] args){
		loadVague(1);
	}
	/**
	 * Returns a psuedo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimim value
	 * @param max Maximim value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
