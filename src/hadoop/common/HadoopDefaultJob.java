package hadoop.common;

import hadoop.custominputformat.WholeFileInputFormat;

import java.io.IOException;
 
import main.FilePaths;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class HadoopDefaultJob extends Job {
 
	public HadoopDefaultJob(Configuration conf, String name) throws IOException {
		super(conf, name); 
	}

	public void configure(Configuration conf) throws IOException {

		// Specify the type of output keys and values
		setOutputKeyClass(Text.class);
		setOutputValueClass(IntWritable.class);
		setInputFormatClass(WholeFileInputFormat.class);
		setOutputFormatClass(TextOutputFormat.class);
 
		// Specify results path and clear it if it already exists.
		FileSystem fs = FileSystem.get(conf);
		fs.delete(new Path(FilePaths.hadoopOutput), true); // delete file, true for recursive
	}
}
