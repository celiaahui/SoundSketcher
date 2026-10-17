package assign12;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

/**
 * A TrackEditor is the interactive GUI component for drawing a sequence of
 * note events or volume changes in a track.
 * 
 * @author CS 1420 course staff and CELIA AHUI
 * @version 11/20/2025
 */
public class TrackEditor extends JPanel implements MouseListener,MouseMotionListener {
	
	public static enum Mode{NOTE, VOLUME, COPY};
	private Mode mode;
	
	private int trackNumber;
	private SimpleSynthesizer synth;
	private ArrayList<AudioEvent> events;     
	private ArrayList<NoteEvent> notesToCopy; 
	
	private int columns, rows; 
	private boolean drawing;   
	private int currentRow, currentColumn;
	private int noteDuration; 
	
	
	private int copyFromRow, copyFromColumn; 
	private int copyToRow, copyToColumn;     
	
	
	private static final int lowestPitch = 21;
	private static final int highestPitch = 108;
	
	/**
	 * Create a new TrackEditor with the default configuration.
	 * 
	 * @param trackNumber assigned to this track in the midi system
	 * @param synthesizer for making sounds
	 * @param sequencer for sequencing note events
	 */
	public TrackEditor(int trackNumber, int songLength, ArrayList<AudioEvent> events, SimpleSynthesizer synth) {
		columns = songLength;
		rows = highestPitch - lowestPitch + 1;
		
		this.trackNumber = trackNumber;
		this.synth = synth;
		this.events = events;
		notesToCopy = new ArrayList<NoteEvent>();
		drawing = false;
		mode = Mode.NOTE;
		
		setBackground(Color.WHITE);
		
		
		addMouseListener(this);
	    addMouseMotionListener(this);
	}
	
	/**
	 * Removes all events from the track.
	 * @return
	 */
	public void clearTrack() {
		events.clear();
		repaint();
	}
	
	/**
	 * Set the song duration in ticks.
	 * 
	 * @param songLength in ticks
	 * @return
	 */
	public void setSongLength(int songLength) {
		columns = songLength;
		if (columns < 1)
			columns = 1;
		repaint();
	}
	
	/**
	 * Set the editor to the specified mode.
	 * 
	 * @param mode - either Mode.NOTE, Mode.VOLUME, or Mode.COPY
	 * @return
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
		
		synth.setVolume(trackNumber, 100);
	}
	
	/**
	 * This method is called by the system when a component needs to be painted.
	 * Which can be at one of three times: --when the component first appears --when
	 * the size of the component changes (including resizing by the user) --when
	 * repaint() is called
	 * 
	 * Partially overrides the paintComponent method of JPanel.
	 * 
	 * @param g -- graphics context onto which we can draw
	 * @return
	 */
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		
		
		
		
		g.setColor(new Color(255, 240, 245));
		int previousVolume = 100; 
		int previousTime = 0; 
		for(AudioEvent event : events) {
			if(event instanceof VolumeEvent) {
				
				
				int xLeft = colToPixel(previousTime);
				int xRight = colToPixel(event.getTime());
				int width = xRight - xLeft;
				
				int row = volumeToRow(previousVolume);
				int top = rowToPixel(row);
				
				int bottom = getHeight();
				int height = bottom - top;
				g.fillRect(xLeft, top , width, height);
				
				previousVolume = ((VolumeEvent) event).getValue();
				previousTime = event.getTime();
				
				
				
			}
		}
		
		
		int xLeft = colToPixel(previousTime);
		int xRight = getWidth();
		int width = xRight - xLeft;
		int row = volumeToRow(previousVolume);
		int yTop = rowToPixel(row);
		int yBottom = getHeight();
		int height = yBottom - yTop;
		g.fillRect(xLeft, yTop, width, height);
		
		
		
		g.setColor(Color.BLACK);
		
		for(int col = 0; col <= columns; col++) {
			int x = colToPixel(col);
			g.drawLine(x, 0, x, getHeight());
		}
		
		for(int ro = 0; ro <= rows; ro++) {
			int y = rowToPixel(ro);
			g.drawLine(0, y, getWidth(), y);
		}
		

	
	
