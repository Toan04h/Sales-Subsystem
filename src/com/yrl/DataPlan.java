package com.yrl;

/*
 * Represent a data plan
 * */
public class DataPlan extends Item{

	private double baseCost; 
	private double dataGB; 
	
	public DataPlan(String code, String name, double baseCost, double dataGB) {
		super(code, name);
		this.baseCost = baseCost; 
		this.dataGB = dataGB; 
	}

	public DataPlan(String code, String name, double baseCost) {
		super(code, name);
		this.baseCost = baseCost;
	}
	
	public double getBaseCost() {
		return baseCost;
	}

	public double getDataGB() {
		return dataGB;
	}

	public void setDataGB(double dataGB) {
		this.dataGB = dataGB;
	}
	
	@Override
	public void print() {
		System.out.printf("%s (%s) - Data\n", this.getName(), this.getCode());
		System.out.printf("%10.2f GB @ %.2f/GB\n", this.dataGB, this.baseCost);
		System.out.printf("                                                             $%10.2f $%10.2f\n", this.getTaxes(), this.getPrice());
	}
	
	@Override
	public double getPrice() {
		return (double)Math.round((this.baseCost * this.dataGB)*100)/100;
	}
	
	@Override
	public double getTaxes() {
		return (double)Math.round((this.getPrice() * 0.055)*100)/100;
	}
	
	@Override
	public double getTotal() {
		return this.getPrice() + this.getTaxes();
	}
	
}
