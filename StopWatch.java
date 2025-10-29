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
	// Holds the current time as a string, used for lap times
	private static String currentTime;
	
	// Stores the elapsed time in milliseconds. Crucial for pausing and resuming.
	private static long timeDifference = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Ensure the GUI is created on the Event Dispatch Thread
		// This is the standard and safe way to start a Swing application.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StopWatch frame = new StopWatch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Converts milliseconds into a time format HH:mm:ss.SSS.
	 * @param millis The duration in milliseconds.
	 * @return A string representing the time.
	 */
	private static String millisToTime(long millis){
        long seconds = millis/1000;
        millis %= 1000;
        long minutes = seconds/60;
	    seconds %= 60;
	    long hours = minutes/60;
        minutes %= 60;
        String second = (seconds<10) ? "0"+seconds : ""+seconds;
        String minute = (minutes<10) ? "0"+minutes : ""+minutes;
        String hour = (hours<10) ? "0"+hours : ""+hours;
        return String.format("%s:%s:%s.%s", hour, minute, second, millis);
	}
	/**
	 * Starts or resumes the stopwatch.
	 * @param textField_screen The text field to update with the current time.
	 * @throws InterruptedException if the thread is interrupted.
	 */
	private static void start(JTextField textField_screen) throws InterruptedException {
		isStarted = true;
		isPaused = false;
		// Calculate the effective start time.
		// If resuming, this subtracts the already elapsed time (timeDifference)
		// to ensure the timer continues from where it left off.
		long startTimeInMillis = System.currentTimeMillis()-timeDifference;
		// Loop as long as the stopwatch is running
		while (isStarted) {
			long currentTimeInMillis = System.currentTimeMillis();
			// Calculate total elapsed time and update the display and helper variables.
			timeDifference = currentTimeInMillis-startTimeInMillis;
			textField_screen.setText(millisToTime(timeDifference));
			currentTime = millisToTime(timeDifference);
			// Pause the thread for a short duration to prevent high CPU usage
			// and to create a periodic update cycle.
			Thread.sleep(100);
		}
	}
	/**
	 * Pauses the stopwatch.
	 * @param textField_screen The text field displaying the time.
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
		// Reset all state variables and the display to their initial values.
		isStarted = false;
		timeDifference = 0;
		currentTime = "00:00:00.000";
		textField_screen.setText("00:00:00.000");
	}

	/**
	 * Constructor to create the stopwatch frame and its components.
	 */
	public StopWatch() {
		// --- GUI Setup ---
		// Frame setup
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
				// Start the stopwatch only if it's not already running
				// This prevents multiple timer threads from being created.
				if (isStarted==false) {
					// Create and start a new thread for the stopwatch logic
					// This thread will run the start() method.
					startThread = new Thread(){@Override public void run(){ try {
						StopWatch.start(textField_screen);
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
				// Action is performed only if stopwatch was started or is paused
				if (isPaused || isStarted) {
					// Clear lap times and reset the display
					model.clear();
					restart(textField_screen);
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
				// Add the current time from the display to the lap list
				model.addElement("   " + currentTime);
			}
		});
		btnLap.setBounds(274, 12, 36, 34);
		panel_1.add(btnLap);
		
		// Pause button
		JButton btnPause = new JButton("");
		btnPause.setIcon(new ImageIcon("/home/itsme/Desktop/javaStopWatch/1491313935-pause_82989.png"));
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Pause the stopwatch only if it is currently running
				if (isStarted) {
					pause(textField_screen);
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