package com.yrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class SalesData {
	
private static final Logger LOGGER = LogManager.getLogger(SalesData.class);
	
	/*
	 * Configure the logger
	 * */
	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}
	

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		
		Connection conn = null;
				
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String deleteSaleItem = "delete from SaleItem";
			String deleteSale = "delete from Sale";
			String deleteItem = "delete from Item";
			String deleteStore = "delete from Store";
			String deleteEmail = "delete from Email";
			String deletePerson = "delete from Person";
			String deleteAddress = "delete from Address";
			
			PreparedStatement deleteSaleItemPs = conn.prepareStatement(deleteSaleItem);
			PreparedStatement deleteSalePs = conn.prepareStatement(deleteSale);
			PreparedStatement deleteItemPs = conn.prepareStatement(deleteItem);
			PreparedStatement deleteStorePs = conn.prepareStatement(deleteStore);
			PreparedStatement deleteEmailPs = conn.prepareStatement(deleteEmail);
			PreparedStatement deletePersonPs = conn.prepareStatement(deletePerson);
			PreparedStatement deleteAddressPs = conn.prepareStatement(deleteAddress);

			deleteSaleItemPs.executeUpdate();
			deleteSalePs.executeUpdate();
			deleteItemPs.executeUpdate();
			deleteStorePs.executeUpdate();
			deleteEmailPs.executeUpdate();
			deletePersonPs.executeUpdate();
			deleteAddressPs.executeUpdate();

			deleteSaleItemPs.close();
			deleteSalePs.close();
			deleteItemPs.close();
			deleteStorePs.close();
			deleteEmailPs.close();
			deletePersonPs.close();
			deleteAddressPs.close();			
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully clear database");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addPerson(String personUuid, String firstName, String lastName, String street, String city,
			String state, String zip) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			int addressId = DatabaseHelper.addAddress(street, city, state, zip);
			
			String personQuery = "insert into Person(personUuid, firstName, lastName, addressId) values "
					+ "(?, ?, ?, ?)";
			PreparedStatement personPs = conn.prepareStatement(personQuery);
			personPs.setString(1, personUuid);
			personPs.setString(2, firstName);
			personPs.setString(3, lastName);
			personPs.setInt(4, addressId);
			personPs.executeUpdate();
			
			personPs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding person");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(String personUuid, String email) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
					
			int personId = DatabaseHelper.getPersonId(personUuid);
			
			String emailQuery = "insert into Email(email, personId) values "
					+ "(?, ?)";
			PreparedStatement emailPs = conn.prepareStatement(emailQuery);
			emailPs.setString(1, email);
			emailPs.setInt(2, personId);
			emailPs.executeUpdate();
			
			emailPs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding email");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
						
			int addressId = DatabaseHelper.addAddress(street, city, state, zip);
			
			int managerId = DatabaseHelper.getPersonId(managerCode);
					
			String storeQuery = "insert into Store(storeCode, managerId, addressId) values "
					+ "(?, ?, ?)";
			PreparedStatement storePs = conn.prepareStatement(storeQuery);
			storePs.setString(1, storeCode);
			storePs.setInt(2, managerId);
			storePs.setInt(3, addressId);
			storePs.executeUpdate();
			
			storePs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding store");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an item record to the database of the given <code>type</code> with the
	 * given <code>code</code>, <code>name</code> and <code>basePrice</code>.
	 *
	 * Valid values for the <code>type</code> will be <code>"Product"</code>,
	 * <code>"Service"</code>, <code>"Data"</code>, or <code>"Voice"</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param type
	 * @param basePrice
	 */
	public static void addItem(String code, String name, String type, double basePrice) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			if(type.equals("Product")) {
				type = "P";
			} else if (type.equals("Service")) {
				type = "S";
			} else if (type.equals("Data")) {
				type = "D";
			} else if (type .equals("Voice")) { 
				type = "V";
			} else {
				throw new IllegalArgumentException("Invalid Type of Item");
			}
			String itemQuery = "insert into Item(itemCode, itemName, itemType, itemBasePrice) values "
					+ "(?, ?, ?, ?)";
			PreparedStatement itemPs = conn.prepareStatement(itemQuery);
			itemPs.setString(1, code);
			itemPs.setString(2, name);
			itemPs.setString(3, type);
			itemPs.setDouble(4, basePrice);
			itemPs.executeUpdate();
			
			itemPs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding item");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an Sale record to the database with the given data.
	 *
	 * @param saleCode
	 * @param storeCode
	 * @param customerPersonUuid
	 * @param salesPersonUuid
	 * @param saleDate
	 */
	public static void addSale(String saleCode, String storeCode, String customerPersonUuid, String salesPersonUuid,
			String saleDate) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		
			int customerId = DatabaseHelper.getPersonId(customerPersonUuid);
			
			int salesPersonId = DatabaseHelper.getPersonId(salesPersonUuid);
			
			String storeQuery = "select storeId from Store where storeCode"
					+ "= ?";
			PreparedStatement storePs = conn.prepareStatement(storeQuery);
			storePs.setString(1, storeCode);
			ResultSet storeRs = storePs.executeQuery();
			storeRs.next();
			int storeId = storeRs.getInt(1);
			
			storeRs.close();
			storePs.close();
			
			String saleQuery = "insert into Sale(saleCode, storeId, customerId, salesPersonId, saleDate) values "
					+ "(?, ?, ?, ?, ?)";
			PreparedStatement salePs = conn.prepareStatement(saleQuery);
			salePs.setString(1, saleCode);
			salePs.setInt(2, storeId);
			salePs.setInt(3, customerId);
			salePs.setInt(4, salesPersonId);
			salePs.setString(5, saleDate);
			salePs.executeUpdate();
			salePs.close();
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding sale");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>).
	 *
	 * @param saleCode
	 * @param itemCode
	 */
	public static void addProductToSale(String saleCode, String itemCode) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
				
			double productCost = DatabaseHelper.getItemCost(itemCode);
			
			int saleId = DatabaseHelper.getSaleId(saleCode);
			
			int itemId = DatabaseHelper.getItemId(itemCode);
			
			String addSaleItemQuery = "insert into SaleItem(saleId, itemId, price) values "
					+ "(?, ?, ?)";
			PreparedStatement addSaleItemPs = conn.prepareStatement(addSaleItemQuery, Statement.RETURN_GENERATED_KEYS);
			addSaleItemPs.setInt(1, saleId);
			addSaleItemPs.setInt(2, itemId);
			addSaleItemPs.setDouble(3, productCost);

			addSaleItemPs.executeUpdate();
			addSaleItemPs.close();
			
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding product to sale");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular leased (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the start/end date
	 * specified.
	 *
	 * @param saleCode
	 * @param startDate
	 * @param endDate
	 */
	public static void addLeaseToSale(String saleCode, String itemCode, String startDate, String endDate) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
					
			double baseCost = DatabaseHelper.getItemCost(itemCode);
			
			long leasingMonth = ChronoUnit.MONTHS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
			
			double monthlyPayment = (double)Math.round(((baseCost*1.5) / leasingMonth)*100)/100;;
		
			int saleId = DatabaseHelper.getSaleId(saleCode);
			
			int itemId = DatabaseHelper.getItemId(itemCode);
			
			String addSaleItemQuery = "insert into SaleItem(saleId, itemId, monthlyPayment, leasingStart, leasingEnd) values "
					+ "(?, ?, ?, ?, ?)";
			PreparedStatement addSaleItemPs = conn.prepareStatement(addSaleItemQuery, Statement.RETURN_GENERATED_KEYS);
			addSaleItemPs.setInt(1, saleId);
			addSaleItemPs.setInt(2, itemId);
			addSaleItemPs.setDouble(3, monthlyPayment);
			addSaleItemPs.setString(4, startDate);
			addSaleItemPs.setString(5, endDate);
			addSaleItemPs.executeUpdate();
			addSaleItemPs.close();
			
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding lease to sale");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of hours. The service is done by the employee with the specified
	 * <code>servicePersonUuid</code>
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param billedHours
	 * @param servicePersonUuid
	 */
	public static void addServiceToSale(String saleCode, String itemCode, double billedHours,
			String servicePersonUuid) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			int servicePersonId = DatabaseHelper.getPersonId(servicePersonUuid);
						
			double costPerHour = DatabaseHelper.getItemCost(itemCode);
			
			int saleId = DatabaseHelper.getSaleId(saleCode);
			
			int itemId = DatabaseHelper.getItemId(itemCode);
			
			String addSaleItemQuery = "insert into SaleItem(saleId, itemId, costPerHour, serviceHours, servicePersonId) values "
					+ "(?, ?, ?, ?, ?)";
			PreparedStatement addSaleItemPs = conn.prepareStatement(addSaleItemQuery, Statement.RETURN_GENERATED_KEYS);
			addSaleItemPs.setInt(1, saleId);
			addSaleItemPs.setInt(2, itemId);
			addSaleItemPs.setDouble(3, costPerHour);
			addSaleItemPs.setDouble(4, billedHours);
			addSaleItemPs.setInt(5, servicePersonId);
			addSaleItemPs.executeUpdate();
			addSaleItemPs.close();
					
			conn.close();			
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding service to sale");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular data plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of gigabytes.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param gbs
	 */
	public static void addDataPlanToSale(String saleCode, String itemCode, double gbs) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
						
			double costPerGB = DatabaseHelper.getItemCost(itemCode);
			
			int saleId = DatabaseHelper.getSaleId(saleCode);
			
			int itemId = DatabaseHelper.getItemId(itemCode);
			
			String addSaleItemQuery = "insert into SaleItem(saleId, itemId, costPerGB, dataGB) values "
					+ "(?, ?, ?, ?)";
			PreparedStatement addSaleItemPs = conn.prepareStatement(addSaleItemQuery, Statement.RETURN_GENERATED_KEYS);
			addSaleItemPs.setInt(1, saleId);
			addSaleItemPs.setInt(2, itemId);
			addSaleItemPs.setDouble(3, costPerGB);
			addSaleItemPs.setDouble(4, gbs);
			addSaleItemPs.executeUpdate();
			addSaleItemPs.close();

			conn.close();			
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding data plan to sale");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular voice plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * <code>phoneNumber</code> for the given number of <code>days</code>.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param phoneNumber
	 * @param days
	 */
	public static void addVoicePlanToSale(String saleCode, String itemCode, String phoneNumber, int days) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
						
			double periodCost = DatabaseHelper.getItemCost(itemCode);
			
			int saleId = DatabaseHelper.getSaleId(saleCode);
			
			int itemId = DatabaseHelper.getItemId(itemCode);
			
			String addSaleItemQuery = "insert into SaleItem(saleId, itemId, periodCost, voicePhoneNum, voiceDays) values "
					+ "(?, ?, ?, ?, ?)";
			PreparedStatement addSaleItemPs = conn.prepareStatement(addSaleItemQuery, Statement.RETURN_GENERATED_KEYS);
			addSaleItemPs.setInt(1, saleId);
			addSaleItemPs.setInt(2, itemId);
			addSaleItemPs.setDouble(3, periodCost);
			addSaleItemPs.setString(4, phoneNumber);
			addSaleItemPs.setInt(5, days);
			addSaleItemPs.executeUpdate();
			addSaleItemPs.close();

			conn.close();			
		} catch (SQLException e) {
			LOGGER.error("Unsuccessfully adding voice plan to sale");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}