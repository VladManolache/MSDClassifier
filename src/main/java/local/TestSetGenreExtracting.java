package local;

import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException; 
import java.util.ArrayList;
 
import main.FilePaths;

public class TestSetGenreExtracting {
 
	public static ArrayList<Integer> run() throws Exception { 
		TestSetGenreExtracting majorityVoting = new TestSetGenreExtracting();
		return majorityVoting.executeTargetExtracting();
	}

	private ArrayList<Integer> executeTargetExtracting() throws IOException {

		ArrayList<Integer> targets = new ArrayList<Integer>();

		try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.scaled_test_file_name + "_0"))) {
			String line;
			String split[];

			while ((line = br.readLine()) != null) {
				split = line.split(" ", 2);
				targets.add((int) Math.round(Double.parseDouble(split[0])));
			}
		}    

		return targets;
	}

}