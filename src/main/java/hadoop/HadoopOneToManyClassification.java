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

import common.OneToManyClassifier; 


public class HadoopOneToManyClassification extends HadoopCommon {

	public static class HadoopMapper extends Mapper<NullWritable, Text, Text, IntWritable> {

		static int i = 0;
		List<String> paths;

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			super.setup(context); 

			i = 0; 
			Path[] filePaths = ((CombineFileSplit) context.getInputSplit()).getPaths();
			paths = new ArrayList<String>(); 
			for(int i = 0; i < filePaths.length; i++) {
				paths.add(filePaths[i].toUri().getPath().toString());
			}
		}

		@Override
		public void map(NullWritable key, Text value, Context context) throws IOException, InterruptedException {

			try {   
				OneToManyClassifier.run(FilePaths.scaled_test_file_name+"_0", 
						FilePaths.result_file_name+"_"+i, 
						FilePaths.model_file_name+"_"+i);
				i++; 
			} 
			catch (Exception e) { 
				e.printStackTrace();
			}  
		}
	}

	@Override
	public int run(String[] arg0) throws Exception { 
		super.run(arg0);
 
		// Create the job specification object.
		conf = new Configuration(); 
		job = new HadoopDefaultJob(conf, "HadoopOneToManyClassifier");
		job.setJarByClass(HadoopOneToManyClassification.class);
		job.configure(conf);

		// Set the Mapper and Reducer classes
		job.setMapperClass(HadoopMapper.class); 

		setupForPath(FilePaths.modelSetPath);
		
		job.waitForCompletion(true); 

		printJobRunTime(); 
		return 0;
	}

	public static void run() throws Exception { 
		HadoopOneToManyClassification driver = new HadoopOneToManyClassification(); 
		ToolRunner.run(driver, new String[]{}); 
	} 

}
