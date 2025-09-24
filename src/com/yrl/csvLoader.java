package com.yrl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
 * A collection of methods that load data from a csv file 
 * and put them in a list.
 * */
public class csvLoader {
	
	/*
	 * Method that load persons from persons file
	 * */
	public static List<Person> loadPersons(String personFile) {

		List<Person> result = new ArrayList<Person>();
		
		String line = null;
		
		try (Scanner s = new Scanner(new File(personFile))) {
					
			line = s.nextLine();

			while(s.hasNextLine()) {
				line = s.nextLine();
				if (!line.trim().isEmpty()) {
					String tokens[] = line.split(",");
					String uuid = tokens[0];
					String firstName = tokens[1];
					String lastName = tokens[2];
					Address address = new Address(tokens[3], tokens[4], tokens[5], tokens[6]);
					ArrayList<String> emails = new ArrayList<>();
					if (tokens.length > 6) {
						for(int j = 7; j<tokens.length; j++) {
							emails.add(tokens[j]);
						}
					} 
					
					Person p = new Person(uuid, firstName, lastName, address, emails);
					result.add(p);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}
	
	/*
	 * Method that load store from stores file
	 * */
	public static List<Store> loadStores(String storeFile, String personFile) {

		List<Store> result = new ArrayList<Store>();
		
		String line = null;

		try (Scanner s = new Scanner(new File(storeFile))) {
			
			line = s.nextLine();
			
			List<Person> persons = loadPersons(personFile);
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				int index = 0;
				if (!line.trim().isEmpty()) {
					String tokens[] = line.split(",");
					String storeCode = tokens[0];
					String managerUuid = tokens[1];
					Person manager = new Person(managerUuid, null, null, null, null);
					persons.sort(Comparators.CMP_UUID);
					index = Collections.binarySearch(persons, manager, Comparators.CMP_UUID);
					if(index >= 0) {
						manager = persons.get(index);
					} 
					Address address = new Address(tokens[2], tokens[3], tokens[4], tokens[5]);
					Store r = new Store(storeCode, manager, address);
					result.add(r);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}
	
	/*
	 * Method that load items from items file
	 * */
	public static List<Item> loadItems(String itemFile) {

		List<Item> result = new ArrayList<Item>();
		
		Item item = null;
		
		String line = null;
		
		try (Scanner s = new Scanner(new File(itemFile))) {
						
			line = s.nextLine();

			while(s.hasNextLine()) {
				line = s.nextLine();
				if (!line.trim().isEmpty()) {
					String tokens[] = line.split(",");
					String code = tokens[0];
					String name	= tokens[2];
					double cost = Double.parseDouble(tokens[3]);
					if(tokens[1].equals("P")) {
						item = new Product(code, name, cost);
					} else if(tokens[1].equals("S")) {
						item = new Service(code, name, cost);
					} else if(tokens[1].equals("D")) {
						item = new DataPlan(code, name, cost);
					} else {
						item = new VoicePlan(code, name, cost);
					}
					
					result.add(item);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}
	
	/*
	 * Method that load sales from sales file
	 * */
	public static List<Sale> loadSales(String salesFile, String personFile) {
		List<Sale> result = new ArrayList<Sale>();
		
		String line = null;

		try (Scanner s = new Scanner(new File(salesFile))) {
			
			line = s.nextLine();

			List<Person> persons = loadPersons(personFile);
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				int index = 0;
				if (!line.trim().isEmpty()) {
					String tokens[] = line.split(",");
					String salesCode = tokens[0];
					String storeCode = tokens[1];
					String customerUuid = tokens[2];
					String salesPersonUuid = tokens[3];
					Person customer = new Person(customerUuid, null, null, null, null);
					Person salesPerson = new Person(salesPersonUuid, null, null, null, null);
					persons.sort(Comparators.CMP_UUID);
					index = Collections.binarySearch(persons, customer, Comparators.CMP_UUID);
					if(index >= 0) {
						customer = persons.get(index);
					} 
					index = Collections.binarySearch(persons, salesPerson, Comparators.CMP_UUID);
					if(index >= 0) {
						salesPerson = persons.get(index);
					} 
					String salesDate = tokens[4];
					Sale r = new Sale(salesCode, storeCode, customer, salesPerson, salesDate);
					result.add(r);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}
	
	/*
	 * Method that load sale items from saleItems file
	 * */
	public static List<SaleItem> loadSaleItems(String saleItemsFile, String itemFile, String personFile) {
		List<SaleItem> result = new ArrayList<SaleItem>();
		
		String line = null;

		try (Scanner s = new Scanner(new File(saleItemsFile))) {
						
			line = s.nextLine();

			List<Person> persons = loadPersons(personFile);
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				int index = 0;
				if (!line.trim().isEmpty()) {
					List<Item> items = loadItems(itemFile);
					String tokens[] = line.split(",");
					String salesCode = tokens[0];
					String itemCode = tokens[1];
					Item item = new Item(itemCode, null);
					items.sort(Comparators.CMP_CODE);
					index = Collections.binarySearch(items, item, Comparators.CMP_CODE);
					if(index >= 0) {
						item = items.get(index);
					} 
					SaleItem r = new SaleItem(salesCode, item);
					if(tokens.length > 2) {
						if(r.getItem() instanceof Product) {
							((Product) r.getItem()).setLeasingStart(LocalDate.parse(tokens[2]));
							((Product) r.getItem()).setLeasingEnd(LocalDate.parse(tokens[3]));
						} else if(r.getItem() instanceof Service) {
							((Service) r.getItem()).setServiceHour(Double.parseDouble(tokens[2]));
							persons.sort(Comparators.CMP_UUID);
							Person key = new Person(tokens[3], null, null, null, null);
							Person servicePerson = persons.get(Collections.binarySearch(persons, key, Comparators.CMP_UUID));
							((Service) item).setServicePerson(servicePerson);
						} else if(r.getItem() instanceof DataPlan) {
							((DataPlan) r.getItem()).setDataGB(Double.parseDouble(tokens[2]));
						} else if(r.getItem() instanceof VoicePlan) {
							((VoicePlan) r.getItem()).setPhoneNumber(tokens[2]);
							((VoicePlan) r.getItem()).setPlanDay(Double.parseDouble(tokens[3]));
						}
					}
					result.add(r);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		return result;
	}
}