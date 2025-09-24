package com.yrl;

import java.util.Comparator;

/*
 * A sorted linked list implementation for T instances
 * 
 * */
public class SortedLinkedList<T> extends MyLinkedList<T> {

	private Comparator<T> comparator;

	public SortedLinkedList(Comparator<T> comparator) {
		super();
		this.comparator = comparator;
	}
	
	/**
	 * This method compares and then adds the given T instance to the
	 * linked list 
	 * 
	 * @param element
	 */
	@Override
	public void add(T element) {
		
		Node<T> newNode = new Node<T>(element);
		
		if(this.head == null) {
			this.head = newNode;
		} else if (comparator.compare(element, this.head.getElement())<0){
			Node<T> oldHead = this.head; 
			this.head = newNode; 
			this.head.setNext(oldHead);
			this.size++;
			return;
		} else {
			Node<T> currNode = this.head;
			while((currNode.getNext() != null)) {
				Node<T> nextNode = currNode.getNext();
				if ((comparator.compare(element, nextNode.getElement())<=0)) {
					currNode.setNext(newNode);
					currNode.getNext().setNext(nextNode);
					this.size++;
					return;
				} else if((comparator.compare(element, currNode.getElement())>0)) {
					currNode = currNode.getNext();
				} 
			}
			currNode.setNext(newNode);
		}
		this.size++;
		return;
	}
}
