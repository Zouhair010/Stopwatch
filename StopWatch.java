/**
 * A simple stopwatch implementation that prints elapsed time to the console.
 * This version runs indefinitely until the program is manually stopped.
 */
public class StopWatch{
	/**
	 * Converts a duration in milliseconds to a formatted string HH:MM:SS.ms.
	 * @param milles The duration in milliseconds.
	 * @return A formatted string representing the time.
	 */
	public static String millesToTime(long milles){
	    
	    // Calculate total seconds from milliseconds
	    long seconds = milles/1000;
	    // Get remaining milliseconds
	    milles %= 1000;
	    // Calculate total minutes from seconds
	    long minutes = seconds/60;
	    // Get remaining seconds
	    seconds %= 60;
	    // Calculate total hours from minutes
	    long hours = minutes/60;
	    // Get remaining minutes
	    minutes %= 60;
	    
	    // Pad single-digit seconds, minutes, and hours with a leading zero for consistent formatting.
	    String second = (seconds<10) ? "0"+seconds : ""+seconds;
	    String minute = (minutes<10) ? "0"+minutes : ""+minutes;
	    String hour = (hours<10) ? "0"+hours : ""+hours;

	    // Return the formatted time string. The '\r' character moves the cursor 
	    // to the beginning of the line, allowing the time to be overwritten on the same line.
	    return String.format("\r %s:%s:%s.%s", hour, minute, second, milles);
	}

	/**
	 * Starts the stopwatch. It runs in an infinite loop, continuously printing
	 * the elapsed time since it was started.
	 * @throws InterruptedException if the thread is interrupted while sleeping.
	 */
	public static void chrono() throws InterruptedException {
	    // Record the start time in milliseconds.
	    long startTimeInMillis = System.currentTimeMillis();
	    // Infinite loop to keep the stopwatch running.
	    while(true){
	        // Get the current time.
	        long currTimeInMillis = System.currentTimeMillis();
	        // Calculate elapsed time and print it in the desired format.
	        System.out.print(millesToTime(currTimeInMillis-startTimeInMillis));
	        // Pause the thread for 100 milliseconds to control the update frequency.
	        Thread.sleep(100);
	    }
	}

	/**
	 * The main entry point for the application.
	 * @param args Command line arguments (not used).
	 * @throws InterruptedException if the thread is interrupted.
	 */
	public static void main(String[] args) throws InterruptedException {
		// Start the chronometer.
		chrono();
	}
}