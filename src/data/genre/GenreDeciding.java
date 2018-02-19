package data.genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import data.hdf5.HDF5FileData;
import main.Configurations;
  

public class GenreDeciding {
	 
	public static ArrayList<String> getGenreTagAfterBootstrappingForSong(HDF5FileData fileSegmentsObject, 
			HashMap<String, String> bootstrap) {
		
		ArrayList<String> genres = new ArrayList<String>();
		String temp = bootstrap.get(fileSegmentsObject.trackId);
		if(temp != null) {
			
			BootstrapGenres[] allowedGenres = BootstrapGenres.getAllowedGenres();
			for(int j = 0; j < allowedGenres.length; j++) {
				
				if(temp.equals(allowedGenres[j].getValue())) { 
					genres.add(allowedGenres[j].getId()+"");
					break;
				}
			}
		}  
		return genres; 
	}
	
	public static ArrayList<String> getGenreTags(String[] tags) {
		 
		List<String> inputTags = Arrays.asList(tags);  
		  
		// Decide if we want single or multi-class labeling.
		ArrayList<String> allowedTags;
		if(Configurations.singleClassLabeling == true)  {
			allowedTags = singleClassLabeling(inputTags);
		}
		else {
			allowedTags = multiClassLabeling(inputTags);
		}
		
		return allowedTags;
	}
	
	private static ArrayList<String> singleClassLabeling(List<String> inputTags) {
		
		/* 
		 *  Input-tags oriented. Avoid issues caused by setting the tag based on our pre-defined order. 
		 */ 
		ArrayList<String> allowedTags = new ArrayList<String>();
		Genres[] allowedGenres = Genres.getAllowedGenres();
		for(int i = 0; i < inputTags.size(); i++) {
			
			for(int j = 0; j < allowedGenres.length; j++) {
				
				if(inputTags.get(i).equals(allowedGenres[j].getValue())) {
					allowedTags.add(allowedGenres[j].getId()+"");
					return allowedTags;
				}
			}
		}
		return allowedTags;
	}
	
	private static ArrayList<String> multiClassLabeling(List<String> inputTags) {
		
		/* 
		 *  Genre-tags oriented. Make sure we set the tags in order to avoid having more classes than we need to have.
		 */ 
		ArrayList<String> allowedTags = new ArrayList<String>();
		Genres[] allowedGenres = Genres.getAllowedGenres();
		for(int i = 0 ; i < allowedGenres.length; i++) {
			
			if(inputTags.contains(allowedGenres[i].getValue())) {
				allowedTags.add(allowedGenres[i].getId()+"");  
			}
		}
		return allowedTags;
	}
}
