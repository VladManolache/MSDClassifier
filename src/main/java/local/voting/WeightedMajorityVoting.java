package local.voting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException; 
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import main.FilePaths;
 

public class WeightedMajorityVoting {
 
	class Vote {
		String vote;
		String weight;
		
		Vote(String vote, String weight) {
			this.vote = vote;
			this.weight = weight;
		}
	} 
	 
	private void executeWeightedMajorityVoting() throws IOException {
		
		ArrayList<List<Vote>> accumulatedVotes = new ArrayList<List<Vote>>(); 
		
		// Get all votes of all classifiers.
		File directory = new File(FilePaths.resultSetPath);
		for (final File fileEntry : directory.listFiles()) {
			
			if (!fileEntry.getName().equals(".DS_Store")) {
				accumulatedVotes.add(getResultSet(fileEntry.getAbsolutePath()));  
			}
		}
		
		// Apply weighted majority voting for each result entry.
		Vote[] array; 
		int[] result = new int[accumulatedVotes.get(0).size()];
		for (int i = 0; i < accumulatedVotes.get(0).size(); i++) {

			array = new Vote[accumulatedVotes.size()];
			for(int j = 0; j < accumulatedVotes.size(); j++) {
				array[j] = accumulatedVotes.get(j).get(i);
			}
			result[i] = mostFrequent(array);
		} 
		  
		// Write results to aggregate file
		PrintWriter writer = new PrintWriter(FilePaths.resultAggregate_file_name, "UTF-8");
		for (int aResult : result) {
			writer.println(aResult);
		}
		writer.close();
	}

	private ArrayList<Vote> getResultSet(String path) throws IOException {
 
		ArrayList<Vote> resultSet = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			String split[];

			ArrayList<String> labels = new ArrayList<>();
			while ((line = br.readLine()) != null) {

				// This is the first line. Get the labels the correct order.
				if (line.contains("labels")) {
					split = line.split(" ");
					for (int i = 1; i < split.length; i++) {
						labels.add(split[i]);
					}
				} else {

					/*
					 *  Using the labels obtained above, get the probability for each class.
					 *  Get the index of the label that contains the probability for the current class.
					 */
					split = line.split(" ");
					for (int i = 0; i < labels.size(); i++) {

						if (Integer.parseInt(labels.get(i)) == (int) Double.parseDouble(split[0])) {
							resultSet.add(new Vote(split[0], split[i + 1]));  // First index is actual label of current class.
							break;
						}
					}
				}
			}
		}
		return resultSet;
	}
 
	int mostFrequent(Vote... ary) {
		Map<Integer, Double> m = new HashMap<Integer, Double>();
 
		int key;
		double weight;
		for (Vote a : ary) {
			key = (int)Double.parseDouble(a.vote);
			weight = Double.parseDouble(a.weight);
			Double freq = m.get(key); 
			m.put(key, (freq == null) ? weight : freq + weight);
		} 
		
		double max = -1;
		int mostFrequent = -1;

		for (Map.Entry<Integer, Double> e : m.entrySet()) {
			if (e.getValue() > max) {
				mostFrequent = e.getKey();
				max = e.getValue();
			}
		}

		return mostFrequent;
	}

	public static void run() throws Exception { 
		WeightedMajorityVoting weightedMajorityVoting = new WeightedMajorityVoting();
		weightedMajorityVoting.executeWeightedMajorityVoting();
	}
}
