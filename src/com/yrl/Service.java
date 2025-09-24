package com.yrl;

/*
 * Represent a service
 * */
public class Service extends Item{
	
	private double baseCost; 
	private double serviceHour;
	private Person servicePerson; 
	
	public Service(String code, String name, double baseCost, double serviceHour, Person servicePerson) {
		super(code, name);
		this.baseCost = baseCost;
		this.setServiceHour(serviceHour);
		this.servicePerson = servicePerson;
	}

	public Service(String code, String name, double baseCost) {
		super(code, name);
		this.baseCost = baseCost;
	}

	public double getBaseCost() {
		return baseCost;
	}
	
	public double getServiceHour() {
		return serviceHour;
	}

	public void setServiceHour(double serviceHour) {
		this.serviceHour = serviceHour;
	}

	public Person getServicePerson() {
		return servicePerson;
	}

	public void setServicePerson(Person servicePerson) {
		this.servicePerson = servicePerson;
	}
	
	@Override
	public void print() {
		System.out.printf("%s (%s) - Served by %s, %s\n", this.getName(), this.getCode(), this.servicePerson.getLastName(), this.servicePerson.getFirstName());
		System.out.printf("%10.2f hours @ $%.2f/hour\n", this.serviceHour, this.baseCost);
		System.out.printf("                                                             $%10.2f $%10.2f\n", this.getTaxes(), this.getPrice());
	}

	@Override
	public double getPrice() {
		return (double)Math.round((this.baseCost * this.serviceHour)*100)/100;
	}
	
	@Override
	public double getTaxes() {
		return (double)Math.round((this.getPrice() * 0.035)*100)/100;
	}
	
	@Override
	public double getTotal() {
		return this.getPrice() + this.getTaxes();
 	}
	
}
