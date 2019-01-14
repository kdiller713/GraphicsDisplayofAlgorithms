import java.lang.reflect.Method;

/**
 * This will find a precompiled algorithm to run.
 * The only arg is the package directory and name of the algorithm file.
 * The algorithm should have a main for this to call.
 */
public class Application {
    public static void main(String[] args) throws Exception{
        if(args.length < 1){
            System.out.println("You need to give me the name of the class to run");
            System.out.println("Usage: java -jar GDA.jar <Class Name> <Args for the class>");
            return;
        }
        
        Class<?> tmp = null;
        
        // Checks to see if the java file exists
        try{
            tmp = Class.forName(args[0]);
        }catch(Exception e){
            System.out.println("Failed to find the class name: " + e.getMessage());
            return;
        }
        
        Method m = null;
        
        // Checks to see if the algorithm has a main method to call
        try{
            m = tmp.getMethod("main", String[].class);
        }catch(Exception e){
            System.out.println("Failed to find the main method: " + e.getMessage());
            return;
        }
        
        // This is outside of a try catch so that any errors are propigated
        m.invoke(null, new Object[]{restArgs(args, 1)});
    }
    
    /**
     * Gets the remaining arguments from the command line to be passed on to the algorithm.
     */
    private static String[] restArgs(String[] args, int start){
        if(args.length <= start) return new String[] {};
        
        String[] rest = new String[args.length - start];
        
        for(int i = start; i < args.length; i++){
            rest[i - start] = args[i];
        }
        
        return rest;
    }
}
