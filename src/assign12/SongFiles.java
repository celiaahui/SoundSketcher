package assign12;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * This class provides statics methods to read, write and filter .song files used by the SoundSketcher application.
 * @author celia Ahui CS1420
 * @version 12-02-2025
 */

public class SongFiles {
	
	
	/**
	 * reads date from the File to modify the Song
	 * @param file- file from which we will read data
	 * @param song- song to modify
	 */
	public static void readFile(File file, Song song) {
		
		try(Scanner lire = new Scanner(file)) {
			
			int tempo = lire.nextInt();
			int songLength = lire.nextInt();
			
			song.setTempo(tempo);
			song.setSongLength(songLength);
			
			
			for(int i = 0; i< 10; i++) {
				
				SimpleSynthesizer synth = song.getSynthesizer();
				String trackLabel = lire.next();
				int trackNumber = lire.nextInt();
				int instrument = lire.nextInt();
				int audioNumber = lire.nextInt();
				synth.setInstrument(trackNumber, instrument);


				
				for(int j = 0; j < audioNumber; j++) {
					String eventType = lire.next();
					int time = lire.nextInt();
					int duration = lire.nextInt();
					int pitch = lire.nextInt();
					
					if (eventType.equals("note")) {
						 song.addNoteEvent(time, trackNumber, duration, pitch);
					}
					
					else if(eventType.equals("volume")) {
						song.addVolumeEvent(time, trackNumber, duration);
					}
					
					else {
						throw new InputMismatchException("Unknow Event Type");
					}
				}
			}
			
			
		}
		
		catch (InputMismatchException e  ){
			System.out.println("Invalid file ");
		}
		
		catch(IllegalStateException e){
			System.out.println("Invalid file");
			
		} catch (FileNotFoundException e) {
			System.out.println("invalid file");
		}
	}
	
	/**
	 * writes the Song to a file specified by the File
	 * @param file is the file in which we will write 
	 * @param song the song that we will write in the given file 
	 */
	public static void writeFile(File file, Song song) {
		
		try(FileWriter out = new FileWriter(file)){
			
			int tempo = song.getTempo();
			int songLength = song.getSongLength();
			
			out.write(tempo + "\n");
			out.write(songLength + "\n");
			for(int i = 0; i < 10; i++) {
				
				ArrayList<AudioEvent> firstTrackEvents = song.getTrack(i);
				ArrayList<AudioEvent> filtered = filterEvents(firstTrackEvents);
				
				Collections.sort(filtered, new ChronologicalOrder());
				SimpleSynthesizer synth = song.getSynthesizer();
				int instrument = synth.getInstrument(i);
				
				out.write("track" + i + "\n");
				out.write(i + "\n");
				out.write(instrument + "\n");
				out.write(filtered.size()+ "\n");
				
				for(AudioEvent event :  filtered) {
					if(event instanceof NoteEvent) {
						NoteEvent e =(NoteEvent) event;
						String type = "note";
						int time = e.getTime();
						int duration = e.getDuration();
						int pitch =e.getPitch();
						
						out.write(type + "\n");
						out.write(time + "\n");
						out.write(duration + "\n");
						out.write(pitch + "\n");
						
					}
					
					else if(event instanceof VolumeEvent) {
						VolumeEvent v = (VolumeEvent) event;
						String type = "volume";
						int time = v.getTime();
						int value = v.getValue();
						int zero = 0;
						
						out.write(type + "\n");
						out.write(time + "\n");
						out.write(value + "\n");
						out.write(zero + "\n");
						
						
					}
				}
				
				
			}
			
			out.close();
			
			
		} catch (IOException e) {
			System.out.println("Error: could not write song file " + file.getName());
		}
		
		
	}
	
	/**
	 * Create a new List of events with no duplicates.
	 * @param events - previous list of events
	 * @return an Arraylist that contains events without duplicates
	 */
	public static ArrayList<AudioEvent> filterEvents(ArrayList<AudioEvent> events){
		if(events == null) {
			throw new IllegalArgumentException("Events should not be null");
		}
		
		HashMap<Integer, ArrayList<AudioEvent>> map = new HashMap<>();
		ArrayList<AudioEvent> listReturned = new ArrayList<>();
		for(AudioEvent event : events) {
			int time = event.getTime();
			if(! map.containsKey(time)) {
				ArrayList<AudioEvent> newList = new ArrayList<>();
				newList.add(event);
				map.put(time, newList);
				listReturned.add(event);
				
			}
			
			else {
				ArrayList<AudioEvent> sameTimeEvents = map.get(time);
				
				boolean isDuplicate = false;
				
				for(AudioEvent a : sameTimeEvents) {
					if (event instanceof NoteEvent && a instanceof NoteEvent) {
	                   
	                    if (event.compareTo(a) == 0) {
	                        isDuplicate = true;
	                        break;
	                    }
	                    
					}
					else if (event instanceof VolumeEvent && a instanceof VolumeEvent) {
	             
	                    	isDuplicate = true;
		                    break;
	                    
	                }
					
					
					
				}
				
				if(!isDuplicate) {
					sameTimeEvents.add(event);
					listReturned.add(event);
				}
					
					
			}
			
		}
		return listReturned;
		
		
	}

	/**
	 * Nested class that do non natural ordering fro AudioEvent Objects 
	 */
	public static class ChronologicalOrder implements Comparator<AudioEvent>{

		@Override
		public int compare(AudioEvent o1, AudioEvent o2) {
			if(o1.getTime() > o2.getTime()) {
				return 1;
			}
			else if(o1.getTime() < o2.getTime()) {
				return -1;
			}
			else {
				return o1.compareTo(o2);
			}
		
		}
		
	}

}
