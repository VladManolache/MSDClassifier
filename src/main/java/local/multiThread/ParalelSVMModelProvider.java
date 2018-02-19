package local.multiThread;

import java.util.List;

import common.SVMModelProvider; 
import data.utils.FileUtils;
import local.multiThread.utils.MyThreadPoolExecutor;
import main.FilePaths;

public class ParalelSVMModelProvider {

	public static void run(MyThreadPoolExecutor executor) throws Exception { 
		ParalelSVMModelProvider paralelSVMModelProvider = new ParalelSVMModelProvider();
		paralelSVMModelProvider.executeTrainModels(executor); 
	} 

	private void executeTrainModels(MyThreadPoolExecutor executor) {
 
		final List<String> inputFilesPath = FileUtils.getFileNamesList(FilePaths.scaledTrainingSetPath);
		for (int i = 0; i < inputFilesPath.size(); i++) {

			final int index = i;
			executor.runTask(() -> {
                try {
                    SVMModelProvider.run(inputFilesPath.get(index), FilePaths.model_file_name+"_"+index);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            });
		}   
	}
}
