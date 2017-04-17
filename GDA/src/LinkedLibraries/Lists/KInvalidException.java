package LinkedLibraries.Lists;

/**
 *  A simple exception for when there is an illegal operation on one of the 
 *  linked list structures
 */
public class KInvalidException extends Exception {
    /**
     *  Simply sets the message of the exception
     */
    public KInvalidException(String message){
        super(message);
    }
}