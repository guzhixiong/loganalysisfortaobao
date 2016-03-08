package com.wangcheng.utils;
/*************************************************************************
 *  Compilation:  javac CircularLinkedList.java
 *  Execution:    java CircularLinkedList N
 *
 *  A circular list implemented with a doubly linked list. The elements
 *  are stored (and iterated over) in the same order that they are inserted.
 * 
 *  % java CircularLinkedList
 *
 *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularLinkedList<Value> {
    private int N;            // number of elements on list
    private Node first;       // first element
    private Node last;        // last element

    // linked list node helper data type
    private class Node {
        Value value;
        Node next;
        Node prev;
    }

    public boolean isEmpty()    { return N == 0; }
    public int size()           { return N;      }

    // add the value to the list
    public void add(Value value) {
        Node x = new Node();
        x.value = value;
        if (isEmpty()) first = last = x;
        x.next = first;
        x.prev = last;
        first.prev = x;
        last.next = x;
        last = x;
        N++;
    }

    public Iterator<Value> iterator()  { return new CircularLinkedListIterator(); }

    private class CircularLinkedListIterator implements Iterator<Value> {
        Node current      = first;   // the node that is returned by next()
        Node lastAccessed = null;    // the last node to be accessed

        public Value next() {
            if (isEmpty()) throw new NoSuchElementException();
            lastAccessed = current;
            Value value = current.value;
            current = current.next; 
            return value;
        }

        @Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		public Value previous() {
            if (isEmpty()) throw new NoSuchElementException();
            current = current.prev; 
            lastAccessed = current;
            return current.value;
        }

        public void remove() {
            if (lastAccessed == null) throw new UnsupportedOperationException();
            else if (isEmpty())       throw new UnsupportedOperationException();
            else if (size() == 1) { first = last = current = null; }
            Node x = lastAccessed.prev;
            Node y = lastAccessed.next;
            x.next = y;
            y.prev = x;
            lastAccessed = null;
            N--;
        }
    }

                
    // a test client - Josephus problem
    public static void main(String[] args) {
        int M = Integer.parseInt(args[0]);
        int N = Integer.parseInt(args[1]);
        CircularLinkedList<Integer> list = new CircularLinkedList<Integer>();
        for (int i = 1; i <= N; i++)
            list.add(i); 
        
        // try out the iterator
        Iterator<Integer> iterator = list.iterator();
        for (int i = 0; i < N; i++)
            System.out.print(iterator.next() + " ");
        System.out.println();
 
    }   
}
 

