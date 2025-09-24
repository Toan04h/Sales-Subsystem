package com.yrl;

import java.util.Comparator;

public class CustomerComparator implements Comparator<Sale>{
	
	@Override
	public int compare(Sale s1, Sale s2) {
		if(s1.getCustomer().getLastName().compareTo(s2.getCustomer().getLastName()) == 0) {
			if(s1.getCustomer().getFirstName().compareTo(s2.getCustomer().getFirstName()) == 0) {
				if(s1.getSaleCode().compareTo(s2.getSaleCode())==0) {
					return 0;
				} else {
					return s1.getSaleCode().compareTo(s2.getSaleCode());
				}
			} else {
				return s1.getCustomer().getFirstName().compareTo(s2.getCustomer().getFirstName());
			}
		} else {
			return s1.getCustomer().getLastName().compareTo(s2.getCustomer().getLastName());
		}
	}

}
