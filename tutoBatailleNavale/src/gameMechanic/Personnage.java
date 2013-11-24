package gameMechanic;

public abstract class Personnage {
	
	private int hp;
	private int sp;
	private int xp;
	private int force;
	private int magie;
	private int vitesse;
	private String name;
	
	public Personnage(String name){
		this.name=name;
	}


}
