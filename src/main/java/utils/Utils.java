package utils;

import java.io.File; 
import java.io.IOException;

import main.Configurations;
import main.FilePaths;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import data.genre.BootstrapGenres;
import data.genre.Genres;
 

public class Utils {

	private static Logger logger = Logger.getLogger(Utils.class);
	
	private static String consoleSeparator = "=======================================================================================================";
	
	public static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {

			String[] children = dir.list();
			for (String aChildren : children) {
				boolean success = deleteDir(new File(dir, aChildren));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	 
	public static void printMemory() {
		
		logger.info(":max-memory:"+(Runtime.getRuntime().maxMemory()/1024/1024)+
                           		    "m:free-memory:"+(Runtime.getRuntime().freeMemory()/1024/1024)+
                           		    "m:total"+(Runtime.getRuntime().totalMemory()/1024/1024)+"\r\n");
	}
	
	public static void logInfo(String message) { 
		System.out.println(message);
	}
	
	public static void logInfoWithoutNewline(String message) {
		System.out.print(message);
	}
	
	public static void logConsoleSeparator() { 
		System.out.print(consoleSeparator);
	}	
	
	public static void clearPreviousData() throws IOException {

		// Delete folders and their content.
		int runConfiguration = Configurations.runConfiguration;
		if (Configurations.clearInputData) {
			 
			if (runConfiguration == 1) {
				FileUtils.deleteDirectory(new File(FilePaths.trainingDataPath));
			}
			else {
				FileUtils.deleteDirectory(new File(FilePaths.parallelTrainingDataPath));
			}
			FileUtils.deleteDirectory(new File(FilePaths.testSetPath));
		} 
		FileUtils.deleteDirectory(new File(FilePaths.tmpDatSetPath));
		FileUtils.deleteDirectory(new File(FilePaths.scaledTrainingSetPath));
		FileUtils.deleteDirectory(new File(FilePaths.scaledTestSetPath)); 
		FileUtils.deleteDirectory(new File(FilePaths.modelSetPath));
		FileUtils.deleteDirectory(new File(FilePaths.resultSetPath));
        FileUtils.deleteDirectory(new File(FilePaths.scalingRangePath));

		// Recreate the folders.
		if (Configurations.clearInputData) {
			
			if (runConfiguration == 1) {
				new File(FilePaths.trainingDataPath).mkdirs();
			}
			else {
				new File(FilePaths.parallelTrainingDataPath).mkdirs();
			}
			new File(FilePaths.testSetPath).mkdirs();
		} 
		new File(FilePaths.tmpDatSetPath).mkdirs();
        new File(FilePaths.scalingRangePath).mkdirs();
        new File(FilePaths.scaledTrainingSetPath).mkdirs();
		new File(FilePaths.scaledTestSetPath).mkdirs();
		new File(FilePaths.modelSetPath).mkdirs();
		new File(FilePaths.resultSetPath).mkdirs();
	}
	
	public static Object[] getAllGenres() {
		
		if (Configurations.bootstrapMSD && Configurations.singleClassLabeling) {
			return BootstrapGenres.values();
		}
		else {
			return Genres.values();
		}
	}
}
