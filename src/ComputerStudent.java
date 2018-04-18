
/**
 * This class is used to store information a about a Computer science student.
 * @author Karlis Vigulis
 */
public class ComputerStudent extends Student{
       
    /**
     * Default constructor
     */
    public ComputerStudent() {
        super();
    } 
    
    /** 
     * Constructor with parameters for counting 
     * @param id Student's id
     * @param dob
     * @param name Student's name
     * @param course     
     * @param counter_from Counter starting value
     * @param counter_to Counter final value
     * @param counter_increment
    */
    public ComputerStudent (int id, String dob, String name, String course, int counter_from, int counter_to, int counter_increment){
        super(id, dob, name, course, counter_from, counter_to, counter_increment);
    }  
    
    
    /** 
     * Display student class type
     */
    public void displayStudent(){
        //super.displayStudent();
        String type = "ComputerStudent.class";
        System.out.println(type);
    }    
    
    /**
     * @return The string representation of the Computer student
     */    
    public String toString(){
        return super.toString();        
    }    
}
