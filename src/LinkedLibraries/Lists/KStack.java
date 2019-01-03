package LinkedLibraries.Lists;

/**
 *  A simple version of a stack with all the methods that would be needed
 */
public class KStack<E> {
    private KNode<E> head;
    private int size;
    
    /**
     *  Creates an empty stack for your manipulation
     */
    public KStack(){
        head = new KNode<E>(null);
        size = 0;
    }
    
    /**
     *  Gets the number of elements in the stack
     */
    public int length(){
        return size;
    }
    
    /**
     *  Appends the data to the end of the stack
     */
    public void push(E data) {
        head.setNext(new KNode<E>(data, head.getNext()));
        size++;
    }
    
    /**
     *  Removes the top element from the stack
     *
     *  @throws Throws an exception if there is nothing in the stack
     */
    public E pop() throws KInvalidException {
        if(size == 0)
            throw new KInvalidException("Nothing in the stack");
        
        E data = head.getNext().getData();
        head = head.getNext();
        size--;
        return data;
    }
    
    /**
     *  Gets the element from the top of the stack
     *
     *  @throws Throws an exception if the stack is empty
     */
    public E peek() throws KInvalidException {
        if(size == 0)
            throw new KInvalidException("Nothing in the stack");
        
        return head.getNext().getData();
    }
    
    /**
     *  Returns a simple iterator to iterate through the stack
     */
    public KListIterator<E> iterator(){
        return new KListIterator<E>(head.getNext());
    }
    
    /**
     *  Stores the elements of the stack in the array.
     *
     *  If the array is too small, then it fills up all the elements it could.
     *  If the array is too large, then it leaves the extra space as empty.
     */
    public void toArray(E[] vals) {
        int i = 0;
        KListIterator<E> list = iterator();
        
        while(i < vals.length && list.hasNext()){
            vals[i] = list.next();
            i++;
        }
    }
}