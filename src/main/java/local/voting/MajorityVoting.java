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


public class MajorityVoting {
  
	
	public static void run() throws Exception { 
		MajorityVoting majorityVoting = new MajorityVoting();
		majorityVoting.executeMajorityVoting();
	}

	private void executeMajorityVoting() throws IOException {
		ArrayList<List<Double>> list = new ArrayList<List<Double>>();

		// Get all votes of all classifiers.
		File directory = new File(FilePaths.resultSetPath);
		for (final File fileEntry : directory.listFiles()) {
			
			if (!fileEntry.getName().equals(".DS_Store")) {
				list.add(getResultSet(fileEntry.getAbsolutePath())); 
			}
		}
		
		// Apply majority voting for each result entry.
		int[] array; 
		int[] result = new int[list.get(0).size()];
		for(int i = 0; i < list.get(0).size(); i++) {

			array = new int[list.size()];
			for (int j = 0; j < list.size(); j++) {
				array[j] = (int) Math.round(list.get(j).get(i));
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

	private ArrayList<Double> getResultSet(String path) throws IOException {
 
		ArrayList<Double> resultSet = new ArrayList<Double>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			String split[];

			while ((line = br.readLine()) != null) {

				if (!line.contains("labels")) {    // Skip first line if it contains the label list.
					split = line.split(" ", 2);
					resultSet.add(Double.parseDouble(split[0]));
				}
			}
		}
		return resultSet;
	}
 
	int mostFrequent(int... ary) {
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();

		for (int a : ary) {
			Integer freq = m.get(a);
			m.put(a, (freq == null) ? 1 : freq + 1);
		}

		int max = -1;
		int mostFrequent = -1;

		for (Map.Entry<Integer, Integer> e : m.entrySet()) {
			if (e.getValue() > max) {
				mostFrequent = e.getKey();
				max = e.getValue();
			}
		}

		return mostFrequent;
	}
}
