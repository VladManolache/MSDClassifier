package common;

import java.util.Collections;
import java.util.List;

import main.Configurations;
import utils.Utils;

import com.google.common.collect.Lists;

import data.hdf5.HDF5_Utils;
  

public class DataSplitter {
   
	private static DataSplitter INSTANCE = null; 
	static List<String> bigList;
	static List<List<String>> partitionedList;
	
	private DataSplitter() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static DataSplitter getInstance() {
    	
    	if(INSTANCE == null) {
    		INSTANCE = new DataSplitter();
    		
    		bigList = HDF5_Utils.getHDF5FileNamesList(Configurations.dataSetPath, Configurations.allowedSubfolders);
    		if(Configurations.shuffleData) {
        		Collections.shuffle(bigList);
    		}
    		partitionedList = Lists.partition(bigList, ((int)(bigList.size()*Configurations.ratio)) + 1);
    	}
        return INSTANCE;
    }
	
	// Used for separating the list into Constants.folds sets.
	public static List<List<String>> splitData(List<String> paths) {
		 		
		if(Configurations.shuffleData) {
			Collections.shuffle(paths);
		}
		int partitionSize = ((int) (paths.size() / Configurations.trainingSetFolds)) + 1; 
		return Lists.partition(paths, partitionSize) ; 
	}
	 
	/*
	 * Utilities used for separating the initial data into training and test data sets.
	 */
	public List<String> trainDatasetPath() {  
		return partitionedList.get(0);
	}
	
	public List<String> testDatasetPath() { 
		return partitionedList.get(1);
	}  
	
	public void printTotalNumberOfFiles() {
		 
		Utils.logInfo("\nDid select training and test sets:");
		Utils.logInfo("Number of training files: "+partitionedList.get(0).size());
		Utils.logInfo("Number of test files: "+partitionedList.get(1).size()+"\n");
	}
}
