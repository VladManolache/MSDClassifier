package hadoop;

import hadoop.common.HadoopCommon;
import hadoop.common.HadoopDefaultJob;

import java.io.IOException;
import java.util.List;
 
import main.FilePaths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper; 
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ToolRunner;

import common.DataSplitter;
import utils.Utils; 
import data.utils.DataSet_Utils;


public class HadoopTrainingSetProvider extends HadoopCommon {

	public static class HDF5Mapper extends Mapper<NullWritable, Text, Text, IntWritable> {

		static int index = 0;
		List<String> trainingFilesPath;

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);

			index = 0;  
			trainingFilesPath = DataSplitter.getInstance().trainDatasetPath();
		}

		@Override
		public void map(NullWritable key, Text value, Context context) throws IOException, InterruptedException  {

			try {   
				List<List<String>> paritionedTrainingSet = DataSplitter.splitData(trainingFilesPath);
				DataSet_Utils.writeDataToFile(paritionedTrainingSet.get(index), FilePaths.paralel_train_fileName+"_"+(index++));  
				Utils.logInfo("Did create training file set "+index+" out of "+paritionedTrainingSet.size()+"!");
			} 
			catch (Exception e) { 
				e.printStackTrace();
			}
		}
	}
  
	@Override
	public int run(String[] arg0) throws Exception {
		super.run(arg0);

		DataSplitter.getInstance().printTotalNumberOfFiles();
 
		// Create the job specification object
		conf = new Configuration();  
		job = new HadoopDefaultJob(conf, "HadoopTrainingSetProvider");
		job.setJarByClass(HadoopTrainingSetProvider.class);
		job.configure(conf);

		// Set the Mapper and Reducer classes
		job.setMapperClass(HDF5Mapper.class); 

		// Specify input paths and type of input. 
		List<String> trainingFilesPath = DataSplitter.getInstance().trainDatasetPath();
		List<List<String>> paritionedTrainingSet = DataSplitter.splitData(trainingFilesPath);
		for (int i = 0; i < paritionedTrainingSet.size(); i++){
			List<String> currentList = paritionedTrainingSet.get(i); 
			FileInputFormat.addInputPaths(job, currentList.get(0));
		}  
		FileOutputFormat.setOutputPath(job, new Path(FilePaths.hadoopOutput));

		job.waitForCompletion(true);
 
		printJobRunTime();
		return 0;
	}  

	public static void run() throws Exception { 

		HadoopTrainingSetProvider driver = new HadoopTrainingSetProvider(); 
		ToolRunner.run(driver, new String[]{}); 
	}
}
