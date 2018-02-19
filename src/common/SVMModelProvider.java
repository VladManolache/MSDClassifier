package common;
 
import libsvm.svm_parameter;
import libsvm.svm_problem;
import libsvm.utils.ParameterDecision;
import libsvm.utils.SVM_Wrapper; 
 

public class SVMModelProvider {
	
	private svm_parameter param;		// set by parse_command_line
	private svm_problem prob;		// set by read_problem 
	
	public static void run(String scaled_trainFilePath, String modelFilePath) throws Exception {
		
		SVMModelProvider modelProvider = new SVMModelProvider(); 
		modelProvider.createModel(scaled_trainFilePath, modelFilePath);
	} 

	public void createModel(String scaled_trainFilePath, String modelFilePath) throws Exception {

		/*
		 *  Initialize default params. Setting probability to 1 makes libsvm also print prediction confidence.
		 *  Besides setting this here, you also need to set it when calling predict.
		 */
		param = ParameterDecision.getSelectedPreset();   
		param.probability = 1;	
		
		/*
		 *  Read the input from the training file and keep it as an array of nodes with indexes.
		 *  Use that array to train the svm machine and save the results in a model file.
		 *  
		 *  Input: train_file_name
		 *  Output: model_file_name
		 */
		prob = SVM_Wrapper.readProblem(scaled_trainFilePath, param);
		SVM_Wrapper.train(scaled_trainFilePath, modelFilePath, prob, param);  
	} 
}

