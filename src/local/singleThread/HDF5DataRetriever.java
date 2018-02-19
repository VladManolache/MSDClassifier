package local.singleThread;
 
import java.util.List;

import common.DataSplitter; 
import main.FilePaths;
import utils.Utils;
import data.utils.DataSet_Utils;


public class HDF5DataRetriever {

	public static void run() throws Exception {
		
		HDF5DataRetriever dataRetriever = new HDF5DataRetriever();
		dataRetriever.retrieveData(); 
	}
	
	public void retrieveData() throws Exception {
   
		DataSplitter.getInstance().printTotalNumberOfFiles();
		 
		// HDF5 -> training data set.
		List<String> trainingFilesPath = DataSplitter.getInstance().trainDatasetPath();  
		DataSet_Utils.writeDataToFile(trainingFilesPath, FilePaths.train_fileName+"_0");
		Utils.logInfo("Did create training file set ..."); 
		
		// HDF5 -> test data set.
		List<String> testFilesPath = DataSplitter.getInstance().testDatasetPath();  
		DataSet_Utils.writeDataToFile(testFilesPath, FilePaths.test_fileName+"_0");
		Utils.logInfo("Did create test file ..."); 
		 
		Utils.logInfo("\nDid generate trainning and test files!");  
	}
}
