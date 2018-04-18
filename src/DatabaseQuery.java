import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Karlis Vigulis
 */
public class DatabaseQuery {
    
    /**
    * A method that tries to connect to a remote MySQL database, do a query and populate an
    * ArrayList with the students contained in the database's student table.
    * This is a synchronised method and can be run only by one Thread at a time.
    * @param mySQL_query Query that gets sent to the database
    * @param driver A Java MySQL driver location.
    * @param url A URL to the host of the MySQL database.
    * @param db Name of the database.
    * @param user Username with access to the database.
    * @param pass User's password.
    * @return List of received and parsed Student objects
    */
    public static synchronized ArrayList<Student> connectToDBAndQuery(String mySQL_query, String driver, String url, String db, String user, String pass){
        ArrayList<Student> studentList = new ArrayList<>();
        try {
            /* Get the MySQL driver loaded. */
            Class.forName(driver).newInstance();
            /* Get the connection from the DriverManager. */
            Connection con = DriverManager.getConnection(url + db, user, pass);
                        
            try {
                Statement st = con.createStatement();
                // Execute a query on the database
                ResultSet rs = st.executeQuery(mySQL_query);
                
                // Print retrieved student table's header
                System.out.println(EchoServer_HTTP.getDate() + " - [INFO]: MySQL Query \"" + mySQL_query + "\" was sent to the database.");
                System.out.println(EchoServer_HTTP.getDate() + " - [INFO]: MySQL Query response results for : Student table : \n==============================="
                        + "==================================================================\n"
                        + "Id\tName\t\tDate of birth\tCourse\t\tCounter from\tCounter to\tCounter increment");
              
                // While ResultSet has next row do ...
                while (rs.next()) {                    
                    /* Add a student to the ArrayList from the row at current cursor's
                    position */
                    parseStudentRowIntoObject(studentList, rs);                      
                }  
                // Close the connection to the database
                con.close();
            } catch (SQLException se) {
                 System.out.println(EchoServer_HTTP.getDate() + " - SQL ERR: " + se);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            System.out.println(EchoServer_HTTP.getDate() + " - ERR: " + e);
        }
        
        return studentList;
    }   
    
    
    /**
    * A method tries to read a row from ResultSet, store the column values in variables
    * and create a corresponding type of student depending on the course column value.
    * The other column values are fed to the student's constructor.
    * @param studentList A dynamic list which gets appended with a student in this method
    * @param rs ResultSet with a cursor at a particular row.
    */
    public static void parseStudentRowIntoObject(ArrayList<Student> studentList, ResultSet rs){
        try {            
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String course = rs.getString("course"); 
            int counter_from = Integer.parseInt(rs.getString("counter_from")); 
            int counter_to = Integer.parseInt(rs.getString("counter_to"));
            int counter_increment = Integer.parseInt(rs.getString("counter_increment"));
            String dob = rs.getString("dob");
            // Calculate age given date of birth.
             
            switch (course){           
                case "MathStudent": // MathStudent
                    studentList.add(new MathStudent(id, dob, name, course, counter_from, counter_to, counter_increment));                                   
                    break;
                case "ComputerStudent": // ComputerStudent                    
                    studentList.add(new ComputerStudent(id, dob, name, course, counter_from, counter_to, counter_increment));                    
                    break;
                case "ScienceStudent": // ComputerStudent                    
                    studentList.add(new ScienceStudent(id, dob, name, course, counter_from, counter_to, counter_increment));                    
                    break;
                    
                default:                    
                    System.out.println(EchoServer_HTTP.getDate() + " - [WARNING] Course field not recognised. This student entry will be skipped.");                     
                    break;                    
            }
            System.out.println(id + "\t" + name + "\t" + dob + "\t" + course + "\t"
                    + counter_from + "\t\t" + counter_to + "\t\t" + counter_increment);                    
            
        }catch (SQLException se) {
                 System.out.println(EchoServer_HTTP.getDate() + " - SQL ERR: " + se);
        }
    }  
    
}
