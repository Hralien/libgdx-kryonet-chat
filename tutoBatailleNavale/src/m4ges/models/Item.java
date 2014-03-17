package m4ges.models;

import java.util.ArrayList;

import m4ges.controllers.MyGame;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Item {
	//attributs de classe
	/**
	 * arraylist permettant de repertorier les items existants
	 */
	public static ArrayList<Item> listItem;
	private static volatile TextureAtlas atlas;

	//attribut d'un objet
	/**
	 * identifiant
	 */
	private int id;
	/**
	 * nom de l'objet
	 */
	private String name;
	/**
	 * est il utilisable?
	 */
	private boolean usable;
	/**
	 * si oui ItemId correspondant
	 */
	private int SkillId;
	/**
	 * l'image de l'item
	 */
	private Image image;
	/**
	 * rate de l'item , � partir duquel on peut l'avoir
	 */
	private int rate;

	/**
	 * constructeur 
	 * @param id
	 * @param name
	 * @param usable
	 * @param ItemId
	 */
	public Item(int id, String name, boolean usable, int SkillId, int rate) {
		super();
		this.id = id;
		this.name = name;
		this.usable = usable;
		this.SkillId = SkillId;
		this.rate = rate;
		this.image = new Image(getInstance().findRegion(name));

	}
	/**
	 * initialise la liste d'item
	 */
	public static ArrayList<Item> buildListItem() {
		if(listItem == null){
			listItem = new ArrayList<Item>();
			listItem.add(new Item(1,"cle",false, -1,30));
			listItem.add(new Item(2,"potion",true, 1,50));
		}
		return listItem;

	} 
	public static Item selectItemFromItemID(int itemId) {
		for (Item it : buildListItem()) {
			if (it.id == itemId) {
				return it;
			}
		}
		return null;
	}

	/**
	 * M�thode permettant de renvoyer une instance de la classe Singleton
	 * 
	 * @return Retourne l'instance du singleton.
	 */
	private final static TextureAtlas getInstance() {
		// Le "Double-Checked Singleton"/"Singleton doublement v�rifi�" permet
		// d'�viter un appel co�teux � synchronized,
		// une fois que l'instanciation est faite.
		if (Item.atlas == null) {
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
			// Il est TRES important.
			synchronized (Item.class) {
				if (Item.atlas == null) {
					Item.atlas = MyGame.manager.get("effects/Item.pack",
							TextureAtlas.class);
					buildListItem();
				}
			}
		}
		return Item.atlas;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	public int getSkillId() {
		return SkillId;
	}

	public void setSkillId(int ItemId) {
		this.SkillId = ItemId;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
}
