package m4ges.models;

public class Citation {
	
	private String phrase;
	private String auteur;
	
	public Citation(String phrase, String auteur) {
		super();
		this.phrase = phrase;
		this.auteur = auteur;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}
	

}
