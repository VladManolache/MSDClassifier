package main;

public class FilePaths {

	/*
	 *  Specify the path from which the training set will be created.
	 *  You can also specify allowed subfolders (only these folders will be opened). If this value is null,
	 *  all subfolders will be traversed and used to create the training set.
	 */
	public final static String trainingDataPath = Configurations.outputPath + "TrainingSet";
	public final static String parallelTrainingDataPath = Configurations.outputPath + "ParallelTrainingSet";
	public final static String scaledTrainingSetPath = Configurations.outputPath + "ScaledTrainingSet";
	public final static String testSetPath = Configurations.outputPath + "TestSet";
	public final static String scaledTestSetPath = Configurations.outputPath + "ScaledTestSet";
	public final static String tmpDatSetPath = Configurations.outputPath + "TmpSet";
	public final static String modelSetPath = Configurations.outputPath + "ModelSet";
	public final static String scalingRangePath = Configurations.outputPath + "DataRange";
	public final static String hadoopOutput = Configurations.outputPath + "Output";
	public final static String resultSetPath = Configurations.outputPath + "ResultSet";
	
	/*
	 * All file paths related to SVM and Hadoop execution.
	 */ 
	public final static String train_fileName = trainingDataPath + "/trainingData";
	public final static String paralel_train_fileName = parallelTrainingDataPath + "/trainingData";
	public final static String test_fileName = testSetPath + "/testData";
	
	public final static String temp_train_file_name = tmpDatSetPath + "/tmp_train";
	public final static String temp_test_file_name = tmpDatSetPath + "/tmp_test";
	public final static String temp_scaling_file_name = tmpDatSetPath + "/tmp_class";
 
	public final static String scaled_train_file_name = scaledTrainingSetPath + "/scaledTrainingData";
	public final static String scaled_test_file_name = scaledTestSetPath + "/scaledTestData";
	public final static String scaling_range_file_name = scalingRangePath + "/range.txt";
	
	public final static String model_file_name = modelSetPath + "/model";
	public final static String result_file_name = resultSetPath + "/result";
	public final static String resulTarget_file_name = resultSetPath + "/resultTarget";
	public final static String resultAggregate_file_name = resultSetPath + "/resultAggregate";
	
}
