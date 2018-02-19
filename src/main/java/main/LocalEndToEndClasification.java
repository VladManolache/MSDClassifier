package main;

import hadoop.HadoopOneToManyClassification;
import hadoop.HadoopSingleFlow;
import hadoop.HadoopSVMModelProvider;
import hadoop.HadoopScaledTrainingDataProvider;
import hadoop.HadoopTrainingSetProvider;

import java.util.concurrent.TimeUnit;

import local.AccuracyCalculating;
import local.multiThread.ParalelDataRetriever;
import local.multiThread.ParalelDataScaling;
import local.multiThread.ParalelOneToManyClassification;
import local.multiThread.ParalelSingleFlow;
import local.multiThread.ParalelSVMModelProvider;
import local.multiThread.utils.MyThreadPoolExecutor;
import local.singleThread.HDF5DataRetriever;
import local.singleThread.LabelCombining;
import local.voting.WeightedMajorityVoting;
import utils.DifferenceChart;
import utils.PieChartResultsVisualiser;
import utils.ResultsData;
import utils.Utils;
import common.DataSplitter;
import common.OneToManyClassifier;
import common.SVMModelProvider;
import common.ScaledDataProvider;
import data.utils.DataSet_Utils;

/**
 * Note:
 * - Need VM Option: -Djava.library.path=libs/
 */
public class LocalEndToEndClasification {

	public static void main(String[] args) throws Exception { 
		LocalEndToEndClasification t = new LocalEndToEndClasification();
		t.run(args); 
	}

