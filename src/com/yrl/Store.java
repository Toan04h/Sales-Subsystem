package com.yrl;

import java.util.ArrayList;
import java.util.List;

/*
 * Represent a store
 * */
public class Store {
	
	private String storeCode;
	private Person manager;
	private Address address;
	private List<Sale> sales;
	
	public Store(String storeCode, Person manager, Address address) {
		this.storeCode = storeCode;
		this.manager = manager;
		this.address = address;
		this.sales = new ArrayList<>();
	}
	
	public String getStoreCode() {
		return storeCode;
	}
	
	public Person getManager() {
		return manager;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public List<Sale> getSales() {
		return sales;
	}
	
	public void addSale(Sale s) {
		if(s == null) {
			return;
		}
		this.sales.add(s);
	}
	
	/*
	 * Method that get total in $ from all sales. 
	 * */
	public double getTotalSales() {
		double total = 0;
		for(Sale sale: this.sales) {
			total += sale.sumAllTotal();
		}
		return total;
	}
	
	/*
	 * Method that add sale from sales list 
	 * that match the storeCode.
	 * */
	public void addAllSales(List<Sale> sales) {
		for(Sale sale : sales) {
			if(this.getStoreCode().equals(sale.getStoreCode())) {
				this.addSale(sale);
			}
		}
	}
	
	public void print() {
		System.out.printf("%s %10s, %10s %13d	     $%10.2f\n", this.storeCode, this.manager.getLastName(), this.manager.getFirstName(), this.sales.size(), this.getTotalSales());
		return;
	}
	
}