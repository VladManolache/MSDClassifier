package hadoop.common;

import java.io.IOException;
 
import main.FilePaths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import utils.Utils;

public class HadoopCommon extends Configured implements Tool {

	public static Logger log = Logger.getRootLogger(); 
	long startTime;
	long endTime;
	
	protected Configuration conf;
	protected HadoopDefaultJob job;

	@Override
	public int run(String[] arg0) throws Exception {  

		log.setLevel(Level.WARN); 

		startTime = System.currentTimeMillis();

		return 0;
	}

	protected void setupForPath(String pathString) throws IOException {
		  
		// Specify input paths and type of input.
		FileSystem fs = FileSystem.get(conf); 
		FileStatus[] status = fs.listStatus(new Path(pathString));
		for (int i = 0; i < status.length; i++){
			String currentPath = status[i].getPath().toUri().getPath();
			if(currentPath.contains(".DS_Store")) {
				continue;
			}
			FileInputFormat.addInputPaths(job, currentPath);
		}  
		FileOutputFormat.setOutputPath(job, new Path(FilePaths.hadoopOutput));
	}

	protected void printJobRunTime() {
 
		endTime = System.currentTimeMillis();
		Utils.logInfo("Total job time in miliseconds: "+ (endTime-startTime)); //Print the difference in miliseconds	
	}
}
