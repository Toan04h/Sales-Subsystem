package com.yrl;

public class Node<T> {
	private Node<T> next;
	private T element;

	public Node(T element) {
		this.element = element;
		this.next = null;
	}

	public T getElement() {
		return element;
	}

	public Node<T> getNext() {
		return next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public String toString() {
		return this.element.toString();
	}
}