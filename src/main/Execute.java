package main;
 
import libsvm.core.svm_model;
import libsvm.core.svm_parameter;
import libsvm.core.svm_problem;
 
 
public class Execute {
 
	private svm_parameter param;		// set by parse_command_line
	private svm_problem prob;		// set by read_problem
	private svm_model model;
	private String train_file_name = "train.txt";
	private String test_file_name = "test.txt";
	private String model_file_name = "model.txt";
	private String result_file_name = "result.txt";
	 
	/*
	 *  Specify the path from which the training set will be created.
	 *  You can also specify allowed subfolders (only these folders will be opened). If this value is null,
	 *  all subfolders will be traversed and used to create the training set.
	 */
	public String h5FilesPath = "MillionSongSubset/data/A/A/A/TRAAADZ128F9348C2E.h5";
//	String[] allowedSubFolders = {"L", "M", "N"};
	String[] allowedSubFolders = {};
	
	public static void main(String[] args) throws Exception {
		
		Execute t = new Execute();
		t.run(args); 
	}
	
	private void run(String argv[]) throws Exception {
		     
		// Initialize default params
		param = SVM_Utils.getDefaultParam();  
		
		/*
		 *  Get segments for all files and convert data to LibSVM format. 
		 *  Create a training file from those results.
		 *  
		 *  Input: h5FilesPath
		 *  Output: train_file_name 
		 */
		IO_Utils.createTrainingSetFromFilesWithPath(h5FilesPath, train_file_name, allowedSubFolders);
		
		/*
		 *  Read the input from the training file and keep it as an array of nodes with indexes.
		 *  Use that array to train the svm machine and save the results in a model file.
		 *  
		 *  Input: train_file_name
		 *  Output: model_file_name
		 */
		prob = SVM_Interface.readProblem(train_file_name, param);
		model = SVM_Interface.train(train_file_name, model_file_name, prob, param);
		  
		/*
		 *  Execute prediction for test file based on the results from the training set that is saved 
		 *  in the model file and save the result in a file.
		 *  
		 *  Input: test_file_name, model_file_name, model
		 *  Output: result_file_name
		 */
		SVM_Interface.test(test_file_name, result_file_name, model_file_name, model);  
		
		/*
		 *  Visualize the results as a XY chart.
		 *  Input: result_file_name
		 */
		IO_Utils.visualiseResult(result_file_name);
	} 
	  
}
