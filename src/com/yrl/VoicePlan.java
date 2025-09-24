package com.yrl;

/*
 * Represent an voice plan
 * */
public class VoicePlan extends Item{
	
	private double baseCost;
	private String phoneNumber; 
	private double planDay;
	
	public VoicePlan(String code, String name, double baseCost, String phoneNumber, int planDay) {
		super(code, name);
		this.baseCost = baseCost;
		this.phoneNumber = phoneNumber; 
		this.planDay = planDay; 
	}
	
	public VoicePlan(String code, String name, double baseCost) {
		super(code, name);
		this.baseCost = baseCost;
	}

	public double getBaseCost() {
		return baseCost;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public double getPlanDay() {
		return planDay;
	}

	public void setPlanDay(double planDay) {
		this.planDay = planDay;
	}
	
	@Override
	public void print() {
		System.out.printf("%s (%s) - Voice %s\n", this.getName(), this.getCode(), this.phoneNumber);
		System.out.printf("%10.0f days @ %.2f/ 30 days\n", this.planDay, this.baseCost);
		System.out.printf("                                                             $%10.2f $%10.2f\n", this.getTaxes(), this.getPrice());
	}
	
	public double getPrice() {
		return (double)Math.round((this.baseCost * (planDay/30))*100)/100;
	}
	
	public double getTaxes() {
		return (double)Math.round((this.getPrice() * 0.065)*100)/100;
	}
	
	public double getTotal() {
		return this.getPrice() + this.getTaxes();
	}
}
