package com.yrl;

import java.util.List;

public class SalesReport {
	
	public static void main(String args[]) {
		
		List<Person> persons = DatabaseDataLoader.loadPerson();
		List<Store> stores = DatabaseDataLoader.loadStore(persons);
		List<Sale> sales = DatabaseDataLoader.loadSale(persons);
		List<SaleItem> saleItems = DatabaseDataLoader.loadSaleItem(persons);
				
		SummaryPrinter.addSaleItemSaleStore(stores, sales, saleItems);
		
		MyLinkedList<Sale> sortedSalesByCustomer = new SortedLinkedList<Sale>(new CustomerComparator());
		MyLinkedList<Sale> sortedSalesByTotal = new SortedLinkedList<Sale>(new TotalComparator());
		MyLinkedList<Sale> sortedSalesByStore = new SortedLinkedList<Sale>(new StoreComparator());

		sortedSalesByCustomer.batchAdd(sales);
		sortedSalesByTotal.batchAdd(sales);
		sortedSalesByStore.batchAdd(sales);
		
		SummaryPrinter.printSortedByCustomerSales(sortedSalesByCustomer);
		SummaryPrinter.printSortedByTotalSales(sortedSalesByTotal);
		SummaryPrinter.printSortedByStoreSales(sortedSalesByStore);
	}
}