package assign12;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A Song is made of 10 tracks that contain AudioEvents.
 * 
 * @author CS 1420 course staff and Celia Ahui
 * @version 11-05-2025
 */
public class Song {
	 
	
	
	private int tempo;
	private int songLength;
	private SimpleSynthesizer synth;
	private SimpleTimer timer;
	private ArrayList <AudioEvent>[] tracks;
	
	/**
	 * Construct a new Song with the given tempo and length.
	 * It starts with 10 empty tracks. This also initializes the
	 * synthesizer and timer.
	 * 
	 * @param tempo - speed of the song in beats(ticks) per minute
	 * @param songLength - length of the song in ticks
	 */
	@SuppressWarnings("unchecked")
	public Song(int tempo, int songLength) {
		this.tempo = tempo;
		this.songLength = songLength;
		synth = new SimpleSynthesizer();
		timer = new SimpleTimer(synth);
	
		tracks = (ArrayList<AudioEvent>[]) new ArrayList[10];
		for(int i = 0; i < tracks.length; i++)
			tracks[i] = new ArrayList<AudioEvent>();
	}
	
	/**
	 * Add one NoteEvent to the specified track.
	 * After adding, the track is sorted to ensure the events
	 * are always in sorted order.
	 * 
	 * @param time - for the NoteEvent
	 * @param trackNumber - for the NoteEvent
	 * @param duration - for the NoteEvent
	 * @param pitch - for the NoteEvent
	 */
	public void addNoteEvent(int time, int trackNumber, int duration, int pitch) {
	
		NoteEvent note = new NoteEvent(time, trackNumber, duration, pitch);
		tracks[trackNumber].add(note);
		Collections.sort(tracks[trackNumber]);
		
	}
	
	/**
	 * Add one VolumeEvent to the specified track.
	 * After adding, the track is sorted to ensure the events
	 * are always in sorted order.
	 * 
	 * @param time - for the VolumeEvent
	 * @param trackNumber - for the VolumeEvent
	 * @param value - for the VolumeEvent
	 */
	public void addVolumeEvent(int time, int trackNumber, int value) {
		
		VolumeEvent volume = new VolumeEvent(time, trackNumber, value);
		tracks[trackNumber].add(volume);
		
		Collections.sort(tracks[trackNumber]);
		
	}
	
	/**
	 * Remove all events from the specified track.
	 * 
	 * @param trackNumber - of track to clear
	 * @throws IllegalArgumentException if trackNumber is not between 0 and 9
	 */
	public void clearTrack(int trackNumber) {
		
		if(trackNumber < 0 || trackNumber > 9) {
			throw new IllegalArgumentException();
		}
		else {
			tracks[trackNumber].clear();
		}
	}
	
	/**
	 * Remove all events from all tracks.
	 */
	public void clearAll() {
		
		for ( int i = 0; i < tracks.length; i++) {
			tracks[i].clear();
		}
	}
	
	/**
	 * Get the list of events for a given track.
	 * 
	 * @param trackNumber - of track to get
	 * @return list of events in that track
	 * @throws IllegalArgumentException if trackNumber is not between 0 and 9
	 */
	public ArrayList<AudioEvent> getTrack(int trackNumber) {
		if(trackNumber <0 || trackNumber > 9) {
			throw new IllegalArgumentException();
		}
		else {
			return tracks[trackNumber];
		}
	}
	
	/**
	 * Get the playing speed of the song.
	 * 
	 * @return tempo
	 */
	public int getTempo() {
		return tempo;
	}
	
	/**
	 * Set the playing speed of the song.
	 * 
	 * @param newTempo - to set
	 */
	public void setTempo(int newTempo) {
		tempo = newTempo;
	}
	
	/**
	 * Get the total time that the song will play in ticks.
	 * 
	 * @return length of the song in ticks
	 */
	public int getSongLength() {
		return songLength;
	}
	
	/**
	 * Set the total time that the song will play in ticks.
	 * 
	 * @param newLength - of the song in ticks
	 */
	public void setSongLength(int newLength) {
		songLength = newLength;
	}
	
	/**
	 * Get the synthesizer that this song uses to make sound.
	 * 
	 * @return synthesizer
	 */
	public SimpleSynthesizer getSynthesizer() {
		return synth;
	}
	
	/**
	 * Get the timer that this song uses to schedule events.
	 * 
	 * @return timer
	 */
	public SimpleTimer getTimer() {
		return timer;
	}
	
	/**
	 * Schedule all events in all tracks to begin playback.
	 * This first stops playback to avoid overlapping any currently playing song.
	 */
	public void play() {
		stop();
		timer.scheduleEvents(tracks, tempo, songLength);
	}
	
	/**
	 * Tell the timer to stop all scheduled events.
	 * This effectively stops playback.
	 */
	public void stop() {
		timer.stop();
	}

	/**
	 * Sets whether the song loops.
	 * 
	 * @param doLoop - true to loop the song, false to only play once
	 */
	public void enableLoop(boolean doLoop) {
		timer.enableLoop(doLoop);
	}
}
