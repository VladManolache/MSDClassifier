package local.multiThread;

import java.util.List;

import local.multiThread.utils.MyThreadPoolExecutor;
import main.FilePaths;
import common.ScaledDataProvider;
import data.utils.FileUtils;

public class ParalelDataScaling {

	public static void run(MyThreadPoolExecutor executor) throws Exception { 
		ParalelDataScaling dataRetriever = new ParalelDataScaling();
		dataRetriever.scaleData(executor); 
	} 
	
	public void scaleData(MyThreadPoolExecutor executor) throws Exception {
		  
		final List<String> inputFilesPath = FileUtils.getFileNamesList(FilePaths.paralelTrainingDataPath);
		
		// Need to scale the first file in order to get the scaling map to be used for scaling subsequent data.
		ScaledDataProvider.run(inputFilesPath.get(0), FilePaths.scaled_train_file_name+"_"+0);

		for(int i = 1; i < inputFilesPath.size(); i++) {

			final int index = i;
			executor.runTask(() -> {
                try {
                    ScaledDataProvider.run(inputFilesPath.get(index), FilePaths.scaled_train_file_name+"_"+index);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            });
		} 
		
		// Scale training data.
		executor.runTask(() -> {
            try {
                ScaledDataProvider.run(FilePaths.testSetPath+"/testData_0", FilePaths.scaled_test_file_name+"_0");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
	}
}
