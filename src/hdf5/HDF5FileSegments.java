package hdf5;

import ncsa.hdf.object.h5.H5File;

public class HDF5FileSegments {

	public double[] segments_start;
	public double[] segments_confidence;
	public double[] segments_pitches;
	public double[] segments_timbre;
	public double[] segments_loudness_max;
	public double[] segments_loudness_max_time;
	public double[] segments_loudness_start;  
	
	public HDF5FileSegments(H5File h5) throws Exception { 
		
		segments_start = HDF5.get_segments_start(h5);
		segments_confidence = HDF5.get_segments_confidence(h5);
		segments_pitches = HDF5.get_segments_pitches(h5);
		segments_timbre = HDF5.get_segments_timbre(h5);
		segments_loudness_max = HDF5.get_segments_loudness_max(h5);
		segments_loudness_max_time = HDF5.get_segments_loudness_max_time(h5);
		segments_loudness_start = HDF5.get_segments_loudness_start(h5);  
	}
	
}
