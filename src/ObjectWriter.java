/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//import lab6_student_name.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author JOHN HENRY
 */
public class ObjectWriter {

    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    /**
     * Method 1 - to Open TCP socket to machine:217.73.66.75 on port; 7879
     *
     */
    public static void openTCP() throws IOException {

        //public Socket(InetAddress address, int port) throws IOException

        String address = "217.73.66.75";
        //String address = "localhost";
        int port = 7879;

        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + address);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + address);
            System.exit(1);
        }
        studentOuput();

        out.close();
        in.close();
        socket.close();
    }

    /**
     * Method 2 - to send student object populated with details
     */
    public static void studentOuput() {

        Student student = new Student();

        try {
            OutputStream streamOut = socket.getOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(streamOut);
            outputStream.writeObject(student.toString());
            
            outputStream.flush(); //
            System.out.println("Sent: " + student.getName());

            severResponse();
            outputStream.close();
        } catch (IOException ex) {
            System.out.println("ERR: Cannot perform output." + ex);
        }
    }

    /**
     * serverResponse - read response back from the server as a character string
     *
     * @throws IOException
     */
    public static void severResponse() throws IOException {

        String line;
        line = in.readLine();
        System.out.println("Received: " + line);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        openTCP();

    }
}
