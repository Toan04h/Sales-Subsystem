package com.yrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/*
 * A collection of methods that load data from database
 * */
public class DatabaseDataLoader {
	
	private static final Logger LOGGER = LogManager.getLogger(DatabaseDataLoader.class);
	
	/*
	 * Configure the logger
	 * */
	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}
	
	/*
	 * Method that loads person information from database
	 * */
	public static List<Person> loadPerson() {
		
		Connection conn = null;
		
		List<Person> persons = new ArrayList<Person>();
				
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String personQuery = "select p.personId, p.personUuid, p.firstName, p.lastName, a.addressLine, a.city, a.state, a.zipCode "
					+ "from Person p join Address a on p.addressId = a.addressId";
			
			PreparedStatement personPs = conn.prepareStatement(personQuery);
			ResultSet personRs = personPs.executeQuery();
			
			while (personRs.next()) {
				ArrayList<String> emails = new ArrayList<String>();
				
				int personId = personRs.getInt("p.personId");
				
				String emailQuery = "select email from Email where Email.personId = ?";
				PreparedStatement emailPs = conn.prepareStatement(emailQuery);
				emailPs.setInt(1, personId);
				ResultSet emailRs = emailPs.executeQuery();
				
				while(emailRs.next()) {
					String email = emailRs.getString(1);
					emails.add(email);
				}
				emailRs.close();
				emailPs.close();
				String personUuid = personRs.getString("p.personUuid");
				String firstName = personRs.getString("p.firstName");
				String lastName = personRs.getString("p.lastName");
				String street = personRs.getString("a.addressLine");
				String city = personRs.getString("a.city");
				String state = personRs.getString("a.state");
				String zipCode = personRs.getString("a.zipCode");
				Address address = new Address(street, city, state, zipCode);
				Person person = new Person(personUuid, firstName, lastName, address, emails);
				persons.add(person);
			}
			personRs.close();
			personPs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not load person");
			e.printStackTrace();
			throw new RuntimeException(e);
		}				
		return persons;
	}
	
	/*
	 * Method that loads store infomation from database
	 * */
	public static List<Store> loadStore(List<Person> persons) {
				
		Connection conn = null;
		
		List<Store> stores = new ArrayList<Store>();
		
		persons.sort(Comparators.CMP_UUID);
				
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String storeQuery = "select s.storeCode, p.personUuid, a.addressLine, a.city, a.state, a.zipCode "
					+ "from Store s join Person p on s.managerId = p.personId "
					+ "join Address a on s.addressId = a.addressId";
			
			PreparedStatement storePs = conn.prepareStatement(storeQuery);
			ResultSet storeRs = storePs.executeQuery();
			
			while (storeRs.next()) {
				String storeCode = storeRs.getString("s.storeCode");
				String personUuid = storeRs.getString("p.personUuid");
				Person manager = new Person(personUuid, null, null, null, null);
				int index = Collections.binarySearch(persons, manager, Comparators.CMP_UUID);
				if(index >= 0) {
					manager = persons.get(index);
				} 
				String street = storeRs.getString("a.addressLine");
				String city = storeRs.getString("a.city");
				String state = storeRs.getString("a.state");
				String zipCode = storeRs.getString("a.zipCode");
				Address address = new Address(street, city, state, zipCode);
				Store store = new Store(storeCode, manager, address);
				stores.add(store);
			}
			storeRs.close();
			storePs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not load store");
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
		return stores;
	}
	
	/*
	 * Method that loads item information from database
	 * */
	public static List<Item> loadItem() {
				
		Connection conn = null;
		
		List<Item> items = new ArrayList<Item>();
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String itemQuery = "select itemCode, itemName, itemType, itemBasePrice from Item";
			
			PreparedStatement itemPs = conn.prepareStatement(itemQuery);
			ResultSet itemRs = itemPs.executeQuery();
			
			while(itemRs.next()) {
				Item item = null;
				String itemCode = itemRs.getString("itemCode");
				String itemName = itemRs.getString("itemName");
				String itemType = itemRs.getString("itemType");
				double baseCost = itemRs.getDouble("itemBasePrice");
				if(itemType.equals("P")) {
					item = new Product(itemCode, itemName, baseCost);
				} else if(itemType.equals("S")) {
					item = new Service(itemCode, itemName, baseCost);
				} else if(itemType.equals("D")) {
					item = new DataPlan(itemCode, itemName, baseCost);
				} else {
					item = new VoicePlan(itemCode, itemName, baseCost);
				}
				items.add(item);
			}
			itemRs.close();
			itemPs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not load item");
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
		return items;
	}
	
	/*
	 * Method that loads sale information from database
	 * */
	public static List<Sale> loadSale(List<Person> persons) {
				
		Connection conn = null;
		
		List<Sale> sales = new ArrayList<Sale>();
		
		persons.sort(Comparators.CMP_UUID);
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String saleQuery = "select Sale.saleId, Sale.saleCode, Store.storeCode, Sale.saleDate "
					+ "from Sale join Store on Sale.storeId = Store.storeId";
			
			PreparedStatement salePs = conn.prepareStatement(saleQuery);
			ResultSet saleRs = salePs.executeQuery();
			
			while(saleRs.next()) {
				int index = 0;
				int saleId = saleRs.getInt("Sale.saleId");
				Person customer = null;
				Person salesPerson = null;
				String customerQuery = "select personUuid from Person join Sale on Sale.customerId = Person.personId where Sale.saleId = ?";
				PreparedStatement customerPs = conn.prepareStatement(customerQuery);
				customerPs.setInt(1, saleId);
				ResultSet customerRs = customerPs.executeQuery();
				if(customerRs.next()) {
					customer = new Person(customerRs.getString("personUuid"), null, null, null, null);
					persons.sort(Comparators.CMP_UUID);
					index = Collections.binarySearch(persons, customer, Comparators.CMP_UUID);
					if(index >= 0) {
						customer = persons.get(index);
					}
				}
				customerRs.close();
				customerPs.close();
				
				String salesPersonQuery = "select personUuid from Person join Sale on Sale.salesPersonId = Person.personId where Sale.saleId = ?";
				PreparedStatement salesPersonPs = conn.prepareStatement(salesPersonQuery);
				salesPersonPs.setInt(1, saleId);
				ResultSet salesPersonRs = salesPersonPs.executeQuery();
				if(salesPersonRs.next()) {
					salesPerson = new Person(salesPersonRs.getString("personUuid"), null, null, null, null);
					persons.sort(Comparators.CMP_UUID);
					index = Collections.binarySearch(persons, salesPerson, Comparators.CMP_UUID);
					if(index >= 0) {
						salesPerson = persons.get(index);
					}
				}
				salesPersonRs.close();
				salesPersonPs.close();
				
				String saleCode = saleRs.getString("Sale.saleCode");
				String storeCode = saleRs.getString("Store.storeCode");
				String saleDate = saleRs.getString("Sale.saleDate");
				Sale sale = new Sale(saleCode, storeCode, customer, salesPerson, saleDate);
				sales.add(sale);
			}
			saleRs.close();
			salePs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not load sale");
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
		return sales;
	}
	
	/*
	 * Method that loads sale item information from database
	 * */
	public static List<SaleItem> loadSaleItem(List<Person> persons) {
		
		Connection conn = null;
		
		List<SaleItem> saleItems = new ArrayList<SaleItem>();
		
		persons.sort(Comparators.CMP_UUID);
		
		try {
			
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String saleItemQuery = "select * from SaleItem "
					+ "join Sale on SaleItem.saleId = Sale.saleId "
					+ "join Item on SaleItem.itemId = Item.itemId ";
			
			PreparedStatement saleItemPs = conn.prepareStatement(saleItemQuery);
			ResultSet saleItemRs = saleItemPs.executeQuery();
			
			SaleItem saleItem = null;

			while(saleItemRs.next()) {
				String saleCode = saleItemRs.getString("saleCode");
				String itemCode = saleItemRs.getString("itemCode");
				String itemName = saleItemRs.getString("itemName");
				String itemType = saleItemRs.getString("itemType");
				Item item = null;
				if(itemType.equals("P")) {
					if(saleItemRs.getString("leasingStart") != null || saleItemRs.getString("leasingEnd") != null) {
						item = new Product(itemCode, itemName);
						saleItem = new SaleItem(saleCode, item);
						double monthlyPayment = saleItemRs.getDouble("monthlyPayment");
						LocalDate leasingStart = LocalDate.parse(saleItemRs.getString("leasingStart"));
						LocalDate leasingEnd = LocalDate.parse(saleItemRs.getString("leasingEnd"));
						((Product) saleItem.getItem()).setMonthlyPayment(monthlyPayment);
						((Product) saleItem.getItem()).setLeasingStart(leasingStart);
						((Product) saleItem.getItem()).setLeasingEnd(leasingEnd);
					} else {
						double price = saleItemRs.getDouble("price");
						item = new Product(itemCode, itemName, price);
						saleItem = new SaleItem(saleCode, item);
					}
				} else if(itemType.equals("S")) {
					double costPerHour = saleItemRs.getDouble("costPerHour");
					item = new Service(itemCode, itemName, costPerHour);
					saleItem = new SaleItem(saleCode, item);
					String servicePersonQuery = "select personUuid from Person where personId = ?"; 
					PreparedStatement servicePersonPs = conn.prepareStatement(servicePersonQuery);
					servicePersonPs.setInt(1, saleItemRs.getInt("servicePersonId"));
					ResultSet servicePersonRs = servicePersonPs.executeQuery();
					servicePersonRs.next();
					
					Person key = new Person(servicePersonRs.getString("personUuid"), null, null, null, null);
					Person servicePerson = persons.get(Collections.binarySearch(persons, key, Comparators.CMP_UUID));
					((Service) saleItem.getItem()).setServiceHour(saleItemRs.getDouble("serviceHours"));
					((Service) saleItem.getItem()).setServicePerson(servicePerson);
					servicePersonRs.close();
					servicePersonPs.close();
					
				} else if(itemType.equals("D")) {
					double costPerGB = saleItemRs.getDouble("costPerGB");
					item = new DataPlan(itemCode, itemName, costPerGB);
					saleItem = new SaleItem(saleCode, item);
					double dataGB = saleItemRs.getInt("dataGB");
					((DataPlan) saleItem.getItem()).setDataGB(dataGB);
				} else {
					double periodCost = saleItemRs.getDouble("periodCost");
					item = new VoicePlan(itemCode, itemName, periodCost);
					saleItem = new SaleItem(saleCode, item);
					double planDay = saleItemRs.getInt("voiceDays");
					String phoneNumber = saleItemRs.getString("voicePhoneNum");
					((VoicePlan) saleItem.getItem()).setPlanDay(planDay);
					((VoicePlan) saleItem.getItem()).setPhoneNumber(phoneNumber);
				}	
				saleItems.add(saleItem);	
			}
			saleItemRs.close();
			saleItemPs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Could not load sale item");
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
		return saleItems;
	}
}