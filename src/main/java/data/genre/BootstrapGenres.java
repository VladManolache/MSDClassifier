package data.genre;


public enum BootstrapGenres {
	 
	BLUES(0, "Blues"),
	COUNTRY(1, "Country"),
	ELECTRONIC(2, "Electronic"),
	INTERNATIONAL(3, "International"),
	JAZZ(4, "Jazz"),
	LATIN(5, "Latin"),
	POPROCK(6, "Pop/Rock"),
	RNB(7, "R&B"),
	RAP(8, "Rap"),
	REGGAE(9, "Reggae");
	
	private static BootstrapGenres[] allowedGenres = BootstrapGenres.values();
	
	private int id;
	private String value;
	private BootstrapGenres(int id, String value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public String getValue() {
		return value; 
	} 
	
	public static BootstrapGenres[] getAllowedGenres() {
		return allowedGenres;
	}
	
	public static String valueFromId(int id) {
		
		BootstrapGenres[] allowedGenres = BootstrapGenres.getAllowedGenres();
		for(int i = 0; i < allowedGenres.length; i++) {
			if(allowedGenres[i].id == id) {
				return allowedGenres[i].value;
			}
		}
		return "";
	} 
}
