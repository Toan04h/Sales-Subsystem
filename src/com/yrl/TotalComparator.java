package com.yrl;

import java.util.Comparator;

public class TotalComparator implements Comparator<Sale> {
	
	@Override
	public int compare(Sale s1, Sale s2) {
		if(s1.sumAllTotal() < s2.sumAllTotal()) {
			return 1; 
		} else if (s1.sumAllTotal() > s2.sumAllTotal()) {
			return -1;
		} else {
			return 0;
		}	
	}
}
