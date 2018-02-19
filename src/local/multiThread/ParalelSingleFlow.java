package local.multiThread;

import java.util.List;

import utils.Utils;
import local.multiThread.utils.MyThreadPoolExecutor;
import main.Configurations;
import main.FilePaths;
import common.DataSplitter;
import common.OneToManyClassifier;
import common.SVMModelProvider;
import common.ScaledDataProvider;
import data.utils.DataSet_Utils; 


public class ParalelSingleFlow {

	public static void run(MyThreadPoolExecutor executor) throws Exception { 
		ParalelSingleFlow paralelFlow = new ParalelSingleFlow();
		paralelFlow.executeClassification(executor); 
	}
	
	private void executeClassification(MyThreadPoolExecutor executor) throws Exception {
		
		List<String> trainingFilesPath = DataSplitter.getInstance().trainDatasetPath();
		final List<List<String>> paritionedTrainingSet = DataSplitter.splitData(trainingFilesPath);
		
		// Do the first data scaling synchronously. We need to have a common scaling file, otherwise this will crash.
		if(Configurations.clearInputData) {
			List<String> currentSetPaths = paritionedTrainingSet.get(0);   
			DataSet_Utils.writeDataToFile(currentSetPaths, FilePaths.paralel_train_fileName+"_0");
			Utils.logInfo("Did create training file set ...");   
			
			List<String> testFilesPath = DataSplitter.getInstance().testDatasetPath();  
			DataSet_Utils.writeDataToFile(testFilesPath, FilePaths.test_fileName+"_0");
		} 
		ScaledDataProvider.run(FilePaths.paralel_train_fileName+"_0", FilePaths.scaled_train_file_name+"_0");
		ScaledDataProvider.run(FilePaths.test_fileName+"_0", FilePaths.scaled_test_file_name+"_0");

		// For the rest of the data, execute in paralel pipeline.
		for(int i = 0; i < paritionedTrainingSet.size(); i++) {

			final int index = i;
			executor.runTask(new Runnable() { 

				public void run() {  
					try {  
						 
						if(index > 0) {
							
							if(Configurations.clearInputData) {
								List<String> currentSetPaths = paritionedTrainingSet.get(index);   
								DataSet_Utils.writeDataToFile(currentSetPaths, FilePaths.paralel_train_fileName+"_"+index);
								Utils.logInfo("Did create training file set ...");  
							} 
							ScaledDataProvider.run(FilePaths.paralel_train_fileName+"_"+index, FilePaths.scaled_train_file_name+"_"+index);
						} 
						
						SVMModelProvider.run(FilePaths.scaled_train_file_name+"_"+index, FilePaths.model_file_name+"_"+index);  
						
						OneToManyClassifier.run(FilePaths.scaled_test_file_name+"_0", 
								FilePaths.result_file_name+"_"+index, 
								FilePaths.model_file_name+"_"+index); 
						
					} catch (Exception e) { 
						e.printStackTrace();
					}
				}
			});
		}
	}
}
