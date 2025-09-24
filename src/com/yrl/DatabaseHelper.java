package com.yrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * A collection of method that helps find id of an item from database using its code or uuid. 
 * */
public class DatabaseHelper {
	
	/*
	 * Method that adds address to database and return addressId 
	 * */
	public static int addAddress(String street, String city, String state, String zip) {
		
		int addressId = 0;

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String addressQuery = "insert into Address(addressLine, city, state, zipCode) values "
					+ "(?, ?, ?, ?)";
			PreparedStatement addressPs = conn.prepareStatement(addressQuery, Statement.RETURN_GENERATED_KEYS);
			addressPs.setString(1, street);
			addressPs.setString(2, city);
			addressPs.setString(3, state);
			addressPs.setString(4, zip);
			addressPs.executeUpdate();
			ResultSet addressIdRs = addressPs.getGeneratedKeys();
			addressIdRs.next();
			addressId = addressIdRs.getInt(1);
			
			addressIdRs.close();
			addressPs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return addressId;
	}
	
	/*
	 * Method that get person id from person uuid 
	 * */
	public static int getPersonId(String personUuid) {
		
		int personId = 0;
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
						
			String personQuery = "select personId from Person where personUuid"
					+ "= ?";
			PreparedStatement personPs = conn.prepareStatement(personQuery);
			personPs.setString(1, personUuid);
			ResultSet personRs = personPs.executeQuery();
			personRs.next();
			personId = personRs.getInt(1);
			
			personRs.close();
			personPs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return personId;
	}
	
	/*
	 * Method that get itemId from item code
	 * */
	public static int getItemId(String itemCode) {
		
		int itemId = 0;
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String itemQuery = "select itemId from Item where itemCode = ?";
			PreparedStatement itemPs = conn.prepareStatement(itemQuery);
			itemPs.setString(1, itemCode);
			ResultSet itemRs = itemPs.executeQuery();
			itemRs.next();
			itemId = itemRs.getInt(1);
			
			itemRs.close();
			itemPs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return itemId;
	}
	
	/*
	 * Method that get item cost from item code
	 * */
	public static double getItemCost(String itemCode) {
		
		double itemCost = 0;
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String itemQuery = "select itemBasePrice from Item where itemCode = ?";
			PreparedStatement itemPs = conn.prepareStatement(itemQuery);
			itemPs.setString(1, itemCode);
			ResultSet itemRs = itemPs.executeQuery();
			itemRs.next();
			itemCost = itemRs.getDouble(1);
			
			itemRs.close();
			itemPs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return itemCost;
	}
	
	
	/*
	 * Method that get saleId from sale code
	 * */
	
	public static int getSaleId(String saleCode) {
		
		int saleId = 0;
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
			
			String saleQuery = "select saleId from Sale where saleCode = ?";
			PreparedStatement salePs = conn.prepareStatement(saleQuery);
			salePs.setString(1, saleCode);
			ResultSet saleRs = salePs.executeQuery();
			saleRs.next();
			saleId = saleRs.getInt(1);
			
			saleRs.close();
			salePs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return saleId; 
	}
}