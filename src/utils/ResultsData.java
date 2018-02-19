package utils;

import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import common.DataSplitter;
import main.Configurations; 
import ncsa.hdf.object.h5.H5File;
import data.genre.BootstrapGenres;
import data.genre.Genres;
import data.hdf5.HDF5; 


public class ResultsData {
 
	List<String> testFiles;
	ArrayList<String> classMappings;
	double[] nr_occurances;
	String resultsFile;
	
	public static void run(String resultsFilePath) throws Exception { 
		ResultsData resultsData = new ResultsData();
		resultsData.printResults(resultsFilePath); 
	}
	 
	public void printResults(String resultsFile) throws Exception {

		this.resultsFile = resultsFile; 
		
		Utils.logConsoleSeparator();
		Utils.logInfo("\n    Results are: \n");
		 
		testFiles = DataSplitter.getInstance().trainDatasetPath();
		classMappings = ResultsManagement.getClassMappings();
		nr_occurances = getNrOccurances(classMappings.size()); 
		
		printAllSongsForEachTag();
	}
	
	private void printAllSongsForEachTag() throws Exception {
		
		int id;
		int currentIndex;
		for(int i = 0; i < nr_occurances.length; i++) {	// For each song label.
			 
			if(nr_occurances[i] == 0) {
				continue;
			}
			 
			// Print song label(s) and number of songs that are classified with the current label.
			StringTokenizer strTok = new StringTokenizer(classMappings.get(i), ",");  
			while (strTok.hasMoreTokens()) {   
				
				id = Integer.parseInt(strTok.nextToken()); 
				if(Configurations.bootstrapMSD) {
					System.out.print(BootstrapGenres.valueFromId(id)+" ");  
				}
				else { 
					System.out.print(Genres.valueFromId(id)+" ");  
				}
			}  
			System.out.println(" - "+(int)nr_occurances[i]+" songs\n"); 
			
			// Print all songs that correspond to the current label.
			ArrayList<Integer> indexes = indexesClassified(i, nr_occurances.length);
			for(int j = 0; j < indexes.size(); j++) {
				 
				currentIndex = indexes.get(j);
				H5File currentFile = HDF5.hdf5_open_readonly(testFiles.get(currentIndex));
				System.out.println("Artist: "+HDF5.get_artist_name(currentFile)+" - "+HDF5.get_title(currentFile));
				currentFile.close(); 
			}
			System.out.println("");
		}
	}
	
	private double[] getNrOccurances(int size) throws IOException {
		
		double[] nr_occurances = new double[size];
		BufferedReader br = new BufferedReader(new FileReader(resultsFile));
		try { 
			
			String line;
			String split[]; 
			while ((line = br.readLine()) != null) {
				
				if(!line.contains("labels")) {
					split = line.split(" ", 2);
					nr_occurances[(int) Double.parseDouble(split[0])]++; 	
				} 
			} 
		} finally {
			br.close();
		}
		return nr_occurances;
	}
	
	private ArrayList<Integer> indexesClassified(int classification, int size) throws IOException {
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		BufferedReader br = new BufferedReader(new FileReader(resultsFile));
		try { 
			String line;
			String split[]; 
			int index = 0;
			
			while ((line = br.readLine()) != null) {
				
				if(!line.contains("labels")) {

					split = line.split(" ", 2);
					if(classification == (int) Double.parseDouble(split[0])) {
						indexes.add(index);
					} 
					index++;
				}  
			} 
		} finally {
			br.close();
		} 
		
		return indexes; 
	}
	
}
