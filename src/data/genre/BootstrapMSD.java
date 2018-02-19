package data.genre;

import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class BootstrapMSD {

	static HashMap<String, String> bootstrapData = null;

	public static HashMap<String, String> getBootstrapData() throws IOException {

		if(bootstrapData == null) {
			
			bootstrapData = new HashMap<String, String>();
			BufferedReader br = new BufferedReader(new FileReader("/Dizertatie/bootstrapMSD.txt"));
			try { 
				String line; 
				String split[];

				while ((line = br.readLine()) != null) { 

					if(!line.contains("#") && !line.equals("")) {
						split = line.split("	", 2);
						bootstrapData.put(split[0], split[1]); 	
					}

				} 
			} finally {
				br.close();
			}  
		}
		
		return bootstrapData; 
	}

}
