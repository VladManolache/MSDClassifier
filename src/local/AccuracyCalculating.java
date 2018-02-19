package local;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;  

import data.genre.BootstrapGenres;
import main.Configurations;
import utils.ResultsManagement; 
import utils.Utils;
 
public class AccuracyCalculating {

	 
	public static double calculateTotalAccuracy(String resultsFilePath) throws Exception {
		
		int total = 0;
		int correct = 0;
		String line; 
		String[] split;
		int prediction;
		int actualValue;
		 
		ArrayList<Integer> targets = TestSetGenreExtracting.run();
		try (BufferedReader br = new BufferedReader(new FileReader(resultsFilePath))) {

			while ((line = br.readLine()) != null) {

				if (!line.contains("labels")) {
					split = line.split(" ");
					prediction = (int) Math.round(Double.parseDouble(split[0]));
					actualValue = targets.get(total);
					if (actualValue == prediction) {
						correct++;
					}
					total++;
				}
			}
		} 
		
		return (double)correct/total*100;
	}
	
	public static void calculateAndPrintAccuracyStatistics(String resultsFilePath) throws Exception {
		
		int total = 0; 
		String line; 
		String[] split;
		int prediction;
		int actualValue;
		
		ArrayList<String> classesInResultsFile = ResultsManagement.getClassesInResultsFile(resultsFilePath);
		int arraySize = Utils.getAllGenres().length;
		int[][] totalPredictions = new int[arraySize][arraySize];
		int[][] actualValues = new int[arraySize][arraySize];
		
		ArrayList<Integer> targets = TestSetGenreExtracting.run(); 
		BufferedReader br = new BufferedReader(new FileReader(resultsFilePath));
		try {  
			while ((line = br.readLine()) != null) { 
				
				if(!line.contains("labels")) { 
					split = line.split(" ");
					prediction = (int)Math.round(Double.parseDouble(split[0]));
					actualValue = targets.get(total);
					
					totalPredictions[prediction][actualValue]++;
					actualValues[actualValue][actualValue]++;
					total++;
				} 
			} 
		} finally {
			br.close();
		} 
		
        // Map the class id's from the results file back to initial ids.
		ArrayList<String> classMappings = ResultsManagement.getClassMappings();
		if(Configurations.bootstrapMSD && Configurations.singleClassLabeling) { 
			int currentId;
			for(int i = 0; i < classesInResultsFile.size(); i++) {
				currentId = Integer.parseInt(classMappings.get(Integer.parseInt(classesInResultsFile.get(i))));
	        	System.out.print(BootstrapGenres.valueFromId(currentId)+" ");
	        }
		}
		else {
			for(int i = 0; i < classesInResultsFile.size(); i++) {
	        	System.out.print(classesInResultsFile);
	        }
		}
		System.out.println();
        
		// Print matrix of predictions. X is prediction Y is actual value.
		System.out.println();
		System.out.println("Predicted accuracy:");
		for(int i = 0; i < arraySize; i++) { 
			for(int j = 0; j < arraySize; j++)  { 
				if(totalPredictions[i][j] != 0) {
					System.out.printf("%.1f", (float)totalPredictions[i][j]/total*100);
					System.out.print("% ");
				}
				else {
					System.out.print("0.0% ");
				}
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Actual genres:");
		for(int i = 0; i < arraySize; i++) { 
			for(int j = 0; j < arraySize; j++)  { 
				if(actualValues[i][j] != 0) {
					System.out.printf("%.1f", (float)actualValues[i][j]/total*100);
					System.out.print("% ");
				}
				else {
					System.out.print("0.0% ");
				}
			}
			System.out.println();
		}
		System.out.println();
		  
		System.out.println("Differences between predictions and actual values:");
		for(int i = 0; i < arraySize; i++) {
			System.out.printf("%.1f", Math.abs((float)actualValues[i][i] - totalPredictions[i][i])/total*100);
			System.out.print("% ");
		}
		System.out.println(); 
	}
}
