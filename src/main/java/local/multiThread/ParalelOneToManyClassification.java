package local.multiThread;

import java.util.List;

import common.OneToManyClassifier; 
import data.utils.FileUtils;
import local.multiThread.utils.MyThreadPoolExecutor;
import main.FilePaths;

public class ParalelOneToManyClassification {
 
	public static void run(MyThreadPoolExecutor executor) throws Exception { 
		ParalelOneToManyClassification paralelOneToManyClassification = new ParalelOneToManyClassification();
		paralelOneToManyClassification.executeClassification(executor); 
	}
	
	private void executeClassification(MyThreadPoolExecutor executor) {
		
		final List<String> inputFilesPath = FileUtils.getFileNamesList(FilePaths.modelSetPath);
		for (int i = 0; i < inputFilesPath.size(); i++) {

			final int index = i;
			executor.runTask(() -> {
                try {
                    OneToManyClassifier.run(FilePaths.scaled_test_file_name+"_0",
                            FilePaths.result_file_name+"_"+index,
                            FilePaths.model_file_name+"_"+index);					}
                catch(Exception e) {
                    e.printStackTrace();
                }
            });
		}   
	}
}
