package main;

import hdf5.HDF5FileSegments;
import hdf5.HDF5_Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.la4j.vector.Vector;
import org.la4j.vector.Vectors;
import org.la4j.vector.sparse.CompressedVector;

import utils.ResultsVisualiser;

public class IO_Utils {

	public static void createTrainingSetFromFilesWithPath(String path,
			String outputFileName, String[] allowedSubFolders) throws Exception {

		System.out.println(SVM_Interface.consoleSeparator);
		System.out.println("\n    Creating training file ... \n");
		
		// Get list of files at provided path  
		ArrayList<String> fileList = HDF5_Utils.getFileNamesList(path, allowedSubFolders);
		System.out.println("Number of files: "+fileList.size()+"\n");
		
		// Get song segments from each h5 file in the list.
		ArrayList<HDF5FileSegments>fileSegmentsList = HDF5_Utils.getFileSegmentsList(fileList);   
		System.out.println("Number of segments: "+fileSegmentsList.size()*fileSegmentsList.get(0).segments_confidence.length +"\n");
		
		// Create a 'sparse' training set from the provided segments and write it in a file.
		createInputFileFromSegments(fileSegmentsList, outputFileName);
		System.out.println("Did create input file!\n");
	}

	private static void createInputFileFromSegments(ArrayList<HDF5FileSegments> fileSegmentsList, String outputFileName) throws FileNotFoundException {

		HDF5FileSegments fileSegmentsObject;
		double normalizer;  
		double[] values;
		Vector compressedVector;
		Vector normalizedValues;

		PrintWriter writer = new PrintWriter(outputFileName);  
		for(int i = 0; i < fileSegmentsList.size(); i++) {

			fileSegmentsObject = fileSegmentsList.get(i); 
			for(int j = 0; j < fileSegmentsObject.segments_confidence.length; j++) {

				writer.write((j+1)+" ");

				values = new double[6];
				values[0] = fileSegmentsObject.segments_confidence[j];
				values[1] = fileSegmentsObject.segments_loudness_max[j];
				values[2] = fileSegmentsObject.segments_loudness_max_time[j];
				values[3] = fileSegmentsObject.segments_loudness_start[j];
				values[4] = fileSegmentsObject.segments_pitches[j];
				values[5] = fileSegmentsObject.segments_timbre[j]; 

				compressedVector = new CompressedVector(values); 
				normalizer = compressedVector.fold(Vectors.mkManhattanNormAccumulator()); 
				normalizedValues = compressedVector.divide(normalizer);

				for(int k = 0; k < compressedVector.length(); k++) {

					if(normalizedValues.get(i) != 0) {
						writer.write((k+1)+":"+normalizedValues.get(k)+" ");
					}  
				} 

				writer.println(); 
			}  
		}  
		writer.flush();
		writer.close();  
	}

	public static void visualiseResult(String outputFileName) throws IOException {

		ResultsVisualiser demo = new ResultsVisualiser("Results", "Here are the results...", outputFileName);
		demo.pack();
		demo.setVisible(true);
	}
}
