package data.genre;
 
 
public enum Genres { 
 
	ROCK(0, "rock"),
	POP(1, "pop"),
	JAZZ(2, "jazz"), 
	HIPHOP(3, "hip hop"),
	ELECTRONIC(4, "electronic");
//	COUNTRY(5, "country"),
//	INTERNATIONAL(6, "international");
//	LATIN(7, "latin");
//	REGGAE(8, "reggae");
	 
	private static Genres[] allowedGenres = Genres.values();
	
	private int id;
	private String value;
	private Genres(int id, String value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public String getValue() {
		return value; 
	} 
	
	public static Genres[] getAllowedGenres() {
		return allowedGenres;
	}
	
	public static String valueFromId(int id) {
		
		Genres[] allowedGenres = Genres.getAllowedGenres();
		for(int i = 0; i < allowedGenres.length; i++) {
			if(allowedGenres[i].id == id) {
				return allowedGenres[i].value;
			}
		}
		return "";
	}
}