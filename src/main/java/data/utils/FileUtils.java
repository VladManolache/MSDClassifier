package data.utils;
 
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;  

public class FileUtils {

	public static ArrayList<String> getFileNamesList(String path) {

		final ArrayList<String> fileNamesList = new ArrayList<String>();
		
		try 
		{
		    Path startPath = Paths.get(path);  
		    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
		    	 
		        @Override
		        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		                  
		        	// If this is not a DS_FILE 
		            if(!file.getFileName().toString().equals(".DS_Store")) {
		            	  
		            	// Get the content of the file and keep it as a HDF5Object 
		            	fileNamesList.add(file.toString()); 
		            }
		             
		            return FileVisitResult.CONTINUE;
		        }

		        @Override
		        public FileVisitResult visitFileFailed(Path file, IOException e) { 
		            return FileVisitResult.CONTINUE;
		        } 
		    });
		} 
		catch (IOException e) {
		    e.printStackTrace();
		}
		
		return fileNamesList;
	} 
}
