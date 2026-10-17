package assign12;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Main application window for SoundSketcher.
 * Shows the controls and the track editor tabs.
 * @author Celia Ahui CS1420
 * @version 12-02-2025
 */

public class SoundSketcherFrame extends JFrame implements ActionListener, ChangeListener{
	private static final int DEFAULT_TEMPO= 300;
	private static final int DEFAULT_DURATION = 16;
	private Song song;
	private TrackPanel[]trackPanels;
	private JToggleButton playButton;
	private JToggleButton loopButton;
	private JSlider tempoSlider;
	private JLabel tempoLabel;
	private JMenuItem saving;
	private JMenuItem loading;
	private JMenu menu;
	private JMenuBar menuBar;
	

	private JSpinner lengthSpinner;
	private JLabel lengthLabel;
	private JTabbedPane tabbedPane;

    /**
     * Builds the main SoundSketcher window.
     * Initializes the Song, creates all GUI controls, sets up the layout,
     * and adds the TrackPanels and tabbed pane to the frame.
     */
	public SoundSketcherFrame() {
		song = new Song(DEFAULT_TEMPO, DEFAULT_DURATION);
		trackPanels = new TrackPanel[10];
		for (int i = 0; i < 10; i++) {
		    trackPanels[i] = new TrackPanel(i,song.getSongLength(),song.getTrack(i),song.getSynthesizer());
		}
		
		this.setPreferredSize(new Dimension(800,800));
		this.setTitle("SoundSketcher");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menu = new JMenu("File");
		menuBar =  new JMenuBar();
		menuBar.add(menu);
		
		
		saving= new JMenuItem("Save");
		saving.addActionListener(this);
		loading = new JMenuItem("Load");
		saving.addActionListener(this);
		
		menu.add(saving);
		menu.add(loading);
		this.setJMenuBar(menuBar);
	
		
	
		
		
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		playButton = new JToggleButton("Play"); 
		controlPanel.add(playButton); 
		
		loopButton = new JToggleButton("Loop"); 
		controlPanel.add(loopButton);
		
		tempoLabel = new JLabel("Tempo:");   
		controlPanel.add(tempoLabel); 
		
		tempoSlider = new JSlider(20, 600, DEFAULT_TEMPO);
		tempoSlider.setMajorTickSpacing(100);
		tempoSlider.setMinorTickSpacing(20); 
		tempoSlider.setPaintTicks(true); 
		tempoSlider.setPaintLabels(true); 
		controlPanel.add(tempoSlider);  
		
		
		lengthLabel = new JLabel("Length:");              
		controlPanel.add(lengthLabel);  
		lengthSpinner = new JSpinner(new SpinnerNumberModel( DEFAULT_DURATION,4,1024,4));
		controlPanel.add(lengthSpinner); 
		
		this.setLayout(new BorderLayout());
	    this.add(controlPanel, BorderLayout.NORTH);
	    
	    tabbedPane = new JTabbedPane();  
	    for (int i = 0; i < trackPanels.length; i++) {
	        tabbedPane.addTab("Track " + (i + 1), trackPanels[i]);
	    }
	  
	   
	    this.add(tabbedPane, BorderLayout.CENTER);
	    
	    playButton.addActionListener(this);
	    loopButton.addActionListener(this); 
	    tempoSlider.addChangeListener(this);
	    lengthSpinner.addChangeListener(this);
	    
	    
	    
	    this.setVisible(true);
		this.pack();
		
	}


	/**
	 * Handles changes from the tempo slider and length spinner.
     * Updates the Song's tempo or length and refreshes all TrackPanels.
	 */
	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		
		if(source == tempoSlider) {
			int tempo = tempoSlider.getValue();
			song.setTempo(tempo);
		}
		
		else if (source == lengthSpinner) {
			int value = (Integer) lengthSpinner.getValue();
			song.setSongLength(value);
			
			for(int i = 0; i < trackPanels.length; i ++) {
				if(trackPanels[i] != null) {
					trackPanels[i].setSongLength(value);
				}
			}
			
		}
		
		
		
	}
	/**
	 * checks whether the new tempo is within the slider's range and adjusts it accordingly
	 * @param newTempo
	 */
	private void setTempoSlider(int newTempo) {
	    if(newTempo < tempoSlider.getMinimum())
	        tempoSlider.setMinimum(newTempo);
	    else if(newTempo > tempoSlider.getMaximum())
	        tempoSlider.setMaximum(newTempo);
	    tempoSlider.setValue(newTempo);
	}


	/**
	 * Responds to button and combo box actions.
     * Handles play/stop, loop toggle, and other control interactions.
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if( source == playButton) {
			if(playButton.isSelected()) {
				song.play();
				playButton.setText("Stop");
			}
			
			else {
				song.stop();
				playButton.setText("Play");
				
			}
			
		}
		
		else if (source == loopButton) {
			if(loopButton.isSelected()) {
				song.enableLoop(true);
			}
			else {
				song.enableLoop(false);
			}
		}
		
		else if(source == loading) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Song files", "song"));
			int result = chooser.showOpenDialog(this);
			 if (result == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = chooser.getSelectedFile();
		            SongFiles.readFile(selectedFile, song);
		            setTempoSlider(song.getTempo());
		            SimpleSynthesizer synth = song.getSynthesizer();
		            
		            for (int i = 0; i < trackPanels.length; i++) {
		                int instrIndex = synth.getInstrument(i);
		                trackPanels[i].setInstrument(instrIndex);
		            }
		            
		            for (int i = 0; i < trackPanels.length; i++) {
		                trackPanels[i].setSongLength(song.getSongLength());
		                trackPanels[i].repaint();
		            }
		            
			 }
		}
		
		else if(source == saving) {
			
			 JFileChooser chooser = new JFileChooser();
		        chooser.setFileFilter(new FileNameExtensionFilter("Song files", "song"));

		        int result = chooser.showSaveDialog(this);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = chooser.getSelectedFile();

		            
		            if (!selectedFile.getName().toLowerCase().endsWith(".song")) {
		                selectedFile = new File(selectedFile.getAbsolutePath() + ".song");
		            }

		            SongFiles.writeFile(selectedFile, song);
		        }
			
		}
		
		
		
		
	}
	
	

}
