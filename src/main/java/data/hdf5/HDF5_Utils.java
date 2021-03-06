package data.hdf5;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import main.Configurations;
import ncsa.hdf.object.h5.H5File;

public class HDF5_Utils {
 
	public static ArrayList<String> getHDF5FileNamesList(String path) {
		
		String[] allowedSubFolders = {};
		return getHDF5FileNamesList(path, allowedSubFolders);
	}
	
	public static ArrayList<String> getHDF5FileNamesList(final String path, String[] allowedSubFolders) {
		 
		final Path parent = Paths.get(path);
		final ArrayList<String> fileNamesList = new ArrayList<String>();
		final ArrayList<String> allowedFolders = new ArrayList<String>();
		for(int i = 0; i < allowedSubFolders.length; i++) {
			allowedFolders.add(allowedSubFolders[i]);
		}
		
		try 
		{
		    Path startPath = Paths.get(path); 
		    
		    Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
		    	
				int filesAdded = 0;
		    	
		        @Override
		        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {  
		            
		        	// Continue if this is the parent
		        	if(parent.equals(dir)) {
		        		return FileVisitResult.CONTINUE;
		        	}
		        	
		        	// Check if the folder is in the allowed folders list. Skip if no allowed folders list
		        	Boolean contains = (allowedFolders.size() == 0) ? true : false; 
		        	for(String str : allowedFolders) {
		        		 
		        		if(str.equalsIgnoreCase(dir.getFileName().toString())) { 
		        			contains = true;
		        			break;
		        		}
		        	}
		        	 
		        	// If folder belongs to allowed folder list, visit it, else skip it. 
		        	if(contains == true) {
		        		return FileVisitResult.CONTINUE;
	            	}   
		        	else if(!dir.getParent().getFileName().toString().equals("data") && !Configurations.filterSubfolders) { 
		        		return FileVisitResult.CONTINUE;
		        	}
		        	return FileVisitResult.SKIP_SUBTREE;
		        }

		        @Override
		        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		                 
		        	// If we have reached the maximum nr. of files we want, stop searching.
		        	if(filesAdded == Configurations.maximumFiles) {
		        		return FileVisitResult.TERMINATE;
		        	}
		        	
		            if(FilenameUtils.getExtension(file.toString()).equals("h5")) {  
		        		filesAdded++;
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
	
	public static ArrayList<HDF5Object> getSongsFromArtist(String artist, ArrayList<HDF5Object> dataSource) {
		
		ArrayList<HDF5Object> list = new ArrayList<HDF5Object>();
		for(int i = 0; i < dataSource.size(); i++) {
			
			HDF5Object object = dataSource.get(i);
			if(object.artistName.equals(artist)) {
				list.add(object);
			}
		}
		 
		return list; 
	} 
	
	public static HDF5FileData getFileData(String file) throws Exception {

		HDF5FileData fileData = null;
		H5File h5 = null; 
		try { 
			h5 = HDF5.hdf5_open_readonly(file);
			fileData = new HDF5FileData(h5);
		}
		catch(Exception e) { 
			System.out.println("Cannot read content of file: "+file);
		}
		finally {
			if(h5 != null) {
				h5.close();
				h5 = null;
			}
		} 
		
		return fileData; 
	}
 
	public static void printFileContent(String filename) {
		
		H5File h5 = HDF5.hdf5_open_readonly(filename);
		int nSongs = HDF5.get_num_songs(h5);
		System.out.println("numberof songs: " + nSongs);
		if (nSongs > 1) System.out.println("we'll display infor for song 0");
		try {
			double[] res;
			String[] resS;
			int[] resI;
			
			// metadata
			System.out.println("artist familiarity: " + HDF5.get_artist_familiarity(h5));
			System.out.println("artist hotttnesss: " + HDF5.get_artist_hotttnesss(h5));
			System.out.println("artist id: " + HDF5.get_artist_id(h5));
			System.out.println("artist mbid: " + HDF5.get_artist_mbid(h5));
			System.out.println("artist playmeid: " + HDF5.get_artist_playmeid(h5));
			System.out.println("artist 7digitalid: " + HDF5.get_artist_7digitalid(h5));
			System.out.println("artist latitude: " + HDF5.get_artist_latitude(h5));
			System.out.println("artist longitude: " + HDF5.get_artist_longitude(h5));
			System.out.println("artist location: " + HDF5.get_artist_location(h5));
			System.out.println("artist name: " + HDF5.get_artist_name(h5));
			System.out.println("release: " + HDF5.get_release(h5));
			System.out.println("release 7digitalid: " + HDF5.get_release_7digitalid(h5));
			System.out.println("song hotttnesss: " + HDF5.get_song_hotttnesss(h5));
			System.out.println("title: " + HDF5.get_title(h5));
			System.out.println("track 7digitalid: " + HDF5.get_track_7digitalid(h5));
			resS = HDF5.get_similar_artists(h5);
			System.out.println("similar artists, length: "+resS.length+", elem 2: "+resS[20]);
			resS = HDF5.get_artist_terms(h5);
			System.out.println("artists terms, length: "+resS.length+", elem 0: "+resS[0]);
			res = HDF5.get_artist_terms_freq(h5);
			System.out.println("artists terms freq, length: "+res.length+", elem 0: "+res[0]);
			res = HDF5.get_artist_terms_weight(h5);
			System.out.println("artists terms weight, length: "+res.length+", elem 0: "+res[0]);
			
			// analysis
			System.out.println("duration: " + HDF5.get_duration(h5));
			System.out.println("end_of_fade_in: " + HDF5.get_end_of_fade_in(h5));
			System.out.println("key: " + HDF5.get_key(h5));
			System.out.println("key confidence: " + HDF5.get_key_confidence(h5));
			System.out.println("loudness: " + HDF5.get_loudness(h5));
			System.out.println("mode: " + HDF5.get_mode(h5));
			System.out.println("mode confidence: " + HDF5.get_mode_confidence(h5));
			System.out.println("start of fade out: " + HDF5.get_start_of_fade_out(h5));
			System.out.println("tempo: " + HDF5.get_tempo(h5));
			System.out.println("time signature: " + HDF5.get_time_signature(h5));
			System.out.println("time signature confidence: " + HDF5.get_time_signature_confidence(h5));
			res = HDF5.get_segments_start(h5);
			System.out.println("segments start, length: "+res.length+", elem 20: "+res[20]);
			res = HDF5.get_segments_confidence(h5);
			System.out.println("segments confidence, length: "+res.length+", elem 20: "+res[20]);
			res = HDF5.get_segments_pitches(h5);
			System.out.println("segments pitches, length: "+res.length+", elem 20: "+res[20]);
			res = HDF5.get_segments_timbre(h5);
			System.out.println("segments timbre, length: "+res.length+", elem 20: "+res[20]);
			res = HDF5.get_segments_loudness_max(h5);
			System.out.println("segments loudness max, length: "+res.length+", elem 20: "+res[20]);
			res = HDF5.get_segments_loudness_max_time(h5);
			System.out.println("segments loudness max time, length: "+res.length+", elem 20: "+res[20]);
			res = HDF5.get_segments_loudness_start(h5);
			System.out.println("segments loudness start, length: "+res.length+", elem 20: "+res[20]);
			res = HDF5.get_sections_start(h5);
			System.out.println("sections start, length: "+res.length+", elem 1: "+res[1]);
			res = HDF5.get_sections_confidence(h5);
			System.out.println("sections confidence, length: "+res.length+", elem 1: "+res[1]);
			res = HDF5.get_beats_start(h5);
			System.out.println("beats start, length: "+res.length+", elem 1: "+res[1]);
			res = HDF5.get_beats_confidence(h5);
			System.out.println("beats confidence, length: "+res.length+", elem 1: "+res[1]);
			res = HDF5.get_bars_start(h5);
			System.out.println("bars start, length: "+res.length+", elem 1: "+res[1]);
			res = HDF5.get_bars_confidence(h5);
			System.out.println("bars confidence, length: "+res.length+", elem 1: "+res[1]);
			res = HDF5.get_tatums_start(h5);
			System.out.println("tatums start, length: "+res.length+", elem 3: "+res[3]);
			res = HDF5.get_tatums_confidence(h5);
			System.out.println("tatums confidence, length: "+res.length+", elem 3: "+res[3]);
			
			// musicbrainz
			System.out.println("year: " + HDF5.get_year(h5));
			resS = HDF5.get_artist_mbtags(h5);
			resI = HDF5.get_artist_mbtags_count(h5);
			if (resS.length > 0) {
				System.out.println("artists mbtags, length: "+resS.length+", elem 0: "+resS[0]);
				System.out.println("artists mbtags count, length: "+resI.length+", elem 0: "+resI[0]);
			}
			else {
				System.out.println("artists mbtags, length: "+resS.length);
				System.out.println("artists mbtags count, length: "+resI.length);
			}
		} 
		catch (Exception e) {
			System.out.println("something went wrong:");
			e.printStackTrace();
		}
		
		HDF5.hdf5_close(h5);
		System.out.println("done, file closed.");
	}
}
