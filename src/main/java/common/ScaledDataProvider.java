package common;

import data.utils.DataSet_Utils; 

public class ScaledDataProvider {
 
	public static void run(String inputFilePath, String outputFilePath) throws Exception { 
		ScaledDataProvider scaledDataProvider = new ScaledDataProvider();
		scaledDataProvider.scaleData(inputFilePath, outputFilePath); 
	}
	
	public void scaleData(String inputFilePath, String outputFilePath) throws Exception {
		
		/*
		 *  Take output of trans_class.py and scale the date. Use this separately for training and test data.
		 *  Input: temp_train_file_name
		 *  Output: scaled_train_file_name
		 */
		DataSet_Utils.scaleData(inputFilePath, outputFilePath); 
	}
}
