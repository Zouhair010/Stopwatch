import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;

public class StopWatch extends JFrame {

	// Unique ID for serialization
	private static final long serialVersionUID = 1L;
	// Main panel for the frame
	private JPanel contentPane;
	// Text field to display the stopwatch time
	private JTextField textField_screen;
	// Model for the JList to store lap times
	private DefaultListModel<String> model;
	// List to display lap times
	private JList<String> list;
	
	// Thread that runs the stopwatch timer
	private static Thread startThread ;
	// Flag to indicate if the stopwatch is running
	private static boolean isStarted;
	// Flag to indicate if the stopwatch is paused
	private static boolean isPaused;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Ensure the GUI is created on the Event Dispatch Thread
		// This is the standard and safe way to start a Swing application.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StopWatch1 frame = new StopWatch1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Checks if the given object represents a single digit.
	 * This method handles String, Character, and Integer types.
	 * @param obj The object to check.
	 * @return true if the object is a digit, false otherwise.
	 */
	private static boolean isDigit(Object obj){
        if (obj instanceof String){
            try{
                Integer n =  Integer.valueOf(obj.toString());
                if(n.toString().equals(obj)){
                    return true;
                }
            }
            catch (NumberFormatException e){
                return false;
            }
        }
        else if (obj instanceof Character){
            try{
                Integer n = Integer.valueOf(obj.toString());
                Character c = n.toString().toCharArray()[0];
                if(c.equals(obj)){
                    return true;
                }
            }
            catch (NumberFormatException e){
                return false;
            }
        }
        else if (obj instanceof Integer){
            return true;
        }
        return false;
    }
	/**
	 * Converts milliseconds into a time format HH:mm:ss.SSS.
	 * @param milles The duration in milliseconds.
	 * @return A string representing the time.
	 */
	private static String millesToTime(long milles){
        long seconds = milles/1000;
        milles %= 1000;
        long minutes = seconds/60;
	    seconds %= 60;
	    long hours = minutes/60;
        minutes %= 60;
        String second = (seconds<10) ? "0"+seconds : ""+seconds;
        String minute = (minutes<10) ? "0"+minutes : ""+minutes;
        String hour = (hours<10) ? "0"+hours : ""+hours;
        return String.format("%s:%s:%s.%s", hour, minute, second, milles);
	}
	/**
	 * Converts a time string in HH:mm:ss.SSS format to milliseconds.
	 * @param time The time string to convert.
	 * @return The total time in milliseconds.
	 */
	private static long timeToMillis(String time) {
		if (time.length()==0) {
			return 0;
		}
		long millis;
		ArrayList<Integer> timeParts = new ArrayList<Integer>();
		ArrayList<String> part = new ArrayList<String>();
		// Parse the time string by splitting on non-digit characters.
		for (char ch : time.toCharArray()) {
			if (isDigit(ch)) {
				part.add(""+ch);
			}
			else {
				timeParts.add(Integer.valueOf(String.join("", part)));
				part = new ArrayList<String>();
			}
		}
		timeParts.add(Integer.valueOf(String.join("", part)));
		// Calculate total milliseconds from hours, minutes, seconds, and milliseconds.
		millis = (timeParts.get(0)*3600000)+(timeParts.get(1)*60000)+(timeParts.get(2)*1000)+timeParts.get(3);
		return millis;
	}
	/**
	 * Starts or resumes the stopwatch.
	 * @param textField_screen The text field to update with the current time.
	 * @throws InterruptedException if the thread is interrupted.
	 */
	private static void start(JTextField textField_screen) throws InterruptedException {
		isStarted = true;
		isPaused = false;
		// Calculate the effective start time by subtracting the already elapsed time.
		// This allows the timer to resume from where it was paused.
		long startTimeInMillis = System.currentTimeMillis()-timeToMillis(textField_screen.getText());
		// Loop as long as the stopwatch is running.
		while (isStarted) {
			long currentTimeInMillis = System.currentTimeMillis();
			// Calculate total elapsed time and update the display.
			textField_screen.setText(millesToTime(currentTimeInMillis-startTimeInMillis));
			// Pause the thread briefly to prevent high CPU usage and to create a periodic update cycle.
			Thread.sleep(100); 
		}
	}
	/**
	 * Pauses the stopwatch.
	 * @param textField_screen The text field displaying the time (not used here but kept for signature consistency).
	 */
	private static void pause(JTextField textField_screen) {
		// Set flags to stop the timer loop in the start() method.
		isStarted = false;
		isPaused = true;
	}
	/**
	 * Stops and resets the stopwatch to zero.
	 * @param textField_screen The text field to reset.
	 */
	private static void restart(JTextField textField_screen) {
		// Reset running flag and the display to its initial state.
		isStarted = false;
		textField_screen.setText("00:00:00.000");
	}

