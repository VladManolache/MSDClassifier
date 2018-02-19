package data.utils;

import data.genre.BootstrapMSD;
import data.genre.GenreDeciding; 
import data.hdf5.HDF5FileData;
import data.hdf5.HDF5_Utils; 

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import libsvm.tools.SVM_Scale;
import main.Configurations;
import main.FilePaths;

public class DataSet_Utils {

	public static void writeDataToFile(List<String> fileList, String outputFileName) throws Exception {
  
		HDF5FileData fileSegmentsObject;  
		PrintWriter writer = new PrintWriter(outputFileName);  
		
		// Only get bootstrap data if we are going to use it.
		HashMap<String, String> bootstrap = new HashMap<String, String>();
		if (Configurations.bootstrapMSD) {
			bootstrap = BootstrapMSD.getBootstrapData(); 
		} 
		
		ArrayList<String> genres;
		for (String aFileList : fileList) {

			fileSegmentsObject = HDF5_Utils.getFileData(aFileList);
			if (fileSegmentsObject == null) {
				continue;
			}

			/*
			 *  Decide the genre of the current song.
			 *  Either use the data provided in MSD (the artist genre tags) or bootstrap MSD with each song's genre tag.
			 *  If no valid genre tags are detected, skip to the next song.
			 */
			genres = new ArrayList<>();
			if (!Configurations.bootstrapMSD) {
				genres = GenreDeciding.getGenreTags(fileSegmentsObject.tags);
			} else {
				genres = GenreDeciding.getGenreTagAfterBootstrappingForSong(fileSegmentsObject, bootstrap);
			}

			// Skip this song if there are no valid genre tags present.
			if (genres.size() == 0) {
				continue;
			}

			/*
			 *  Output the class of the current song.
			 *  This is required for training data, but optional for test data.
			 *  If it is provided for test data, it will be used to determine accuracy.
			 */
			writer.write(genres.get(0));
			for (int j = 1; j < genres.size(); j++) {
				writer.write("," + genres.get(j));
			}
			writer.write(" ");

			// Select features to use for classification.
			ArrayList<String> features = new ArrayList<String>();
			int preset = Configurations.featureSelectionPreset;
			if (preset == 1) {
				addSegmentLevelData(features, fileSegmentsObject);

				addBarsSectionsTatumsData(features, fileSegmentsObject);

				addSongLevelData(features, fileSegmentsObject);
			} else if (preset == 2) {
				addSegmentLevelData(features, fileSegmentsObject);
			}

			writer.write("1:" + features.get(0));
			for (int j = 1; j < features.size(); j++) {
				writer.write(" " + (j + 1) + ":" + features.get(j));
			}

			writer.println();
		}  
		writer.flush();
		writer.close();   
	} 
	
	private static ArrayList<String> addSegmentLevelData(ArrayList<String> list, HDF5FileData fileSegmentsObject) {
		
		list.add(Statistics.getMean(fileSegmentsObject.segments_timbre)+"");
//		list.add(Statistics.getStdDev(fileSegmentsObject.segments_timbre)+"");
		list.add(Statistics.getMean(fileSegmentsObject.segments_pitch)+"");
//		list.add(Statistics.getStdDev(fileSegmentsObject.segments_pitch)+"");
		list.add(Statistics.getMean(fileSegmentsObject.segments_loudness_max)+"");
//		list.add(Statistics.getStdDev(fileSegmentsObject.segments_loudness_max)+""); 
//		list.add(Statistics.getStdDev(fileSegmentsObject.segments_loudness_max_time)+"");
//		list.add(Statistics.getStdDev(fileSegmentsObject.bars_start)+"");
//		list.add(Statistics.getStdDev(fileSegmentsObject.beats_start)+"");
//		list.add(Statistics.getStdDev(fileSegmentsObject.tatums_start)+"");
//		list.add(Statistics.getStdDev(fileSegmentsObject.sections_start)+"");
		return list;
	}
	
	private static ArrayList<String> addSongLevelData(ArrayList<String> list, HDF5FileData fileSegmentsObject) { 
		list.add(fileSegmentsObject.danceability + "");
		list.add(fileSegmentsObject.duration + "");
		list.add(fileSegmentsObject.key_confidence + "");
		list.add(fileSegmentsObject.loudness + "");
		list.add(fileSegmentsObject.mode + "");
		list.add(fileSegmentsObject.mode_confidence + "");
		list.add(fileSegmentsObject.tempo + "");
		list.add(fileSegmentsObject.time_signature + "");
		list.add(fileSegmentsObject.time_signature_confidence + "");
		return list;
	}
	
	private static ArrayList<String> addBarsSectionsTatumsData(ArrayList<String> list, HDF5FileData fileSegmentsObject) {
		
		list.add(Statistics.getMean(fileSegmentsObject.bars_confidence) + "");
		list.add(Statistics.getStdDev(fileSegmentsObject.bars_confidence) + "");
		list.add(Statistics.getMean(fileSegmentsObject.beats_confidence) + "");
		list.add(Statistics.getStdDev(fileSegmentsObject.beats_confidence) + "");
		list.add(Statistics.getMean(fileSegmentsObject.sections_confidence) + "");
		list.add(Statistics.getStdDev(fileSegmentsObject.sections_confidence) + "");
		list.add(Statistics.getMean(fileSegmentsObject.tatums_confidence) + "");
		list.add(Statistics.getStdDev(fileSegmentsObject.tatums_confidence) + "");
		return list;
	}	
	
	public static void scaleData(String oldInputFileName, String newScaledDataFileName) throws IOException {
		
		String[] argv;
		File scalingData = new File(FilePaths.scaling_range_file_name);
		if (scalingData.exists()) {
			argv = new String[]{"-l", "0", "-u", "1", "-r", FilePaths.scaling_range_file_name, "-i", oldInputFileName, "-o", newScaledDataFileName};
		} 
		else {
			argv = new String[]{"-l", "0", "-u", "1", "-s", FilePaths.scaling_range_file_name, "-i", oldInputFileName, "-o", newScaledDataFileName};
		}
		SVM_Scale scale = new SVM_Scale();
		scale.run(argv);
	} 
}
