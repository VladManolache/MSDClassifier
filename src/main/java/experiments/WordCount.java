package experiments;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
 
public class WordCount {
 
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
    	
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
 
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }
        }
    } 
 
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
    	
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            context.write(key, new IntWritable(sum));
        }
    }
 
    public static void main(String[] args) throws Exception {
    	
    	// Params check
    	if (args.length != 2) {
            System.err.println("Usage: hadoopex <input path> <output path>");
            System.exit(-1);
        }
    	
    	long startTime = System.currentTimeMillis();
    	
        Configuration conf = new Configuration(); 
        
        // Make sure to delete output folder if it already exists. 
        FileSystem fs = FileSystem.get(conf);
        fs.delete(new Path("output"), true); // delete file, true for recursive
        
        // Create the job specification object
        Job job = new Job(conf, "WordCount");
        job.setJarByClass(WordCount.class);
        
        // Specify the type of output keys and values
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
 
        // Set the Mapper and Reducer classes
        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);
 
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
  
        // Setup input and output paths
        FileInputFormat.addInputPath(job, new Path(args[0])); 
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
 
        job.waitForCompletion(true);
        
        long endTime = System.currentTimeMillis();
        System.out.println("Total time in mili seconds: "+ (endTime-startTime)); //Print the difference in mili seconds
    }   
    
}
