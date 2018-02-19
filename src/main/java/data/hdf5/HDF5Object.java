package data.hdf5;

import ncsa.hdf.object.h5.H5File;

public class HDF5Object {
 
	public String objectPath;
	public H5File h5;
	
	// Metadata
	public double artistFamiliarity;
	public double artistHotttness;
	public String artistId;
	public String artistMbid;
	public int artistPlaymeid; 
	public int artist7digitalid;  
	public double artistLatitude;
	public double artistLongitude; 
	public String artistLocation;
	public String artistName;
	public String release;
	public int release7digitalid; 
	public double songHottness;
	public String title;
	public double track7digitalid; 
	public String[] similar_artists;
	public String[] artist_terms;
	public double[] term_freq;
	public double[] terms_weight; 
	
	// Analysis
	public double duration;
	public double end_of_fade;
	public double key;
	public double confidence;
	public double loudness;
	public double mode;
	public double modeConfidence;
	public double start_fade_out;
	public double tempo;
	public double timeSignature;
	public double timeSignatureConfidence; 
	public double[] segments_start;
	public double[] segments_confidence;
	public double[] segments_pitches;
	public double[] segments_timbre;
	public double[] segments_loudness_max;
	public double[] segments_loudness_max_time;
	public double[] segments_loudness_start;  
	public double[] sections_start;
	public double[] sections_confidence;
	public double[] beats_start;
	public double[] beats_confidence;
	public double[] bars_start;
	public double[] bars_confidence;
	public double[] tatums_start;
	public double[] tatums_confidence; 
	
	// Musicbrainz
	public int year; 
	public String[] mbtags;  
	
	public HDF5Object(H5File h5, String objectPath) {
		 
		try { 
			this.h5 = h5;
			this.objectPath = objectPath;
			
			// Metadata
			artistFamiliarity = HDF5.get_artist_familiarity(h5);
			artistHotttness = HDF5.get_artist_hotttnesss(h5);
			artistId = HDF5.get_artist_id(h5);
			artistMbid = HDF5.get_artist_mbid(h5);
			artistPlaymeid = HDF5.get_artist_playmeid(h5);
			artist7digitalid = HDF5.get_artist_7digitalid(h5);
			artistLatitude = HDF5.get_artist_latitude(h5);
			artistLongitude = HDF5.get_artist_longitude(h5); 
			artistLocation = HDF5.get_artist_location(h5);
			artistName = HDF5.get_artist_name(h5);
			release = HDF5.get_release(h5);
			release7digitalid = HDF5.get_release_7digitalid(h5);
			songHottness = HDF5.get_song_hotttnesss(h5);
			title = HDF5.get_title(h5);
			track7digitalid = HDF5.get_track_7digitalid(h5); 
			similar_artists = HDF5.get_similar_artists(h5);
			artist_terms = HDF5.get_artist_terms(h5);
			term_freq = HDF5.get_artist_terms_freq(h5);
			terms_weight = HDF5.get_artist_terms_weight(h5); 
			
			// Analysis
			duration = HDF5.get_duration(h5);
			end_of_fade = HDF5.get_end_of_fade_in(h5);
			key = HDF5.get_key(h5);
			confidence = HDF5.get_key_confidence(h5);
			loudness = HDF5.get_loudness(h5);
			mode = HDF5.get_mode(h5);
			modeConfidence = HDF5.get_mode_confidence(h5);
			start_fade_out = HDF5.get_start_of_fade_out(h5);
			tempo = HDF5.get_tempo(h5);
			timeSignature = HDF5.get_time_signature(h5);
			timeSignatureConfidence = HDF5.get_time_signature_confidence(h5); 
			segments_start = HDF5.get_segments_start(h5);
			segments_confidence = HDF5.get_segments_confidence(h5);
			segments_pitches = HDF5.get_segments_pitches(h5);
			segments_timbre = HDF5.get_segments_timbre(h5);
			segments_loudness_max = HDF5.get_segments_loudness_max(h5);
			segments_loudness_max_time = HDF5.get_segments_loudness_max_time(h5);
			segments_loudness_start = HDF5.get_segments_loudness_start(h5);  
			sections_start = HDF5.get_sections_start(h5);
			sections_confidence = HDF5.get_sections_confidence(h5);
			beats_start = HDF5.get_beats_start(h5);
			beats_confidence = HDF5.get_beats_confidence(h5);
			bars_start = HDF5.get_bars_start(h5);
			bars_confidence = HDF5.get_bars_confidence(h5);
			tatums_start = HDF5.get_tatums_start(h5);
			tatums_confidence = HDF5.get_tatums_confidence(h5); 
			
			// Musicbrainz
			year = HDF5.get_year(h5);  
//			mbtags = HDF5.get_artist_mbtags(h5); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}  
	}
	
}
