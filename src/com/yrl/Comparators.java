package com.yrl;

import java.util.Comparator;

/*
 * A collection of methods that use to compare. 
 * */
public class Comparators {
	
	/*
	 * Method that compare person by their UUID
	 * */ 
	public static final Comparator<Person> CMP_UUID = new Comparator<Person>() {
		public int compare(Person p1, Person p2) {
			if(p1.getUuid().compareTo(p2.getUuid())==0) {
				return p1.getUuid().compareTo(p2.getUuid());
			} else {
				return p1.getUuid().compareTo(p2.getUuid());
			}
		}
	};
	
	/*
	 * Method that compare items by their itemCode
	 * */ 
	public static final Comparator<Item> CMP_CODE = new Comparator<Item>() {
		public int compare(Item i1, Item i2) {
			if(i1.getCode().compareTo(i2.getCode())==0) {
				return i1.getCode().compareTo(i2.getCode());
			} else {
				return i1.getCode().compareTo(i2.getCode());
			}
		}
	};
	
	/*
	 * Method that compare stores by their manager and then their sales
	 * */ 
	public static final Comparator<Store> CMP_STORE = new Comparator<Store>() {
		public int compare(Store s1, Store s2) {
			if(s1.getManager().getLastName().compareTo(s2.getManager().getLastName())==0) {
				if(s1.getManager().getFirstName().compareTo(s2.getManager().getFirstName())==0) {
					double totalS1 = 0;
					double totalS2 = 0;
					for(Sale sale : s1.getSales()) {
						totalS1 += sale.sumAllTotal();
					}
					for(Sale sale : s2.getSales()) {
						totalS2 += sale.sumAllTotal();
					}
					if(totalS1 > totalS2) {
						return -1; 
					} else if (totalS1 < totalS2) {
						return 1;
					} else {
						return 0;
					}
				} else {
					return s1.getManager().getFirstName().compareTo(s2.getManager().getFirstName());
				}
			} else {
				return s1.getManager().getLastName().compareTo(s2.getManager().getLastName());
			}
		}
	};
	
	/*
	 * Method that compare sales by their total
	 * */ 
	public static final Comparator<Sale> CMP_SALES = new Comparator<Sale>() {
		public int compare(Sale s1, Sale s2) {
			if(s1.sumAllTotal() > s2.sumAllTotal()) {
				return -1; 
			} else if (s1.sumAllTotal() < s2.sumAllTotal()) {
				return 1;
			} else {
				return 0;
			}
			
		}
	};

}