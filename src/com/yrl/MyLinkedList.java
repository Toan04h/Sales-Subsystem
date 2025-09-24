package com.yrl;

import java.util.Iterator;
import java.util.List;

/*
 * A linked list implementation for T instances
 * */
public class MyLinkedList<T> implements Iterable<T> {
	
	protected Node<T> head;
	protected int size;
	
	public MyLinkedList() {
		this.head = null;
		this.size = 0;
	}

	/**
	 * This function returns the size of the list, the number of elements currently
	 * stored in it.
	 * 
	 * @return
	 */
	public int getSize() {
		return this.size; 
	}
	
	/**
	 * This function clears out the contents of the list, making it an empty list.
	 */
	public void clear() {
		this.size = 0;
	}
	
	/**
	 * This method adds the given T instance to the
	 * linked list 
	 * 
	 * @param element
	 */
	public void add(T element) {
		Node<T> newNode = new Node<T>(element);
		
		if(this.head == null) {
			this.head = newNode;
		} else {
			Node<T> currNode = this.head;
			while((currNode.getNext() != null)) {
				currNode = currNode.getNext();
			}
			currNode.setNext(newNode);
		}
		this.size++;
		return;
	}
	
	/**
	 * This method removes the node from the given <code>position</code>,
	 * indices start at 0. Implicitly, the remaining elements' indices are reduced.
	 * 
	 * @param position
	 */
	public void remove(int position) {
		this.boundsCheck(position);
		if (position == 0) {
			this.head = this.head.getNext();
		} else {
			Node<T> previous = this.getNode(position-1);
			Node<T> curr = previous.getNext();
			previous.setNext(curr.getNext());
		}
		this.size--;
	}

	/**
	 * This is a private helper method that returns a node corresponding to 
	 * the given position. Implementing this method is optional but
	 * may help you with other methods.
	 * 
	 * @param position
	 * @return
	 */
	private Node<T> getNode(int position) {
		this.boundsCheck(position);
		Node<T> currNode = this.head; 
		for(int i=0; i<position; i++) {
			currNode = currNode.getNext();
		}
		return currNode;
	}
	
	/**
	 * Returns the T element stored at the given <code>position</code>.
	 * 
	 * @param position
	 * @return
	 */
	public T getElement(int position) {
		this.boundsCheck(position);
		Node<T> currNode = getNode(position);
		T element = currNode.getElement();
		return element;
	}
	
	/**
	 * This method adds multiple elements from a list to the linked list
	 * */
	public void batchAdd(List<T> list) {
		for(T element : list) {
			this.add(element);
		}
	}
	
	/*
	 * This method checks if the input position is valid or not
	 * */
	private void boundsCheck (int position) {
		if (position < 0 || position >= this.size) {
			throw new IllegalArgumentException("Invalid position: " + position);
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			Node<T> currNode = head; 
			
			public boolean hasNext() {
				return currNode != null;
			}
			
			public T next() {
				if(hasNext()) {
					T element = currNode.getElement();
					currNode = currNode.getNext();
					return element;
				}
				return null;
			}
		};
	}

	
}