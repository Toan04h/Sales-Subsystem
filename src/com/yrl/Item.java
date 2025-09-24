package com.yrl;

/*
 * Represent an item
 * */
public class Item {
	 
	private String code;
	private String name;
	
	public Item(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public double getPrice() {
		return 0;
	}
	
	public double getTaxes() {
		return 0;
	}
	
	public double getTotal() {
		return 0;
	}

	public void print() {
		return;
	}

}