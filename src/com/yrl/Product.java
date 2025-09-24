package com.yrl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
 * Represent a product
 * */
public class Product extends Item {
	
	private double baseCost;
	private double monthlyPayment;
	private LocalDate leasingStart;
	private LocalDate leasingEnd;

	public Product(String code, String name, double monthlyPayment, LocalDate leasingStart, LocalDate leasingEnd) {
		super(code, name);
		this.setMonthlyPayment(monthlyPayment);
		this.leasingStart = leasingStart;
		this.leasingEnd = leasingEnd;
	}
	
	public Product(String code, String name, double baseCost) {
		super(code, name);
		this.baseCost = baseCost;
	}
	
	public Product(String code, String name) {
		super(code, name);
	}

	public double getBaseCost() {
		return baseCost;
	}

	public LocalDate getLeasingStart() {
		return leasingStart;
	}

	public void setLeasingStart(LocalDate leasingStart) {
		this.leasingStart = leasingStart;
	}

	public LocalDate getLeasingEnd() {
		return leasingEnd;
	}

	public void setLeasingEnd(LocalDate leasingEnd) {
		this.leasingEnd = leasingEnd;
	}
	
	public long getLeasingMonth() {
		return ChronoUnit.MONTHS.between(leasingStart, leasingEnd);
	}
	
	public double getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	@Override
	public void print() { 
		if(leasingStart == null || leasingEnd == null) {
			System.out.printf("%s (%s)\n", this.getName(), this.getCode());
			System.out.printf("                                                             $%10.2f $%10.2f\n", this.getTaxes(), this.getPrice());
		} else {
			System.out.printf("%s (%s) - Lease for %d months\n", this.getName(), this.getCode(), this.getLeasingMonth());
			System.out.printf("                                                             $%10.2f $%10.2f\n", this.getTaxes(), this.getPrice());
		}
	}
	
	@Override
	public double getPrice() {
		if (leasingStart == null || leasingEnd == null) { 
			return this.baseCost;
		} else { 
			return this.monthlyPayment;
		}
	}
	
	@Override
	public double getTaxes() {
		if (leasingStart != null && leasingEnd != null) { 
			return 0;
		} else { 
			return (double)Math.round((this.getPrice() * 0.065)*100)/100;
		}
	}
	
	@Override
	public double getTotal() {
		return this.getPrice() + this.getTaxes();
 	}
	
}