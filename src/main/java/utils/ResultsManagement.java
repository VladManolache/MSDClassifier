package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.Configurations;
import main.FilePaths;

public class ResultsManagement {

	public static ArrayList<String> getClassMappings() throws IOException {
		
		String dataMappingFile = FilePaths.temp_scaling_file_name;
		
		// Make sure not to crash in the dataMappingFile does not exist.
		File file = new File(dataMappingFile);
		if (!file.exists()) {
			return null;
		}
		
		int maxValue = 0;
		ArrayList<String> mapping = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(dataMappingFile))) {
			String line;
			String split[];
			while ((line = br.readLine()) != null) {
				if (!line.contains("labels")) {
					split = line.split(" ", 2);
					mapping.add(split[0]);
					if (Integer.parseInt(split[0]) > maxValue) {
						maxValue = Integer.parseInt(split[0]);
					}
				}
			}
		}
		
		for (int i = 0; i < maxValue; i++) {
			if (!mapping.contains(i)) {
				mapping.add(i + "");
			}
		}
		
		return mapping;
	}
	
	public static ArrayList<String> getClassesInResultsFile(String fileName) throws IOException {
		
		ArrayList<String> classesInResultsFile = new ArrayList<String>();

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			String split[];

			while ((line = br.readLine()) != null) {

				if (line.contains("labels")) {
					split = line.split(" ");
					for (int j = 1; j < split.length; j++) {
						classesInResultsFile.add(split[j]);
					}
				}
			}
		}
        
		return classesInResultsFile;
	}
	
	/* !!! --- Mapping is only done on single-thread --- !!!
	 * This logic will hide that mapping is done for multi-threaded executions.
	 */
	public static ArrayList<String> getFinalResults(String fileName, ArrayList<String> classMappings) throws IOException {
		
		ArrayList<String> actualResultIds = new ArrayList<String>(); 
		
        // Add values for that dataset. 
		int runConfiguration = Configurations.runConfiguration;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			String split[];

			while ((line = br.readLine()) != null) {

				if (!line.contains("labels")) {

					split = line.split(" ", 2);
					int currentItem = ((int) Double.parseDouble(split[0]));

					if (runConfiguration != 1) {
						actualResultIds.add(currentItem + "");
					} else {
						actualResultIds.add(classMappings.get(currentItem));
					}
				}
			}
		}
        
		return actualResultIds;
	}
	
}
