package m4ges.models.classes;

import m4ges.models.Personnage;
import m4ges.models.Skill;
import m4ges.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Aquamancien extends Joueur {

	private final static String DESCRIPTION = "L'aqueromancien ne paye peut �tre pas de mine, mais son pouvoir de destruction, combin� � celui d'un pyromancien, en font un adversaire redoutable.";
	protected static volatile Animation animation;

	public Aquamancien() {
		super();
		super.hp=90;
		super.hp_max = 90;
		super.mana=110;
		super.mana_max = 110;
		super.strength=8;
		super.speed=11;
		super.intel=12;

		super.listSkills=Skill.getSkillForClass(Personnage.AQUAMANCIEN);

	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return DESCRIPTION;
	}
	/**
	 * M�thode permettant de renvoyer une instance de la classe Singleton
	 * @return Retourne l'instance du singleton.
	 */
	public Animation animate() {
		//Le "Double-Checked Singleton"/"Singleton doublement v�rifi�" permet 
		//d'�viter un appel co�teux � synchronized, 
		//une fois que l'instanciation est faite.
		if (Aquamancien.animation == null) {
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
			// Il est TRES important.
			synchronized(Aquamancien.class) {
				if (Aquamancien.animation == null) {
					Texture sprite = new Texture(Gdx.files.internal("character/aquamancien.png"));
					TextureRegion[] regions = new TextureRegion[9]; 
					regions[0] = new TextureRegion(sprite, 0, 0, 30, 50);
					regions[1] = new TextureRegion(sprite, 30, 0, 33, 50);
					regions[2] = new TextureRegion(sprite, 64, 0, 32, 50);
					regions[3] = new TextureRegion(sprite, 95, 0, 32, 50);
					regions[4] = new TextureRegion(sprite, 127, 0, 33, 50);
					regions[5] = new TextureRegion(sprite, 160, 0, 29, 50);
					regions[6] = new TextureRegion(sprite, 189, 0, 30, 50);
					regions[7] = new TextureRegion(sprite, 0, 50, 51, 26);
					regions[8] = new TextureRegion(sprite, 70, 50, 51, 26);
					animation = new Animation(0.1f, regions);              // #11

				}
			}
		}
		return Aquamancien.animation;
	}

	/**
	 *  Sur les deux premiers octets : les infos
	 *  sur le reste le pseudo du joueur
	 * @return les donnees a envoyer
	 */
	public byte[] getBytes(){
		byte data[] = new byte[3+this.name.length()];
		data[0] = Constants.CONNEXION;
		data[1] = Personnage.AQUAMANCIEN;
		//Et oui, on peut doit l'ip apres :(
		data[2] = (byte) this.name.length();
		byte[] pseudo = this.name.getBytes();
		for(int i = 3; i < pseudo.length+3;i++){
			data[i] = pseudo[i-3];
		}
		return data;
	}

	@Override
	public String getNameClass() {
		return "Aquamancien";
	}
}
