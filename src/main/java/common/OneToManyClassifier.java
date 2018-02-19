package common;
  
import libsvm.utils.SVM_Wrapper; 


public class OneToManyClassifier {

	public static void run(String scaledTestFilePath, String resultsFilePath, String modelFilePath) throws Exception {
		
		OneToManyClassifier oneToManyClassifier = new OneToManyClassifier(); 
		oneToManyClassifier.executeClassification(scaledTestFilePath, resultsFilePath, modelFilePath);
	} 

	private void executeClassification(String scaledTestFilePath, String resultsFilePath, String modelFilePath) throws Exception {
		
		/*
		 *  Execute prediction for test file based on the results from the training set that is saved 
		 *  in the model file and save the result in a file.
		 *  
		 *  Input: test_file_name, model_file_name, model
		 *  Output: result_file_name
		 */
		SVM_Wrapper.test(scaledTestFilePath, resultsFilePath, modelFilePath, null);  
	}
	 
}
