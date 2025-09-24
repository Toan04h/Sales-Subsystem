package com.yrl;

import java.util.ArrayList;

/*
 * Represent a person
 * */
public class Person {
	
	private String personUuid; 
	private String firstName;
	private String lastName;
	private Address address;
	private ArrayList<String> emails;
	
	public Person(String personUuid, String firstName, String lastName, Address address, ArrayList<String> emails) {
		super();
		this.personUuid = personUuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emails = emails;
	}

	public String getUuid() {
		return personUuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}
}