package local.singleThread;

import java.io.InputStream;
 
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import utils.Utils;

public class LabelCombining {
 
	public static void run(String trainFilePath, String testFilePath) {
		LabelCombining labelCombining = new LabelCombining();
		labelCombining.executeLabelCombining(trainFilePath, testFilePath); 
	}
	
	public void executeLabelCombining(String trainFilePath, String testFilePath) {
		       
		Utils.logConsoleSeparator();
		Utils.logInfo("\n    Combining multi-label classes ...\n");
		
		String scriptName = "jython/trans_class.py";
		PySystemState state = new PySystemState(); 
		state.argv.clear();
		state.argv.append(new PyString(scriptName));
		state.argv.append(new PyString(trainFilePath + "_0"));
		state.argv.append(new PyString(testFilePath + "_0"));

		PythonInterpreter interpreter = new PythonInterpreter(null, state);
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(scriptName);
		interpreter.execfile(is);
		Utils.logInfo("\nDid combine labels!"); 
	}  
}
