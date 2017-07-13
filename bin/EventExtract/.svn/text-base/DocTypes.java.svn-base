package com.lietu.EventExtract;

import java.util.Iterator;

public class DocTypes implements Iterable<DocTypes.DocTypeInf>{
	public static class Node
    {
		public DocTypeInf item;
		public Node next;
        Node(DocTypeInf item)
          { this.item = item; next = null; }
    }
	
	public static class DocTypeInf {
		public DocType pos;   //类型
	    public int weight=0;   //频率
	    public long code; //语义编码
	    
	    public DocTypeInf(DocType p,int f,long semanticCode)
	    {
	    	pos=p;
	    	weight = f;
	    	code = semanticCode;
	    }

	    public String toString()
		{
	    	return pos+":"+weight;
	    }
	}
    
    private Node head, tail;
	
    public DocTypes()
    {
    	head = null;
    	tail = null; 
    }
    
    public void put(DocTypeInf item)
    {
    	Node t = tail; 
    	tail = new Node(item);
        if (head == null) head = tail;
        else t.next = tail;
    }
    
    public void insert(DocTypeInf item)
    {
    	//one element
    	if (head == tail)
    	{
    		if(head.item.pos.compareTo(item.pos)>0)
    		{
    	    	Node t = head;
    	    	head = new Node(item);
    	        head.next = t;
    	    }
    		else
    		{
    	    	Node t = tail;
    	    	tail = new Node(item);
    	        t.next = tail;
    	     }
    		return;
		}
    	Node t = head;
    	
    	while(t.next!= null)
    	{
    		if (t.next.item.pos.compareTo(item.pos)>0)
    			break;
	    	t = t.next;
    	}
    	Node p = t.next;
    	t.next = new Node(item);
    	t.next.next = p;
    }
    
	public Node getHead()
	{
		return head;
	}
	
    public DocTypeInf findType(DocType toFind)
    {
    	if (head == null)
    		return null;
    	Node t = head;
    	while(t!= null && t.item!= null)
    	{
    		if (t.item.pos.equals(toFind))
    		{
    			return t.item;
    		}
	    	t = t.next;
    	}
    	
    	return null;
    }

    /**
     * @return
     */
    public int size()
    {
    	int count=0;
    	
    	//if (head == null)
    	//	return count;
    	Node t = head;
    	while(t!= null)
    	{
    		count++;
	    	t = t.next;
    	}
    	
    	return count;
    }

    public int totalCost()
    {
    	int cost=0;
    	
    	Node t = head;
    	while(t!= null)
    	{
    		cost+=t.item.weight;
	    	t = t.next;
    	}
    	
    	return cost;
    }
    
    public Iterator<DocTypes.DocTypeInf> iterator() {
    	//System.out.println("iterator");
        return new LinkIterator(head);
    }

    /** Adapter to provide descending iterators via ListItr.previous */
    private class LinkIterator implements Iterator<DocTypes.DocTypeInf> {
        Node itr;
    	public LinkIterator(Node begin)
    	{
    		itr = begin;
    	}
    	
        public boolean hasNext() {
            return itr!=null;
        }
        
        public DocTypeInf next() {
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
			buf.append(' ');
			pCur=pCur.next;
		}

		return buf.toString();
	}
    
}
