package libsvm.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector; 

import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;


public class SVM_Utils {
 
	public static svm_problem read_problem(String input_file_name, svm_parameter param) throws IOException {
		
		BufferedReader fp = new BufferedReader(new FileReader(input_file_name));
		Vector<Double> vy = new Vector<>();
		Vector<svm_node[]> vx = new Vector<>();
		int max_index = 0;

		while (true)
		{
			String line = fp.readLine();
			if(line == null) break;

			StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");

			vy.addElement(atof(st.nextToken()));
			int m = st.countTokens()/2;
			svm_node[] x = new svm_node[m];
			for(int j = 0; j < m; j++) { 
				x[j] = new svm_node();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}
			if(m > 0) max_index = Math.max(max_index, x[m-1].index);
			vx.addElement(x);
		}

		svm_problem prob = new svm_problem();
		prob.l = vy.size();
		prob.x = new svm_node[prob.l][];
		for(int i=0;i<prob.l;i++)
			prob.x[i] = vx.elementAt(i);
		prob.y = new double[prob.l];
		for(int i=0;i<prob.l;i++)
			prob.y[i] = vy.elementAt(i);

		if(param.gamma == 0 && max_index > 0)
			param.gamma = 1.0/max_index;

		if(param.kernel_type == svm_parameter.PRECOMPUTED) {
		
			for(int i=0;i<prob.l;i++) { 
				
				if (prob.x[i][0].index != 0) {
					
					System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
					System.exit(1);
				}
				if ((int)prob.x[i][0].value <= 0 || (int)prob.x[i][0].value > max_index) {
					
					System.err.print("Wrong input format: sample_serial_number out of range\n");
					System.exit(1);
				}
			}
		}

		fp.close();
		
//		System.out.println("Problem size: "+vx.size()+" nodes \n");
		
		return prob;
	}
	
	public static double atof(String s) {

		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return(d);
	}

	public static int atoi(String s) {

		return Integer.parseInt(s);
	}
	
}