	/**
	 * Constructor to create the stopwatch frame and its components.
	 */
	public StopWatch1() {
		setResizable(false);
			
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		// Using absolute positioning for layout
		panel.setBounds(24, 12, 402, 47);
		contentPane.add(panel);
		panel.setLayout(null);
		
		// Time display text field
		textField_screen = new JTextField();
		textField_screen.setForeground(new Color(0, 153, 204));
		textField_screen.setFont(new Font("Dialog", Font.BOLD, 21));
		textField_screen.setHorizontalAlignment(SwingConstants.CENTER);
		textField_screen.setEditable(false);
		textField_screen.setText("00:00:00.000");
		textField_screen.setBounds(90, 0, 220, 47);
		panel.add(textField_screen);
		textField_screen.setColumns(10);
		
		// Panel for control buttons
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(24, 60, 402, 58);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		// Start button
		JButton btnStart = new JButton("");
		btnStart.setIcon(new ImageIcon("/home/itsme/Desktop/javaStopWatch/1491313953-play_82992.png"));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Start the stopwatch only if it's not already running.
				// This prevents multiple timer threads from being created.
				if (isStarted==false) {
					// Create and start a new thread for the stopwatch logic.
					// This thread will run the start() method.
					startThread = new Thread(){@Override public void run(){ try {
						StopWatch1.start(textField_screen);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}}};
					startThread.start();
				}
			}
		});
		
		btnStart.setBounds(91, 12, 36, 34);
		panel_1.add(btnStart);
		
		// Restart button
		JButton btnRestart = new JButton("");
		btnRestart.setIcon(new ImageIcon("/home/itsme/Desktop/javaStopWatch/1491313942-replay_83001.png"));
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Action is performed only if stopwatch was started or is paused.
				if (isPaused==true || isStarted==true) {
					// Clear lap times and reset the display.
					model.clear();
					restart(textField_screen);
					// The logic below seems to attempt to manage the thread on restart, but it's complex and may not be necessary.
					try {
						startThread.join();
						startThread = new Thread(){@Override public void run(){ try {
							StopWatch1.start(textField_screen);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}}};
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnRestart.setBounds(216, 12, 36, 34);
		panel_1.add(btnRestart);
		
		// Lap button
		JButton btnLap = new JButton("");
		btnLap.setIcon(new ImageIcon("/home/itsme/Desktop/javaStopWatch/1491313940-repeat_82991.png"));
		btnLap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Add the current time from the display to the lap list.
				model.addElement("   "+textField_screen.getText());
			}
		});
		btnLap.setBounds(274, 12, 36, 34);
		panel_1.add(btnLap);
		
		// Pause button
		JButton btnPause = new JButton("");
		btnPause.setIcon(new ImageIcon("/home/itsme/Desktop/javaStopWatch/1491313935-pause_82989.png"));
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Pause the stopwatch only if it is currently running.
				if (isStarted==true) {
					pause(textField_screen);
					// This logic seems to try to manage the thread on pause, which is unusual.
					// Typically, the running thread would just exit its loop.
					try {
						startThread.join();
						startThread = new Thread(){@Override public void run(){ try {
							StopWatch1.start(textField_screen);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}}};
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnPause.setBounds(150, 12, 36, 34);
		panel_1.add(btnPause);
		
		// Scroll pane for the lap times list
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 130, 402, 85);
		contentPane.add(scrollPane);
		// List to display lap times
		model = new DefaultListModel<String>();
		list = new JList<String>(model);
		list.setFont(new Font("Dialog", Font.BOLD, 17));
		list.setSelectionForeground(new Color(0, 153, 204));
		list.setForeground(new Color(0, 153, 204));
		scrollPane.setViewportView(list);
		list.setBackground(UIManager.getColor("Button.background"));
		
		
	}
}
