package local.multiThread;

import java.util.List;
 
import local.multiThread.utils.MyThreadPoolExecutor;
import main.FilePaths;
import utils.Utils;
import common.DataSplitter;
import data.utils.DataSet_Utils;

public class ParalelDataRetriever {

	public static void run(MyThreadPoolExecutor executor) throws Exception { 
		ParalelDataRetriever dataRetriever = new ParalelDataRetriever();
		dataRetriever.retrieveData(executor); 
	}
	
	public void retrieveData(MyThreadPoolExecutor executor) throws Exception {

		List<String> trainingFilesPath = DataSplitter.getInstance().trainDatasetPath();
		final List<List<String>> paritionedTrainingSet = DataSplitter.splitData(trainingFilesPath);
		for (int i = 0; i < paritionedTrainingSet.size(); i++) {

			final int index = i;
			executor.runTask(() -> {
                try {
                    List<String> currentSetPaths = paritionedTrainingSet.get(index);
                    DataSet_Utils.writeDataToFile(currentSetPaths, FilePaths.paralel_train_fileName+"_"+index);
                    Utils.logInfo("Did create training file set ...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
		}

		executor.runTask(() -> {
            try {
                List<String> testFilesPath = DataSplitter.getInstance().testDatasetPath();
                DataSet_Utils.writeDataToFile(testFilesPath, FilePaths.test_fileName+"_0");
                Utils.logInfo("Did create test file ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	} 
}
