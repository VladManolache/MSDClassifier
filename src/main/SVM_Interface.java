package main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import libsvm.core.svm;
import libsvm.core.svm_model;
import libsvm.core.svm_parameter;
import libsvm.core.svm_print_interface;
import libsvm.core.svm_problem;
import libsvm.sample.svm_predict;

public class SVM_Interface {

	public static String consoleSeparator = "=======================================================================================================";
	
	public static svm_problem readProblem(String inputFileName, svm_parameter param) throws IOException {
 
		System.out.println(consoleSeparator);
		System.out.println("\n    Reading problem ...\n");
		
		return SVM_Utils.read_problem(inputFileName, param);  
	}

	public static svm_model train(String inputFileName, String modelFileName, svm_problem prob, svm_parameter param) throws IOException {

		System.out.println(consoleSeparator);
		System.out.println("\n    Building model from training file ...\n");
		
		// Disable printing in console.
		svm_print_interface your_print_func = new svm_print_interface() { 
			
			public void print(String s) { 
				
			}
		};
		svm.svm_set_print_string_function(your_print_func);
		
		svm_model theModel; 
		theModel = svm.svm_train(prob, param);
		svm.svm_save_model(modelFileName, theModel);
		return theModel;
	}

	public static void test(String inputFileName, String outputFileName, String modelFileName, svm_model model) throws IOException {
 
		System.out.println(consoleSeparator);
		System.out.println("\n    Classifying data from test file...\n");
		
		BufferedReader input = new BufferedReader(new FileReader(inputFileName));
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFileName)));
		if(model == null) {
			model = svm.svm_load_model(modelFileName);
		}  
		svm_predict.predict(input, output, model, 0);
		input.close();
		output.close();
	}

}
