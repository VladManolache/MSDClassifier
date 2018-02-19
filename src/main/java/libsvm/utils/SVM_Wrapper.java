package libsvm.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
 
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;
import libsvm.svm_problem;
import libsvm.tools.SVM_Predict;


public class SVM_Wrapper {
 
	public static svm_problem readProblem(String inputFileName, svm_parameter param) throws IOException {
		return SVM_Utils.read_problem(inputFileName, param);  
	}

	public static svm_model train(String inputFileName, String modelFileName, svm_problem prob, svm_parameter param) throws IOException {

		// Disable printing in console.
		svm_print_interface your_print_func = s -> { };
		svm.svm_set_print_string_function(your_print_func);
		
		svm_model theModel;  
		theModel = svm.svm_train(prob, param);  
		svm.svm_save_model(modelFileName, theModel);  
		
		return theModel;
	}

	public static void test(String inputFileName, String outputFileName, String modelFileName, svm_model model) throws IOException {
   
		BufferedReader input = new BufferedReader(new FileReader(inputFileName));
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFileName)));
		if (model == null) {
			model = svm.svm_load_model(modelFileName); 
		}   
		
		/*
		 *  Setting the last param to 1 makes libsvm also print prediction confidence.
		 *  You also need to set prediction to 1 for the svm parameter.
		 */ 
		SVM_Predict.predict(input, output, model, 1);	
		input.close();
		output.close(); 
	}

}
