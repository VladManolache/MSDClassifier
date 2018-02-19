package hadoop;

import hadoop.common.HadoopCommon;
import hadoop.common.HadoopDefaultJob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
import main.FilePaths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.hadoop.util.ToolRunner;

import common.ScaledDataProvider; 


public class HadoopScaledTrainingDataProvider extends HadoopCommon {
 
	public static class HadoopMapper extends Mapper<NullWritable, Text, Text, IntWritable> {

		static int i = 0;
		List<String> paths; 
		
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);

			i = 0; 
			Path[] filePaths = ((CombineFileSplit) context.getInputSplit()).getPaths();
			paths = new ArrayList<>();
			for (Path filePath : filePaths) {
				paths.add(filePath.toUri().getPath().toString());
			}
		}

		@Override
		public void map(NullWritable key, Text value, Context context) throws IOException, InterruptedException {

			try {   
				ScaledDataProvider.run(paths.get(i), FilePaths.scaled_train_file_name+"_"+(i++));
			} 
			catch (Exception e) { 
				e.printStackTrace();
			}  
		}
	}

	@Override
	public int run(String[] argv) throws Exception {
		super.run(argv);  
		 
		// Create the job specification object
		conf = new Configuration(); 
		job = new HadoopDefaultJob(conf, "HadoopScaledDataProvider");
		job.setJarByClass(HadoopScaledTrainingDataProvider.class);
		job.configure(conf);

		// Set the Mapper and Reducer classes
		job.setMapperClass(HadoopMapper.class); 
  
		setupForPath(FilePaths.parallelTrainingDataPath);

		job.waitForCompletion(true);

		printJobRunTime();
		return 0;
	}
	
	public static void run() throws Exception { 
		HadoopScaledTrainingDataProvider driver = new HadoopScaledTrainingDataProvider(); 
		ToolRunner.run(driver, new String[]{});    
	} 
}
