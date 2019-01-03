import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) throws Exception{
        if(args.length < 1){
            System.out.println("You need to give me the name of the class to run");
            System.out.println("Usage: java -jar GDA.jar <Class Name> <Args for the class>");
            return;
        }
        
        Class tmp = null;
    
        try{
            tmp = Class.forName(args[0]);
        }catch(Exception e){
            System.out.println("Failed to find the class name: " + e.getMessage());
            return;
        }
        
        Method m = null;
        
        try{
            m = tmp.getMethod("main", String[].class);
        }catch(Exception e){
            System.out.println("Failed to find the main method: " + e.getMessage());
            return;
        }
        
        // This is outside of a try catch so that any errors are propigated
        m.invoke(null, new Object[]{args});
    }
}
