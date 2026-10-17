package assign12;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
/**
 * This class contains controls for each individual track and allow to draw a note and volume events for the track.
 * @author celia Ahui CS1420
 * @version 11-13-2025
 */

public class TrackPanel extends JPanel implements ActionListener {
	private int trackNumber;
	private SimpleSynthesizer synth;
	private TrackEditor editor;
    private ArrayList<AudioEvent> events;
    private JComboBox<String> instrumentCombo;
    private JLabel instrumentLabel;

    private JToggleButton muteButton;

    private JToggleButton noteModeButton;
    private JToggleButton volumeModeButton;
    private JToggleButton copyModeButton;
    private ButtonGroup modeGroup;

    private JButton clearButton;
	/**
	 * Constructor that create panel , buttons, label...
	 * @param trackNumber
	 * @param songLength
	 * @param events
	 * @param synth
	 */
    public TrackPanel(int trackNumber, int songLength,ArrayList<AudioEvent> events, SimpleSynthesizer synth) {
    	 this.trackNumber = trackNumber;
         this.synth = synth;
         this.events = events;
         this.editor = new TrackEditor(trackNumber, songLength, events, synth);

        
         this.setLayout(new BorderLayout());
         editor = new TrackEditor(trackNumber, songLength, events, synth);
         this.add(editor, BorderLayout.CENTER);
         JPanel controlPanel = new JPanel();
         controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
         instrumentLabel = new JLabel("Instrument:");
         controlPanel.add(instrumentLabel);
         instrumentCombo = new JComboBox<>(new Vector<String>(synth.getInstrumentNames()));
         controlPanel.add(instrumentCombo);
         muteButton = new JToggleButton("Mute");
         controlPanel.add(muteButton);
         
         noteModeButton = new JToggleButton("Note");
         volumeModeButton = new JToggleButton("Volume");
         copyModeButton = new JToggleButton("Copy/Paste");
         
         modeGroup = new ButtonGroup();
         modeGroup.add(noteModeButton);
         modeGroup.add(volumeModeButton);
         modeGroup.add(copyModeButton);
         
         noteModeButton.setSelected(true);
         controlPanel.add(noteModeButton);
         controlPanel.add(volumeModeButton);
         controlPanel.add(copyModeButton);
         clearButton = new JButton("Clear");
         controlPanel.add(clearButton);
         this.add(controlPanel, BorderLayout.NORTH);
         
         instrumentCombo.addActionListener(this);
         muteButton.addActionListener(this);
         noteModeButton.addActionListener(this);
         volumeModeButton.addActionListener(this);
         copyModeButton.addActionListener(this);
         clearButton.addActionListener(this);
         
        		 
         
      
    	
    }
    
    /**
     * This methods set the instrument in the SimpleSynthesizer nd update the JcompoBox
     * @param instrument
     */
    public void setInstrument(int instrument) {
    	synth.setInstrument(trackNumber, instrument);
    	
    	instrumentCombo.setSelectedIndex(instrument);
    	
    }
    /**
     * Modify the duration of a song
     * @param duration
     */
    public void setSongLength(int duration) {
    	editor.setSongLength(duration);
    	
    }

	/**
	 * Handles all button and combo box actions in this track panel.
     * Updates the synthesizer, editor mode, and track content based on user input.
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if(source == muteButton) {
			if(muteButton.isSelected()) {
				synth.setMute(trackNumber, true);
				System.out.println("Mute: true " );
			}
			else {
				synth.setMute(trackNumber, false);
				System.out.println("Mute: false " );
			}
		}
		
		else if (source == noteModeButton) {
			editor.setMode(TrackEditor.Mode.NOTE);
			System.out.println("Mode = NOTE");
		}
		
		else if (source == volumeModeButton) {
			editor.setMode(TrackEditor.Mode.VOLUME );
			System.out.println("Mode = VOLUME");
			
		}
		
		else if(source == copyModeButton) {
			editor.setMode(TrackEditor.Mode.COPY);
			System.out.println("Mode = COPY");
			
		}
		else if (source == clearButton) {
			editor.clearTrack();
			System.out.println("Track cleared");

		}
		
		else if (source == instrumentCombo) {
			int index = instrumentCombo.getSelectedIndex();
			synth.setInstrument(trackNumber, index);
			System.out.println("Instrument index = " + index);
		}
		
	}

	
	

}
