package com.yrl;

import java.util.Comparator;

public class StoreComparator implements Comparator<Sale>{

	@Override
	public int compare(Sale s1, Sale s2) {
		if(s1.getStoreCode().compareTo(s2.getStoreCode()) == 0 ) {
			if(s1.getSalesPerson().getLastName().compareTo(s2.getSalesPerson().getLastName()) == 0) {
				if(s1.getSalesPerson().getFirstName().compareTo(s2.getSalesPerson().getFirstName()) == 0) {
					if(s1.getSaleCode().compareTo(s2.getSaleCode())==0) {
						return 0;
					} else {
						return s1.getSaleCode().compareTo(s2.getSaleCode());
					}
				} else {
					return s1.getSalesPerson().getFirstName().compareTo(s2.getSalesPerson().getFirstName());
				}
			} else {
				return s1.getSalesPerson().getLastName().compareTo(s2.getSalesPerson().getLastName());
			}
		} else {
			return s1.getStoreCode().compareTo(s2.getStoreCode());
		}
	}
}
