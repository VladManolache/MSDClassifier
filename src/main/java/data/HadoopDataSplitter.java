package data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HadoopDataSplitter {
 
	  
	public static List<String> getFilesAtPath(String path) throws IOException { 
		
		List<String> bigList = new ArrayList<String>();
		
		Configuration conf = new Configuration(); 
		FileSystem fs = FileSystem.get(conf); 
 
		FileStatus[] status = fs.listStatus(new Path(path));
		for (int i = 0; i < status.length; i++){
			String currentPath = status[i].getPath().toUri().getPath();
			if(currentPath.contains(".DS_Store")) {
				continue;
			}
			bigList.add(currentPath); 
		} 
		
		return bigList;
	} 
}
