import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;


/**
 * @author Karlis Vigulis
 */
public class RequestThread extends Thread{
    /* Socket object */
    Socket soc;      
    /* Keeps track of the number of threads that are active */
    private static int threadsRunning = 0; 
    /* Thread identifier */
    private int threadId = 0;
    /* Is the thread running */
    private boolean active = false;
    /* Defined here to be seen by functions to serve 500.html page when errors are cought */
    private static PrintWriter output;
    
    /* Getters & setters */
    public static int getThreadsRunning(){
        return threadsRunning;
    }
    public int getThreadId(){
        return threadId;
    }
    public boolean getActive (){
        return this.active;
    }    
    public void setActive(boolean active){
        this.active = active;
    }
    
    /** Thread constructor with Student object parameter.
    * @param soc TCP socket used in this thread.
    */
    public RequestThread(Socket soc){
        this.soc = soc;         
    }    
    

    /**
     * This method gets called by Thread.start() from main class
     */
    @Override    
    public void run(){  
        threadsRunning++; // We now have more threads
        this.threadId = threadsRunning;
        this.setActive(true);        
        try{  
            System.out.println("THREAD: " + this.threadId + " of " + threadsRunning + " was started.");
            BufferedReader input = new BufferedReader(new InputStreamReader(soc.getInputStream()));            
            output = new PrintWriter(soc.getOutputStream(), true); // Character stream

            String line;
            long time = System.currentTimeMillis();
            // Get everything that the client sends and dump it into the string until we get a blank line
            ArrayList<String> http_request_lines = new ArrayList<>();
            while ((line = input.readLine()).length() > 0) {
                // Output the request to console
                System.out.println("[" + line + "]");
                http_request_lines.add(line);
            }
            
            // Fetch the request URL suffix
            String request_url = http_request_lines.get(0).split(" ")[1];            
            String html;
            String html2;
            
            if ("/getStudents".equals(request_url)){                    
                    try{
                        // Text that gets inserted into the HTML response's title and heading 
                        String request_type = "All";
                        // MySQL query that gets sent to the student database
                        String mySQL_query = "SELECT * FROM  students order by id asc;";         
                        // Make a MySQL query, get results and do HTTP POST with a populated HTML page  
                        getStudentsAndPOST(mySQL_query, request_type);
                    } catch (Exception e) {
                        // POST the internal server error message to the client
                        post500(); 
                        System.out.println(EchoServer_HTTP.getDate() + " - Exception in thread #: " + this.threadId + "; Error message: " + e);                      
                    }                                           
            }else if ("/getStudents=math".equals(request_url)){
                try{
                        String request_type = "Math";
                        // MySQL query that gets sent to the student database
                        String mySQL_query = "SELECT * FROM  students WHERE course = 'MathStudent' order by id asc ;";                        
                        // Make a MySQL query, get results and do HTTP POST with a populated HTML page  
                        getStudentsAndPOST(mySQL_query, request_type);
                    } catch (Exception e) {
                        post500();
                        System.out.println(EchoServer_HTTP.getDate() + " - Exception in thread #: " + this.threadId + "; Error message: " + e); 
                    }
                        
                
            }else if ("/getStudents=science".equals(request_url)){
                try{
                        String request_type = "Science";
                        // MySQL query that gets sent to the student database
                        String mySQL_query = "SELECT * FROM  students WHERE course = 'ScienceStudent' order by id asc ;";                        
                        // Make a MySQL query, get results and do HTTP POST with a populated HTML page  
                        getStudentsAndPOST(mySQL_query, request_type); 
                    } catch (Exception e) {                         
                        post500();
                        System.out.println(EchoServer_HTTP.getDate() + " - Exception in thread #: " + this.threadId + "; Error message: " + e); 
                    }
                    
                
            }else if ("/getStudents=computer".equals(request_url)){
                try{
                        String request_type = "Computer";
                        // MySQL query that gets sent to the student database
                        String mySQL_query = "SELECT * FROM  students WHERE course = 'ComputerStudent' order by id asc ;";
                        // Make a MySQL query, get results and do HTTP POST with a populated HTML page  
                        getStudentsAndPOST(mySQL_query, request_type);              
                    } catch (Exception e) {
                        post500();
                        System.out.println(EchoServer_HTTP.getDate() + " - Exception in thread #: " + this.threadId + "; Error message: " + e); 
                    }
                                   
            }else{
                // If not found "HTTP/1.0 404 Not Found" and HTML code below tailored to print "Page not found"
                html = "HTTP/1.0 404 Not Found\n\n";                
                html2 = readHTML("404.html");                    
                output.write(html + html2); // Write the characters of html to the socket                       
            }
            output.flush();
            output.close(); 
            input.close();
            System.out.println(EchoServer_HTTP.getDate() + " - Request processed in: " + ((System.currentTimeMillis() - time)) + " ms");
            System.out.println("Thread: " + this.threadId + " FINISHED\n\n");
        } catch (IOException e) {            
            post500();
            System.out.println(EchoServer_HTTP.getDate() + " - Exception in thread #: " + this.threadId + "; Error message: " + e); 
        }
        threadsRunning--;
        this.setActive(false);
    }  
    
