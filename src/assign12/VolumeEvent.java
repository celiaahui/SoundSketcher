package assign12;
/*
 * This class represents a change in volume level for a specific track in an audio sequence.
 * @author CELIA AHUI CS1420
 * @version 10-30-2025
 */
public class VolumeEvent extends AudioEvent{
	private int value;
	
	
	/*
	 * Contrutor that creates  a new VolumeEvent object using the given parameter.
	 * @param- time that represents  the starting time at which the event occurs 
	 * @param- trackNumber that represents a number between 0 and 9 that determine which instrument the event .
	 * @param value an integer that represents the new value for the volume
	 * @throws an IllegalArgumentException if the value is not between 0 and 127 inclusive.
	 */
	public VolumeEvent(int time, int trackNumber, int value) {
		super(time, trackNumber);
		if(value < 0 || value > 127) {
			throw new IllegalArgumentException();
		}
		else {
			this.value = value;
		}
		
	}
	
	/*
	 * returns the value parameter
	 * @return an int tht is the value of the object
	 * 
	 */
	public int getValue() {
		return this.value;
	}
	
	/*
	 * contains the instructions for playing a note or setting the volume leve
	 * @param- synth is a SimpleSynthesizer's object.
	 */
	public void execute(SimpleSynthesizer synth) {
		synth.noteOn(this.getTrackNumber(), this.value);
	}
	/*
	 * Returns an illustration of the object
	 *@return a String that describes th object
	 */
	public String toString() {
		return "Volume[" + this.getTime() + ", " + this.getTrackNumber() + ", " + this.value + "]";
	}
	
	/*
	 * Method required by the Comparable interface.
	 * This compares two AudioEvent objects.
	 * @param other AudioEvent to compare to 
	 * @return an int that is positive, negative or equal to zero.
	 */
	public int compareTo(AudioEvent other) {
		if (other instanceof NoteEvent) {
			return 1;
		}
		
		if(other instanceof VolumeEvent) {
			VolumeEvent other1 = (VolumeEvent) other;
			return this.getTime() - other1.getTime();
		}
		
		return 0; 
	}

}
