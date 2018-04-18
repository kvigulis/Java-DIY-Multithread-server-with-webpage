

/**
 * This class is used to store information a about a Math student.
 * @author Karlis Vigulis
 */
public class MathStudent extends Student {
           
    /**
     * Default constructor
     */
    public MathStudent() {
        super();
    }    
    
    /** 
     * Constructor with parameters for counting 
     * @param id Student's id 
     * @param dob Student's date of birth
     * @param name Student's name
     * @param course     
     * @param counter_from Counter starting value
     * @param counter_to Counter final value
     * @param counter_increment
    */
    public MathStudent (int id, String dob, String name, String course, int counter_from, int counter_to, int counter_increment){
        super(id, dob, name, course, counter_from, counter_to, counter_increment);
    }  
        
    /** 
     * Display student class type
     */
    public void displayStudent(){
        //super.displayStudent();
        String type = "MathStudent.class";
        System.out.println(type);
    }    
    
    /**
     * @return The string representation of the Mathematics student
     */    
    public String toString(){
        return super.toString();         
    }    
   
}
