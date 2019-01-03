package LinkedLibraries.Lists;

import java.util.Iterator;

/**
 *  A simple iterator to parse through a linked list.
 */
public class KListIterator<E> implements Iterator<E> {
    private KNode<E> node;
    
    /**
     *  Creates a new iterator to parse through a KLinkedList
     */
    public KListIterator(KNode<E> start){
        node = start;
    }
    
    /**
     *  Determines if there is a node that is still to be parsed
     */
    public boolean hasNext(){
        return node != null;
    }
    
    /**
     *  Gets the data in the next node in the KLinkedList
     */
    public E next(){
        E data = node.getData();
        node = node.getNext();
        return data;
    }
    
    /**
     *  This is an empty method that has no effect on the iterator
     */
    public void remove(){
        // Does Nothing
    }
}