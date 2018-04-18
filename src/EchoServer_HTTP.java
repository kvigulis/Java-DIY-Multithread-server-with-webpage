import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Karlis Vigulis
 */
class EchoServer_HTTP {
    // Defined as public so they can be read from within Threads.
    public static final int SOCKET_PORT = 7879;
    public static final String URL = "jdbc:mysql://city.ac.telecom2.net:63006/"; // The connection string        
    public static final String DB = "cityuniversity";                // Which Database?
    public static final String DRIVER = "com.mysql.jdbc.Driver";        
    public static final String USER = "javastudent";                 // Username for authorisation
    public static final String PASS = "kdjf878324jkdf";              // Password
        
    /**
     * Get a string representation of now based on date format
     * @return String
     */
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }    

    /**
     * Setup a server socket and accept connections
     */
    public static void main(String args[]) throws IOException {
              
        // My server socket
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(SOCKET_PORT);
        } catch (IOException ioe) {
            System.out.println(getDate() + " Error finding port");
            System.exit(1);
        }
        // Get my client socket
        Socket soc;
        System.out.println(getDate() + " - SERVER IS RUNNING");
        System.out.println("Ready for new HTTP connection on port " + SOCKET_PORT + " ...");
                
        // Run server indefinately and wait for new connections
        while (true){            
            try {
                //Program will stop here until we get a connection                 
                soc = ss.accept(); // Once read -> start up a thread                
                RequestThread thread = new RequestThread(soc);
                // Start the newest thread
                thread.start();
                
                System.out.println(getDate() + " Connection accepted at :" + soc);
            } catch (IOException ioe) {
                System.out.println(getDate() + "Server failed to accept");
                System.exit(1);
            }
        }              
    }    
}
