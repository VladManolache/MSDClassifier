package main;

public class Configurations {
	
	/*
	 *  Use this to select how to do the classification
	 *  1 - single thread
	 *  2 -	hadoop enabled   
	 *  3 - hadoop enabled, single flow
	 *  4 - multi-threaded
	 *  5 - multi-threaded, single flow
	 */
	public final static int runConfiguration = 1;
	
	/*
	 *  Use this to select what type of features to use.
	 *  1 - SegmentLevelData, SongLevelData, SectionsBarsTatums
	 *  2 - Only SegmentLevelData
	 */
	public final static int featureSelectionPreset = 2;  
	 
	/*
	 *  Use this to select kernel preset.
	 *  1 - Linear kernel
	 *  2 - Polynomial
	 *  3 - RBF kernel
	 *  4 - Sigmoid kernel
	 */
	public final static int SVMPreset = 3;

	// Bootstrap MSD data with individual song genre tag.
	public final static Boolean bootstrapMSD = false;
	
	/*
     *  Use this to configure single or multi-class data labeling.
     *  Warning - This is overwritten by bootstrapMSD. If bootstrapMSD is true, this will be true as well.
     *  	    - Also, when using hadoopEnabled, this will not be used.
     */ 
	public final static Boolean singleClassLabeling = true; 
	
	// Rearrange input data to get a better sampling of the data.
	public final static Boolean shuffleData = false; 
	
	public final static Boolean clearInputData = true; 
	
	public final static String outputPath = "output/";
	public final static String dataSetPath = "input/MillionSongSubset/data/";

	/*
	 *  Control size and ration of the training and test data sets.
	 *  The path points to a folder, all of it's subfolders will be used unless allowedSubfolders are specified.
	 *  The data inside of folder is split into training and data sets, the split is controlled by the ratio flag.
	 */
	public final static int maximumFiles = 500;	// !-- Negative values will disable this --!
	public final static Boolean filterSubfolders = true;
//	public final static String[] allowedSubfolders = {"A","B","C","D"};
	public final static String[] allowedSubfolders = {}; 
	public static double ratio = 9/10.0;
	public static double trainingSetFolds = 7;
}
