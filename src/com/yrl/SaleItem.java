package com.yrl;

/*
 * Represent a saleItem
 * */
public class SaleItem  {
	
	private String saleCode;
	private Item item;
	
	public SaleItem(String saleCode, Item item) {
		this.saleCode = saleCode;
		this.item = item;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public Item getItem() {
		return item;
	}


}