	private void run(String argv[]) throws Exception {

		Utils.clearPreviousData(); 
		long startTime;
 
		int runConfiguration = Configurations.runConfiguration;
		if (runConfiguration == 1) {

			if (Configurations.clearInputData) {
				Utils.logConsoleSeparator();
				Utils.logInfo("\n    Generating training and test data sets ...");

				// Convert HDF5 files to input training and test data.
				HDF5DataRetriever.run();
			}

			startTime = System.currentTimeMillis();

			LabelCombining.run(FilePaths.train_fileName, FilePaths.test_fileName);

			// Mapped data scaling => final input and test files.
			Utils.logConsoleSeparator();
			Utils.logInfo("\n    Scaling data ...");
			Utils.logInfo("\nScaling training data ...");
			ScaledDataProvider.run(FilePaths.temp_train_file_name, FilePaths.scaled_train_file_name+"_0");
			Utils.logInfo("Scaling test data ...");
			ScaledDataProvider.run(FilePaths.temp_test_file_name, FilePaths.scaled_test_file_name+"_0");
			Utils.logInfo("Did scale data!");

			long endTime = System.currentTimeMillis();
			Utils.logInfo("\nScaling time: "+ (endTime-startTime)); //Print the difference in miliseconds

			startTime = System.currentTimeMillis();

			// SVM model generation.
			Utils.logConsoleSeparator();
			Utils.logInfo("\n    Building model from training file ...\n");
			SVMModelProvider.run(FilePaths.scaled_train_file_name+"_0", FilePaths.model_file_name+"_0");
			Utils.logInfo("Did build model!");

			endTime = System.currentTimeMillis();
			Utils.logInfo("\nTraining time: "+ (endTime-startTime)); //Print the difference in miliseconds

			startTime = System.currentTimeMillis();

			// SVM classification.
			Utils.logConsoleSeparator();
			Utils.logInfo("\n    Classifying data from test file ...\n");
			OneToManyClassifier.run(FilePaths.scaled_test_file_name+"_0", FilePaths.result_file_name+"_0", FilePaths.model_file_name+"_0");
			Utils.logInfo("\nDid classify data!");

			endTime = System.currentTimeMillis();
			Utils.logConsoleSeparator();
			Utils.logInfo("\nTest time in miliseconds: "+ (endTime-startTime)+"\n"); //Print the difference in miliseconds

			ResultsData.run(FilePaths.result_file_name+"_0");
			AccuracyCalculating.calculateAndPrintAccuracyStatistics(main.FilePaths.result_file_name+"_0");
			PieChartResultsVisualiser.run(FilePaths.result_file_name+"_0", true);
			PieChartResultsVisualiser.run(FilePaths.result_file_name+"_0", false);
			DifferenceChart.run(main.FilePaths.result_file_name+"_0");
		}
		else if (runConfiguration == 2) {

			if (Configurations.clearInputData) {
				Utils.logConsoleSeparator();
				Utils.logInfo("\n    Generating training and test data sets ...");

				// Convert HDF5 files to input training and test data.
				HadoopTrainingSetProvider.run();
				DataSet_Utils.writeDataToFile(DataSplitter.getInstance().trainDatasetPath(), FilePaths.test_fileName+"_0");

				Utils.logInfo("\nDid create test file ...");
				Utils.logInfo("Did generate trainning and test files!");
			}

			startTime = System.currentTimeMillis();

			Utils.logConsoleSeparator();
			Utils.logInfo("\n    Scaling data ...");
			Utils.logInfo("\nScaling training data ...");
			HadoopScaledTrainingDataProvider.run();
			Utils.logInfo("Scaling test data ...");
			ScaledDataProvider.run(FilePaths.testSetPath+"/testData_0", FilePaths.scaled_test_file_name+"_0");
			Utils.logInfo("Did scale data!\n");

			Utils.logConsoleSeparator();
			Utils.logInfo("\n    Building model from training file ...\n");
			HadoopSVMModelProvider.run();
			Utils.logInfo("\nDid build model!");

			Utils.logConsoleSeparator();
			Utils.logInfo("\n    Classifying data from test file ...\n");
			HadoopOneToManyClassification.run();
			Utils.logInfo("\nDid classify data!");

			startTime = System.currentTimeMillis();

//			MajorityVoting.run();

			WeightedMajorityVoting.run();

			long endTime = System.currentTimeMillis();
			Utils.logInfo("\nResults combining time: "+ (endTime-startTime)); //Print the difference in miliseconds

			Utils.logConsoleSeparator();
			endTime = System.currentTimeMillis();
			Utils.logInfo("\nFinal run time in miliseconds: "+ (endTime-startTime)); //Print the difference in miliseconds

			AccuracyCalculating.calculateAndPrintAccuracyStatistics(FilePaths.resultAggregate_file_name);
			double accuracy = AccuracyCalculating.calculateTotalAccuracy(FilePaths.resultAggregate_file_name);
			System.out.println("\nFinal accuracy = "+accuracy+"%\n");

			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, true);
			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, false);
		}
		else if (runConfiguration == 3) {

			startTime = System.currentTimeMillis();

			HadoopSingleFlow.run();

			WeightedMajorityVoting.run();

			AccuracyCalculating.calculateAndPrintAccuracyStatistics(FilePaths.resultAggregate_file_name);
			double accuracy = AccuracyCalculating.calculateTotalAccuracy(FilePaths.resultAggregate_file_name);
			System.out.println("\nFinal accuracy = "+accuracy+"%\n");

			Utils.logConsoleSeparator();
			long endTime = System.currentTimeMillis();
			Utils.logInfo("\nFinal run time in miliseconds: "+ (endTime-startTime)); //Print the difference in miliseconds

			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, true);
			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, false);
		}
		else if (runConfiguration == 4) {

			startTime = System.currentTimeMillis();

			DataSplitter.getInstance();	// Pre-initialize data split.

			MyThreadPoolExecutor executor = new MyThreadPoolExecutor();

			// Retrieve both training and test data.
			if (Configurations.clearInputData) {
				Utils.logConsoleSeparator();
				Utils.logInfo("\n    Generating training and test data sets ...");

				// Convert HDF5 files to input training and test data.
				ParalelDataRetriever.run(executor);
				executor.shutDown();
				while (!executor.threadPool.awaitTermination(100, TimeUnit.MILLISECONDS));

				Utils.logInfo("\nDid create test file ...");
				Utils.logInfo("Did generate trainning and test files!");
			}

			// Scale both training and test data.
			executor = new MyThreadPoolExecutor();
			ParalelDataScaling.run(executor);
			executor.shutDown();
			while (!executor.threadPool.awaitTermination(100, TimeUnit.MILLISECONDS));

			// Train models from scaled training data.
			executor = new MyThreadPoolExecutor();
			ParalelSVMModelProvider.run(executor);
			executor.shutDown();
			while (!executor.threadPool.awaitTermination(100, TimeUnit.MILLISECONDS));

			// Execute data classification.
			executor = new MyThreadPoolExecutor();
			ParalelOneToManyClassification.run(executor);
			executor.shutDown();
			while (!executor.threadPool.awaitTermination(100, TimeUnit.MILLISECONDS));

			WeightedMajorityVoting.run();

			AccuracyCalculating.calculateAndPrintAccuracyStatistics(FilePaths.resultAggregate_file_name);
			double accuracy = AccuracyCalculating.calculateTotalAccuracy(FilePaths.resultAggregate_file_name);
			System.out.println("\nFinal accuracy = "+accuracy+"%\n");

			Utils.logConsoleSeparator();
			long endTime = System.currentTimeMillis();
			Utils.logInfo("\nFinal run time in miliseconds: "+ (endTime-startTime)); //Print the difference in miliseconds

			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, true);
			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, false);
		}
		else if (runConfiguration == 5) {

			startTime = System.currentTimeMillis();

			DataSplitter.getInstance();	// Pre-initialize data split.

			MyThreadPoolExecutor executor = new MyThreadPoolExecutor();

			executor = new MyThreadPoolExecutor();
			ParalelSingleFlow.run(executor);
			executor.shutDown();
			while (!executor.threadPool.awaitTermination(100, TimeUnit.MILLISECONDS));

			WeightedMajorityVoting.run();

			AccuracyCalculating.calculateAndPrintAccuracyStatistics(FilePaths.resultAggregate_file_name);
			double accuracy = AccuracyCalculating.calculateTotalAccuracy(FilePaths.resultAggregate_file_name);
			System.out.println("\nFinal accuracy = "+accuracy+"%\n");

			Utils.logConsoleSeparator();
			long endTime = System.currentTimeMillis();
			Utils.logInfo("\nFinal run time in miliseconds: "+ (endTime-startTime)); //Print the difference in miliseconds

			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, true);
			PieChartResultsVisualiser.run(FilePaths.resultAggregate_file_name, false);
		}
	}  
}
