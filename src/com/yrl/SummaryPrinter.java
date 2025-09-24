package com.yrl;

import java.util.List;

/*
 * A collection of methods that use to print out summary reports.
 * */
public class SummaryPrinter {
	
	/*
	 * Method that add all sale items to sales and sales to stores
	 * */
	public static void addSaleItemSaleStore(List<Store> stores, List<Sale> sales, List<SaleItem> saleItems) {
		for(Sale sale: sales) {
			sale.addAllSaleItems(saleItems);
		}
		for(Store store: stores) {
			store.addAllSales(sales);
		}
	}
		
	/*
	 * Method that prints out a summary report for sales
	 * */
	public static void printFirstReport(List<Sale> sales) {
		sales.sort(Comparators.CMP_SALES);
		int totalNumItems = 0;
		double totalTaxes = 0;
		double total = 0;
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("| Summary Report - By Total                                                              |");
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("Invoice #  Store      Customer                       Num Items          Tax       Total");
		for(Sale sale : sales) {
			sale.print();
			totalNumItems += sale.getSaleItems().size(); 
			totalTaxes += sale.sumAllTaxes();
			total += sale.sumAllTotal();
		}
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.printf("%54d		$%10.2f $%10.2f\n",totalNumItems, totalTaxes, total);
		System.out.print("\n");
	}
	
	/*
	 * Method that prints out store sales summary report
	 * */
	public static void printSecondReport(List<Store> stores) {
		stores.sort(Comparators.CMP_STORE);
		int totalSales =0; 
		double total = 0;
		System.out.println("+----------------------------------------------------------------+");
		System.out.println("| Store Sales Summary Report                                     |");
		System.out.println("+----------------------------------------------------------------+");
		System.out.println("Store      Manager                        # Sales    Grand Total  ");
		for(Store store : stores) {
			double grandTotal = 0;
			store.print();
			List<Sale> sales = store.getSales();
			int numSales = sales.size();
			for(Sale sale: sales) {
				grandTotal += sale.sumAllTotal();
		}
			totalSales += numSales;
			total += grandTotal;
		}
		System.out.println("+----------------------------------------------------------------+");
		System.out.printf("%43d	     $%10.2f\n", totalSales, total);
		System.out.print("\n");
	}
	
	
	/*
	 * Method that prints out a detailed report of sales from each store
	 * */
	public static void printFinalReport(List<Sale> sales) {
		for(Sale sale : sales) { 
			sale.printDetailedReport();
		}
	}
	
	/*
	 * Method that prints out sorted by customer sales report 
	 * */
	public static void printSortedByCustomerSales(MyLinkedList<Sale> sortedSalesByCustomer) {
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Customer                                                       |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total      ");
		for(Sale sale: sortedSalesByCustomer) {
			System.out.print(sale);
		}
	}
	
	/*
	 * Method that prints out sorted by total sales report 
	 * */
	public static void printSortedByTotalSales(MyLinkedList<Sale> sortedSalesByTotal) {
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Total                                                      	  |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total      ");
		for(Sale sale: sortedSalesByTotal) {
			System.out.print(sale);
		}	
	}
	
	/*
	 * Method that prints out sorted by total sales report 
	 * */
	public static void printSortedByStoreSales(MyLinkedList<Sale> sortedSalesByStore) {
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Store                                                       	  |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("Sale       Store      Customer             Salesperson          Total      ");
		for(Sale sale: sortedSalesByStore) {
			System.out.print(sale);
		}	
	}
	
}