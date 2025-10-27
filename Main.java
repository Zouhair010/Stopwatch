public class Main {
	/**
	 * This method implements a simple stopwatch functionality.
	 * It runs an infinite loop, incrementing seconds, minutes, and hours,
	 * and prints the elapsed time to the console every second.
	 * @throws InterruptedException if the thread is interrupted while sleeping.
	 */
	public static void chrono() throws InterruptedException {
	    // Initialize time units to zero.
	    int s=0; // seconds
	    int m=0; // minutes
	    int h=0; // hours

	    // Infinite loop to keep the stopwatch running.
	    while(true){
	        // Pause the execution for approximately one second.
	        // Note: Using 999ms might lead to a slight drift over time. 1000ms would be more accurate for a second.
	        Thread.sleep(999);
	        // Increment the second count.
	        s++;
	        // Check if 60 seconds have passed.
	        if (s%60==0){
	            // Reset seconds to 0 and increment minutes.
	            s=0;
	            m++;
	            // Check if 60 minutes have passed.
	            if (m%60==0){
	                // Reset minutes to 0 and increment hours.
	                m=0;
	                h++;
	            }
	        }
	        // Format the seconds, minutes, and hours to always have two digits (e.g., "01", "09", "10").
	        String sc = (s<10) ? "0"+s : ""+s;
	        String mn = (m<10) ? "0"+m : ""+m;
	        String hr = (h<10) ? "0"+h : ""+h;
	        // Print the formatted time to the console.
	        // The '\r' (carriage return) moves the cursor to the beginning of the line,
	        // which allows the new time to overwrite the previous one.
	        System.out.print("\r"+hr+":"+mn+":"+sc);
	    }
	}
	// The main method, entry point of the program.
	public static void main(String[] args) throws InterruptedException {
		// Start the stopwatch.
		chrono();
	}
}