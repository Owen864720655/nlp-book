package com.lietu.EventExtract;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
     * 单词类的线性列表类
     * @author Administer 
     * @2010-3-18
 */
public class DocTokenLinkedList implements Iterable<DocTokenInf>{
	public static class Node
    {
		public DocTokenInf item; public Node next;
        Node(DocTokenInf item)
          { this.item = item; next = null; }
    }
    
    private Node head, tail;

    public DocTokenLinkedList()
    {
    	head = null;
    	tail = null; 
    }
    
    public void put(DocTokenInf item)
    {
    	Node t = tail; tail = new Node(item);
        if (head == null) head = tail; else t.next = tail;
    }
    
	public Node getHead()
	{
		return head;
	}
	
    public int size()
    {
    	int count=0;
    	
    	Node t = head;
    	while(t!= null)
    	{
    		count++;
	    	t = t.next;
    	}
    	
    	return count;
    }
    
    public Iterator<DocTokenInf> iterator() {
        return new LinkIterator(head);
    }

    private class LinkIterator implements Iterator<DocTokenInf> {
        Node itr;
    	public LinkIterator(Node begin)
    	{
    		itr = begin;
    	}
    	
        public boolean hasNext() {
            return itr!=null;
        }
        
        public DocTokenInf next() {
        	if(itr == null)
        	{
        		throw new NoSuchElementException();
        	}
        	Node cur = itr;
        	itr = itr.next;
            return cur.item;
        }
        
        public void remove() {
            //itr.remove();
        }
    }
    
    public String toString()
	{
		StringBuilder buf = new StringBuilder();
		Node pCur=head;
		
		while(pCur!=null)
		{
			buf.append(pCur.item.toString());
			buf.append('\t');
			pCur=pCur.next;
		}

		return buf.toString();
	}
    
}
