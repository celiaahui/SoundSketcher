package assign12;
/**
 * This class represents a general audio event that occurs at a specific time on a specific track.
 * It is intended to be a superclass for more specific events.
 * @author CELIA AHUI CS1420
 * @version 10-30-2025
 */
public abstract class AudioEvent implements Comparable<AudioEvent> {
	private int time;
	private int trackNumber;
	
	
	/**
	 * Construstor that create a AudioEvent object with the given time  and trackNumber.
	 * @param- time that represents  the starting time at which the event occurs 
	 * @param- trackNumber that represents a number between 0 and 9 that determine which instrument the event .
	 * @throws an IllegalArgumentExcpetion if trackNumber is not between 0 and 9 inlusive or time is negative
	 */
	public AudioEvent(int time, int trackNumber) {
		if(trackNumber < 0 || trackNumber >9 || time < 0) {
			throw new IllegalArgumentException();
		}
		else {
			this.time = time;
			this.trackNumber = trackNumber;
		}
	}
	
	/**
	 * Returns the time 
	 * @return the time
	 */
	public int getTime() {
		return this.time;
	}
	
	/**
	 * returns track number
	 * @return trackNumber
	 */
	public int getTrackNumber() {
		return this.trackNumber;
	}
	
	/**
	 * Abstract method that contains instructions for the event to execute at its specified time
	 * @param synth a SimpleSynthesizer object that will be use to call SimpleSynthesizer methods.
	 */
    public abstract void execute(SimpleSynthesizer synth);

}