		for(int r = 0; r <= rows; r++) {
			int y = rowToPixel(r);
			if(r % 12 == 0) {
				g.fillRect(0, y, getWidth(), 2);
				
			}
		}
		
	
		for( int c = 0; c <= columns; c++) {
			int x = colToPixel(c);
			g.fillRect(x, 0, 2, getHeight());
		}
		
	
		if(drawing) {
			if(mode == Mode.VOLUME) {
				
				g.setColor(new Color(255, 240, 245));
				int x = colToPixel(currentColumn);
				int y = rowToPixel(currentRow);
				g.drawLine(x,y,colToPixel(currentColumn + 1) - x, getHeight()- y);
			}
			else if(mode == Mode.NOTE && noteDuration > 0){
				
				 int xLeft1 = colToPixel(currentColumn);
				 int xRight1 = colToPixel(currentColumn + noteDuration);
				 int w = xRight1 - xLeft1;

				 int yTop1 = rowToPixel(currentRow);
				 int yNext = rowToPixel(currentRow + 1);
				 int h = yNext - yTop1;

				 g.fillRect(xLeft1, yTop1, w, h);
			}
			else if(mode == Mode.COPY) {
				
				g.setColor(new Color(180,210,255,100));
				int xL = colToPixel(copyFromColumn);
				int xR = colToPixel(copyToColumn);
				int w = xR-xL;
				
				int yT = rowToPixel(copyFromRow);
				int yB = rowToPixel(copyToRow);
				int h = yB - yT;
				g.fillRect(xL, yT, w, h);
				
			}
		}
		
		
		g.setColor(new Color(230, 80, 150));
		for(AudioEvent event : events) {
			if(event instanceof NoteEvent) {
				NoteEvent note = (NoteEvent)event;
				
				  int row1 = pitchToRow(note.getPitch());

			       
			      int col = note.getTime();

			       
			      int xL = colToPixel(col);
			      int xR = colToPixel(col + note.getDuration());
			      int w = xR - xL;

			        
			      int y = rowToPixel(row1);
			      int yNext = rowToPixel(row1 + 1);
			      int h= yNext - y; 

			        
			      g.fillRect(xL, y, w, h);
			      
			      
			}
		}
		
		
		int middleCRow = pitchToRow(60);
		int y = rowToPixel(middleCRow);
		g.setColor(new Color(251,2,20));
		g.drawLine(0, y, getWidth(), y);
		

	
	}
	
	
	
	
	

	//////////////////////////////////////////////////////////////////////
	// Private helper methods
	//////////////////////////////////////////////////////////////////////
	
	/**
	 * Convert a row index in the grid to a pitch number.
	 * 
	 * @param rowNumber - to convert
	 * @return pitch corresponding to that row
	 */
	private int rowToPitch(int rowNumber) {
		return highestPitch - rowNumber;
	}
	
	/**
	 * Convert a pitch number to a row index in the grid.
	 * 
	 * @param pitch - to convert
	 * @return row index corresponding to that pitch
	 */
	private int pitchToRow(int pitch) {
		return highestPitch - pitch;
	}
	
	/**
	 *  Convert a row index in the grid to a volume value.
	 *  
	 * @param rowNumber - to convert
	 * @return volume value corresponding to that row
	 */
	private int rowToVolume(int rowNumber) {
		return 127 - rowNumber * 127 / rows;
	}
	
	/**
	 * Convert a volume value to a row index in the grid.
	 * 
	 * @param volume - to convert
	 * @return row index corresponding to that volume
	 */
	private int volumeToRow(int volume) {
		return rows - volume * rows / 127;
	}
	
	/**
	 * Converts a row index to pixel y value of the TOP edge of the row.
	 * 
	 * @param row - index
	 * @return pixel y value of the top edge
	 */
	private int rowToPixel(int row) {
		return row * getHeight() / rows;
	}

	/**
	 * Converts a column index to pixel x value of the LEFT edge of the column.
	 * 
	 * @param col - column index
	 * @return pixel x value of the left side
	 */
	private int colToPixel(int col) {
		return col * getWidth() / columns;
	}

	/**
	 * Converts a pixel y value to a row index.
	 * 
	 * @param pixelY - pixel y value
	 * @return index of row containing that pixel
	 */
	private int pixelToRow(int pixelY) {
		return rows * pixelY / getHeight();
	}

	/**
	 * Converts a pixel x value to a column index.
	 * 
	 * @param pixelX - pixel x value
	 * @return index of column containing that pixel
	 */
	private int pixelToCol(int pixelX) {
		return columns * pixelX / getWidth();
	}

	
	private static final long serialVersionUID = 1L;

	@Override
	/**
	 * Handles mouse dragging to update note duration, volume changes, or copy selection depending on the current mode.
	 * @param e the mouse event containing the current mouse position.
	 * @return
	 */
	public void mouseDragged(MouseEvent e) {
		
		if (!drawing) {
	        return;
	    }

	    
	    int tempCol = pixelToCol(e.getX());
	    int tempRow = pixelToRow(e.getY());

	    if (mode == Mode.NOTE) {
	        
	        noteDuration = tempCol - currentColumn + 1;

	        
	        if (tempRow != currentRow) {
	            int oldPitch = rowToPitch(currentRow);
	            int newPitch = rowToPitch(tempRow);

	            synth.noteOff(trackNumber, oldPitch);
	            synth.noteOn(trackNumber, newPitch);

	            currentRow = tempRow;
	        }
	    } else if (mode == Mode.VOLUME) {
	        
	        if (tempRow != currentRow) {
	            int newVolume = rowToVolume(tempRow);
	            synth.setVolume(trackNumber, newVolume);

	            
	            synth.noteOn(trackNumber, 60);

	            currentRow = tempRow;
	        }
	    } else if (mode == Mode.COPY) {
	       
	        copyToColumn = tempCol;
	        copyToRow = tempRow;
	    }

	    
	    repaint();
	
		
	}

	@Override
	/**
	 * @return
	 */
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 *Called when the mouse is clicked to delete a note or paste copied notes depending on the current mode.
	 *@param e the mouse event containing the click position
	 */
	public void mouseClicked(MouseEvent e) {
		
		 if (e.getButton() == MouseEvent.BUTTON1) {
		        return;
		    }

		    int col = pixelToCol(e.getX());
		    int row = pixelToRow(e.getY());

		    if (mode == Mode.NOTE) {
		        int pitch = rowToPitch(row);
		        for (int i = 0; i < events.size(); i++) {
		            AudioEvent ev = events.get(i);
		            if (ev instanceof NoteEvent) {
		                NoteEvent note = (NoteEvent) ev;
		                if (note.getTime() == col && note.getPitch() == pitch) {
		                    events.remove(i);
		                    break;
		                }
		            }
		        }
		        repaint();
		        return;
		    }

		    if (mode == Mode.VOLUME) {
		        return;
		    }

		    if (mode == Mode.COPY) {
		        int timeOffset = col - copyFromColumn;
		        int pitchOffset = row - copyFromRow;

		        for (NoteEvent original : notesToCopy) {
		            int newTime = original.getTime() + timeOffset;
		            int newPitch = original.getPitch() + pitchOffset;
		            int duration = original.getDuration();
		            NoteEvent pasted = new NoteEvent(newTime, duration, newPitch, trackNumber);
		            events.add(pasted);
		        }

		        Collections.sort(events);
		        repaint();
		    }
		
	}

	@Override
	/**
	 * Called when the mouse button is pressed to start drawing based on the current mode.
	 * @param e the mouse event containing the press position
	 * @return
	 */
	public void mousePressed(MouseEvent e) {
	
		
		if(e.getButton() != MouseEvent.BUTTON1  ) {
			return;
		}
		drawing = true;
		int x = e.getX();
		int y = e.getY();
		
		currentColumn = pixelToCol(x);
		currentRow = pixelToRow(y);
		
		repaint();
		
		if(mode == Mode.NOTE) {
			int pitch = rowToPitch(currentRow);
			synth.noteOn(trackNumber, pitch);
			noteDuration = 1;
		}
		
		else if (mode == Mode.VOLUME) {
			int volume = rowToVolume(currentRow);
			synth.setVolume(trackNumber, volume);
			synth.noteOn(trackNumber, 60);
			
		}
		
		else if (mode == Mode.COPY) {
			copyFromRow = currentRow;
			copyFromColumn = currentColumn;
			copyToRow = currentRow;
			copyToColumn = currentColumn;
		
			
		}
	}

	@Override
	/**
	 * Called when the mouse button is released to finish drawing and create the appropriate event based on the current mode.
	 * @param e the mouse event containing the release position.
	 * @return
	 */
	public void mouseReleased(MouseEvent e) {
		
		if(!drawing) {
			return;
		}
		
		drawing = false;
		
		if(mode == Mode.NOTE) {
			int pitch = rowToPitch(currentRow);
			synth.noteOff(trackNumber, pitch);
			
			if(noteDuration > 0) {
				int time = currentColumn;
				int row = currentRow;
				int duration = noteDuration;
				int pitch2 = rowToPitch(row);
				NoteEvent newNote = new NoteEvent(time, duration, pitch2, trackNumber);
				events.add(newNote);
				Collections.sort(events);
			}
		}
			
		else if ( mode == Mode.VOLUME) {
				synth.noteOff(trackNumber, 60);
				int time = currentColumn;
				int volume = rowToVolume(currentRow);
				VolumeEvent newVol = new VolumeEvent(time, volume, trackNumber);
				events.add(newVol);
				Collections.sort(events);
				
		}
			
		else if ( mode == Mode.COPY) {
				notesToCopy.clear();

				for (AudioEvent event : events) {
					if(event instanceof NoteEvent) {
						NoteEvent note = (NoteEvent) event;
						int col = note.getTime();
						int row = pitchToRow(note.getPitch());
						
						boolean colRange = (col >= copyFromColumn && col <= copyToColumn);
						boolean rowRange = (row >= copyFromRow && row <= copyToRow);
						
						if(colRange && rowRange) {
							notesToCopy.add(note);
							
						}
							
					}
					
				}
				
		 }
		 drawing = false;
		 repaint();


		 
		
		
		
	}

	@Override
	/**
	 *@return
	 */
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 *@return
	 */
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
