package libsvm.utils;
 
import libsvm.svm_parameter;
import main.Configurations;

public class ParameterDecision {

	public static svm_parameter getSelectedPreset() {
		
		int preset = Configurations.SVMPreset; 
		int cost = 500;
		
		svm_parameter param = null;
		if (preset == 0) {
			param = getDefaultParam();
		}
		else if (preset == 1) {
			param = getLinearKernel(cost);
		}
		else if (preset == 2) {
			param = getPOLYKernel(cost);
		}
		else if (preset == 3) {
			param = getRBFKernel(cost);
		}
		else if (preset == 4) {
			param = getSigmoidKernel(cost);
		}
		
		return param;
	}
	
	public static svm_parameter getParameters(int svm_type, int kernel_type, int cost) {
		
		svm_parameter param = getDefaultParam();
		param.svm_type = svm_type;
		param.kernel_type = kernel_type;
		param.C = cost;
		
		return param;
	}
	
	private static svm_parameter getDefaultParam() {
		 
		svm_parameter param = new svm_parameter(); 
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0]; 
		
		return param;
	}
	
	private static svm_parameter getLinearKernel(int cost) {
		 
		svm_parameter param = getDefaultParam();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.LINEAR; 
		param.C = cost;  
		return param;
	}
	
	private static svm_parameter getPOLYKernel(int cost) {
		 
		svm_parameter param = getDefaultParam();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.POLY; 
		param.C = cost;  
		return param;
	}
	
	private static svm_parameter getRBFKernel(int cost) {
		 
		svm_parameter param = getDefaultParam();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF; 
		param.C = cost;  
		return param;
	}
	
	private static svm_parameter getSigmoidKernel(int cost) {
		 
		svm_parameter param = getDefaultParam();
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.SIGMOID; 
		param.C = cost;  
		return param;
	}
	
}
