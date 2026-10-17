package assign12;

/**
 * This class represents a musical note event in a specific track of an audio sequence.
 * @author CELIA AHUI CS1420
 * @version 10-30-2025
 */

public class NoteEvent extends AudioEvent {
	
	private int duration; 
	private int pitch;
	
	/**
	 *Construtor that creates a new NoteEvent Object with the given parametr
	 *@param- time  an int that represents  the starting time at which the event occurs 
	 *@param- trackNumber an int  that represents a number between 0 and 9 that determine which instrument the event .
	 *@param- duration an int 
	 *@param-picth an int 
	 *@throws an IllegalArgumentException  if duration is negative or pitch is not between 0 and 127 inclusive.
	 */
	public NoteEvent(int time, int trackNumber, int duration, int pitch) {
		super(time,trackNumber);
		if(duration < 0 || pitch < 0 || pitch > 127) {
			throw new IllegalArgumentException();
		}
		else {
			this.duration =  duration; 
			this.pitch = pitch;
		}
	}
	
	/**
	 * returns the duration
	 * @return the duration
	 */
	public int getDuration() {
		return this.duration;
	}
	
	/**
	 * returns pitch
	 * @return picth
	 */
	public int getPitch() {
		return this.pitch;
	}
	
	/**
	 * Indicates the note event has finished playing
	 * @param- synth a SimpleSynthesizer object.
	 */
	public void complete(SimpleSynthesizer synth) {
		synth.noteOff(this.getTrackNumber(), this.pitch);
	}
	
	/**
	 * contains the instructions for playing a note or setting the volume leve
	 * @param- synth is a SimpleSynthesizer's object.
	 */
	public void execute(SimpleSynthesizer synth) {
		synth.noteOn(this.getTrackNumber(), this.pitch);
	}
	
	/**
	 * Gives an illustration of the event
	 * @return a string that describe the event 
	 */
	public String toString() {
		return "Note[" + this.getTime() + ", " + this.getTrackNumber() + ", "+ this.duration + ", " + this.pitch + "]";
	}
	/**
	 * Compare the current object with another AudioEven object
	 * @param another AuioEvent's object that we will use for the comparison
	 * @return Returns a negative value if this NoteEvent should come before the other,
     * a positive value if it should come after,
     * or 0 if both events are of the same type and have the same pitch.
	 */
	public int compareTo(AudioEvent other) {
		
		if(other instanceof VolumeEvent) {
			return -1;
		}
		
		if(other instanceof NoteEvent) {
			NoteEvent other1 = (NoteEvent) other;
			return other1.getPitch() - this.pitch;
		}
		
		return 0;
		
		
	}
	

}
