package com.yrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Represent a sale
 * */
public class Sale {
	
	private String saleCode; 
	private String storeCode;
	private Person customer;
	private Person salesPerson; 
	private LocalDate saleDate;
	private List<SaleItem> saleItems;
	
	public Sale(String saleCode, String storeCode, Person customer, Person salesPerson, String saleDate) {
		this.saleCode = saleCode;
		this.storeCode = storeCode;
		this.customer = customer; 
		this.salesPerson = salesPerson;
		this.saleDate = LocalDate.parse(saleDate);
		this.saleItems = new ArrayList<>();
	}
	
	public String getSaleCode() {
		return saleCode;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public Person getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public LocalDate getSaleDate() {
		return saleDate;
	}
	
	public List<SaleItem> getSaleItems() {
		return saleItems;
	}
	
	public void addSaleItem(SaleItem si) {
		if(si == null) {
			return;
		}
		this.saleItems.add(si);
	}
	
	public String toString() {
		return String.format("%s %12s %10s, %10s %10s, %10s $%10.2f\n", this.saleCode, this.storeCode, this.customer.getLastName(), this.customer.getFirstName(), this.salesPerson.getLastName(), this.salesPerson.getFirstName(), this.sumAllTotal());
	}
	
	public void print() {
		System.out.printf("%s %12s %12s, %10s %11d		$%10.2f $%10.2f\n", this.saleCode, this.storeCode, this.customer.getLastName(), this.customer.getFirstName(), this.saleItems.size(), this.sumAllTaxes(), this.sumAllTotal());
		return;	
	}
	
	/*
	 * Method that prints out a detailed report for sale
	 * */
	public void printDetailedReport() {
		double totalPrice = this.sumAllPrice();
		double totalTaxes = this.sumAllTaxes();
		double total = this.sumAllTotal();
		System.out.printf("Sale    #%s\n", this.saleCode);
		System.out.printf("Store   #%s\n", this.storeCode);
		System.out.printf("Date     %s\n", this.saleDate);
		System.out.println("Customer:");
		System.out.printf("%s, %s (%s)\n", this.customer.getLastName(), this.customer.getFirstName(), this.customer.getUuid());
		System.out.printf("         %s\n", this.customer.getEmails());
		System.out.printf("         %s\n", this.customer.getAddress().getStreet());
		System.out.printf("         %s %s %s\n\n", this.customer.getAddress().getCity(), this.customer.getAddress().getState(), this.customer.getAddress().getZip());
		System.out.println("Sales Person:");
		System.out.printf("%s, %s (%s)\n", this.salesPerson.getLastName(), this.salesPerson.getFirstName(), this.salesPerson.getUuid());
		System.out.printf("         %s\n", this.salesPerson.getEmails());
		System.out.printf("         %s\n", this.salesPerson.getAddress().getStreet());
		System.out.printf("         %s %s %s\n\n", this.salesPerson.getAddress().getCity(), this.salesPerson.getAddress().getState(), this.salesPerson.getAddress().getZip());
		System.out.printf("Items (%d)                                                            Tax       Total\n", this.saleItems.size());
		System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");
		if(saleItems.size() > 0) {
			for(SaleItem saleItem: this.saleItems) {
				saleItem.getItem().print();
			}
		}
		System.out.println("                                                             -=-=-=-=-=- -=-=-=-=-=-");
        System.out.printf("                                                   Subtotals $%10.2f $%10.2f\n", totalTaxes, totalPrice);
        System.out.printf("                                                 Grand Total         	 $%10.2f\n", total);
	}
	
	/*
	 * Method that adds all the sale items to sale if they 
	 * have the same saleCode
	 * */
	public void addAllSaleItems(List<SaleItem> saleItems) {
			for(SaleItem saleItem : saleItems) {
				if(this.getSaleCode().equals(saleItem.getSaleCode())) {
					this.addSaleItem(saleItem);
			}
		}
	}
	
	/*
	 * Method that adds all the price of the items in sale
	 * */
	public double sumAllPrice() {
		double total = 0.0; 
		List<SaleItem> salesItems = this.getSaleItems();
		for(int i=0; i<salesItems.size(); i++) {
			total += salesItems.get(i).getItem().getPrice();
		}
		return total;
	}
	
	/*
	 * Method that adds all the taxes of the items in sale
	 * */
	public double sumAllTaxes() {
		double total = 0.0; 
		List<SaleItem> salesItems = this.getSaleItems();
		for(int i=0; i<salesItems.size(); i++) {
			total += salesItems.get(i).getItem().getTaxes();
		}
		return total;
	}
	
	/*
	 * Method that adds all the price and taxes of the items in sale
	 * */
	public double sumAllTotal() {
		double total = 0.0; 
		List<SaleItem> salesItems = this.getSaleItems();
		for(int i=0; i<salesItems.size(); i++) {
			total += salesItems.get(i).getItem().getTotal();
		}
		return total;
	}
}