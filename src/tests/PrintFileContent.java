package tests;

import hdf5.HDF5_Utils;

public class PrintFileContent {

	public static void main(String[] args) {
		 
		if (args.length < 1) {
			System.out.println("file 'hdf5_getters.java'");
			System.out.println("T. Bertin-Mahieux (2010) tb2332@columbia.edu");
			System.out.println("a util static class to read HDF5 song files from");
			System.out.println("the Million Songs Dataset project");
			System.out.println("demo:");
			System.out.println("   see README.txt to compile");
			System.out.println("   java hdf5_getters <some HDF5 song file>");
			System.exit(0);
		}

		String filename = args[0];
		HDF5_Utils.printFileContent(filename);
	}
	
}