    /**
     * Method to read a file from /src/ directory as 
     * @param path points the file location
     * @return Returns a string
     */    
    public static String readHTML(String path){
        String htmlStr = "";
        String root = "";
        try {  
            root = System.getProperty("user.dir") + "\\src\\";
            htmlStr = FileUtils.readFileToString(new File(root+path), "UTF-8");
            System.out.println(EchoServer_HTTP.getDate() + " - File '" + root+path + "' was requested from the server.");
        } catch(IOException e){              
            System.out.println(EchoServer_HTTP.getDate() + " - ERROR: File '" + root+path + "' not found.");
        }
        return htmlStr;
    }    

    
    /**
     * Method to insert text between 'block tags' of the html String 
     * @param base_html Original HTML string
     * @param delimiter Block's delimiter tag identifier
     * @param block A string to be inserted as a block
     * @return Returns the new HTML string with the inserted block
     */
    public static String insertBlock(String base_html, String delimiter, String block){
        delimiter = "<!--" + delimiter + "-->";        
        String[] parts = base_html.split(delimiter);
        String new_html = parts[0] + block + parts[2];
        return new_html;
    }

    
    /**
     * A method that generates HTML code block which get's inserted into the table.    
     * @param studentList List of students used to populate table cells
     * @return Returns HTML table rows populated with student information
     */
    public static String generateTableHTML(ArrayList<Student> studentList){
        String tableContents = "";
        for (int idx = 0; idx < studentList.size(); idx++){                                                      
                            tableContents = tableContents + "<tr>\n" +
                                    "<td>"+studentList.get(idx).getID()+"</td>\n" +
                                    "<td>"+studentList.get(idx).getName().split(" ")[0]+"</td>\n" +
                                    "<td>"+studentList.get(idx).getName().split(" ")[1]+"</td>\n" +
                                    "<td>"+studentList.get(idx).getDOB()+"</td>\n" +
                                    "<td>"+studentList.get(idx).getCourse()+"</td>\n" +    
                                    "<td>"+studentList.get(idx).getCounterFrom()+"</td>\n" +  
                                    "<td>"+studentList.get(idx).getCounterTo()+"</td>\n" +  
                                    "<td>"+studentList.get(idx).getCounterIncrement()+"</td>\n" +  
                                    "</tr>\n";
                        } 
        return tableContents;
    } 
    
    /**
     * A method that generates HTML code block which gets inserted into the table.
     * @param mySQL_query MySQL query that gets sent to the database
     * @param request_type Gets inserted into the web page's title. Possible choices are: "All"; "Math"; "Science" and "Computer".
     */
    public static void getStudentsAndPOST(String mySQL_query, String request_type){
        // Connect to a database and retieve students using a synchronised method.
        ArrayList<Student> studentList = DatabaseQuery.connectToDBAndQuery(mySQL_query, EchoServer_HTTP.DRIVER,
                EchoServer_HTTP.URL, EchoServer_HTTP.DB, EchoServer_HTTP.USER, EchoServer_HTTP.PASS);               
        // My HTML code
        String html = "HTTP/1.0 200 OK\n\n";
        // If not found "HTTP/1.0 404 Not Found" and HTML code below tailored to print "Page not found"                        
        String html2 = readHTML("index_base.html"); 
        // Set the page's title to all
        html2 = insertBlock(html2, "%request_type_title%", request_type); 
        // Insert the request type into the main heading, e.g. <All> Students.
        html2 = insertBlock(html2, "%request_type_text%", request_type);                       
        // Get HTML table contents       
        String tableContents = generateTableHTML(studentList);                                            
        // Populate the HTML table                        
        html2 = insertBlock(html2, "%student_table%", tableContents);                        
        output.write(html + html2); // Write the characters of html to the socket      
    } 
    
    /**
     * Method that post a 500 Internal Server Error message to the client    
     */
    public static void post500(){
        String html = "HTTP/1.0 500 Internal Server Error\n\n";
        String html2 = readHTML("500.html"); 
        output.write(html + html2); // Write the characters of html to the socket                                                                                   
    }
    
    
    
    
}
