

/**
 * Same student we had before but now we've implemented the Serializable 
 * interface to allow the class to be converted to IO Streams
 * @author alex
 */
import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = -1848148348931789644L;     
    public static int instances = 0;
    
    // Student's ID
    private final int id; 
    // Current counter value
    private int counter = 0;
    // Counter starting value
    private int counter_from = 0;
    // Counter final value
    private int counter_to = 0;
    // By how much the counter gets incremented on the 'increment' call.
    private int counter_increment = 0;
    // Student's name
    private String name;
    // Student's date of birth
    private String dob;  
    // Student's gender
    private String course;  
    // Student's gender
    private String gender; 
    
    // Getters
    public int getID(){
        return this.id;
    }
    public String getDOB(){
        return this.dob;
    }
    public String getName(){
        return this.name;
    }
    public String getCourse(){
        return this.course;
    }
    public int getStudentId(){
        return this.id;
    }
    public int getCounter(){
        return this.counter;
    }
    public int getCounterFrom(){
        return this.counter_from;
    }
    public int getCounterTo(){
        return this.counter_to;
    }
    public int getCounterIncrement(){
        return this.counter_increment;
    }
    
    // Setters
    public void setDOB(String dob){
        this.dob = dob;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setCourse(String course){
        this.course = course;
    }
    public void setCounter(int counter){
        this.counter = counter;
    }
    public void setCounterFrom(int counter_from){
        this.counter_from = counter_from;
    }
    public void setCounterTo(int counter_to){
        this.counter_to = counter_to;
    }
    public void setCounterIncrement(int counter_increment){
        this.counter_increment = counter_increment;
    }
    
    /**
     * Default constructor. Populates age and gender with defaults
     */
    public Student(){
        id = instances;
        this.dob = "08-09-1990";
        this.name = "Not Set";
        instances++;
    }
   
    /**
     *
     * @param id
     * @param dob
     * @param name
     * @param counter_from
     * @param counter_to
     * @param counter_increment
     */
    public Student (int id, String dob, String name, String course, int counter_from, int counter_to, int counter_increment){
        this.id = id;
        this.dob = dob;
        this.name = name;
        this.course = course;
        this.counter_from = counter_from;
        this.counter_to = counter_to;
        this.counter_increment = counter_increment;
        // set the current counter value to counter_from when the student is instantiated.
        this.counter = counter_from;
        instances++;
    }
    
    
    protected void finalize() throws Throwable{
        //do finalization here
        super.finalize(); //not necessary if extending Object.
    } 
    
    /*************************************************************************
     * My Methods:
     * 
     *************************************************************************/

    public String toString (){
        return "Name: " + this.name + " Date of birth: " + this.dob + " Gender: " 
                + this.gender ;
    }
}
