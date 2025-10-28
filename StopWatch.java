import java.util.Scanner;

/**
 * A simple command-line stopwatch application.
 */
public class StopWatch{

	/**
	 * The Chrono class contains all the logic for the stopwatch functionality.
	 */
	public static class Chrono {

		// A flag to control the running state of the stopwatch loop.
		// When true, the stopwatch is running. When false, it stops.
		public static boolean condition = true;

		/**
		 * Converts a duration in milliseconds to a formatted string HH:mm:ss.SSS.
		 * @param milles The duration in milliseconds.
		 * @return A formatted string representing the time.
		 */
		public static String millesToTime(long milles){
			// Calculate total seconds and remaining milliseconds
	        long seconds = milles/1000;
	        milles %= 1000;
			// Calculate total minutes and remaining seconds
	        long minutes = seconds/60;
    	    seconds %= 60;
			// Calculate total hours and remaining minutes
    	    long hours = minutes/60;
	        minutes %= 60;
			// Pad single-digit values with a leading zero for consistent formatting
	        String second = (seconds<10) ? "0"+seconds : ""+seconds;
	        String minute = (minutes<10) ? "0"+minutes : ""+minutes;
	        String hour = (hours<10) ? "0"+hours : ""+hours;
			// Return the formatted time string, using \r to overwrite the line on each update
	        return String.format("\r %s:%s:%s.%s", hour, minute, second, milles);
    	}

		/**
		 * Waits for user input (a space) to stop the stopwatch.
		 * This method is intended to be run in a separate thread.
		 */
		public static void stop(){
			Scanner input = new Scanner(System.in);
			// Waits for the user to press space and then enter
			if (input.nextLine().equals(" ")){
					// Set the condition to false to stop the main loop in run()
		    		condition = false;
					// Closing the scanner on System.in will close System.in for the entire application.
					input.close();
		    }
			System.out.println();
		} 

		/**
		 * Starts and runs the stopwatch.
		 * Prompts the user to start, then displays the elapsed time until stopped.
		 * @throws InterruptedException if the thread is interrupted while sleeping.
		 */
		public static void run() throws InterruptedException {
			System.out.print("press space and then enter to make the chrono starts: ");
			Scanner start = new Scanner(System.in);
			// Check if the user press space
			if (start.nextLine().equals(" ")){
				long startTimeInMillis = System.currentTimeMillis(); // Record the start time
				System.out.println("go!!!!");
				// Create and start a new thread to listen for the stop command
	        	Thread stopThread = new Thread(){@Override public void run(){ Chrono.stop();}};
	        	stopThread.start();
	        	System.out.println();
	            while(condition){ // Loop as long as the stopwatch is running
	                long currTimeInMillis = System.currentTimeMillis(); // Get current time
					// Calculate and print the elapsed time
        	        System.out.print(millesToTime(currTimeInMillis-startTimeInMillis));
	                Thread.sleep(100); // Pause for 100ms to control update frequency
	            }
				System.out.println("finish!!!!");
				stopThread.join(); // Wait for the stop thread to complete
			}
			else{
				// If the user doesn't type "start", recursively call run().
				// This could lead to a StackOverflowError with many invalid inputs.
				run();
			}
	    }
	}


	public static void main(String[] args) throws InterruptedException {
		// Create an instance of the Chrono class.
		// Since all members of Chrono are static, this isn't strictly necessary.
		Chrono ch = new Chrono();
		// Start the stopwatch logic.
		ch.run();
	}
}