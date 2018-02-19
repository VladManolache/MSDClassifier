package data.hdf5;

import ncsa.hdf.object.h5.H5File;

public class HDF5FileData {

	public double[] segments_start;
	public double[] segments_confidence;
	public double[] segments_pitch;
	public double[] segments_timbre;
	public double[] segments_loudness_max;
	public double[] segments_loudness_max_time;
	public double[] segments_loudness_start;  
	
	public double[] bars_start;
	public double[] bars_confidence;
	public double[] beats_start;
	public double[] beats_confidence;
	public double[] tatums_start;
	public double[] tatums_confidence;
	public double[] sections_start;
	public double[] sections_confidence;
	
	public double danceability;
	public double duration;
	public double key;
	public double key_confidence;
	public double loudness;
	public double mode;
	public double mode_confidence;
	public double tempo;
	public double time_signature;
	public double time_signature_confidence;
	
	public String[] tags;
	public String trackId;
	 
	public HDF5FileData(H5File h5) throws Exception { 
		
		segments_start = HDF5.get_segments_start(h5);
		segments_confidence = HDF5.get_segments_confidence(h5);
		segments_pitch = HDF5.get_segments_pitches(h5);
		segments_timbre = HDF5.get_segments_timbre(h5);
		segments_loudness_max = HDF5.get_segments_loudness_max(h5);
		segments_loudness_max_time = HDF5.get_segments_loudness_max_time(h5);
		segments_loudness_start = HDF5.get_segments_loudness_start(h5);  
		
		bars_start = HDF5.get_bars_start(h5);
		bars_confidence = HDF5.get_bars_confidence(h5);
		beats_start = HDF5.get_beats_start(h5);
		beats_confidence = HDF5.get_beats_confidence(h5);
		tatums_start = HDF5.get_tatums_start(h5);
		tatums_confidence = HDF5.get_tatums_confidence(h5);
		sections_start = HDF5.get_sections_start(h5);
		sections_confidence = HDF5.get_sections_confidence(h5);
		
		danceability = HDF5.get_danceability(h5);
		duration = HDF5.get_duration(h5);
		key = HDF5.get_key(h5);
		key_confidence = HDF5.get_key_confidence(h5);
		loudness = HDF5.get_loudness(h5);
		mode = HDF5.get_mode(h5);
		mode_confidence = HDF5.get_mode_confidence(h5);
		tempo = HDF5.get_tempo(h5);
		time_signature = HDF5.get_time_signature(h5);
		time_signature_confidence = HDF5.get_time_signature_confidence(h5);
		
		tags = HDF5.get_artist_terms(h5);
		trackId = HDF5.get_track_id(h5); 
	}
	
}
