package com.yrl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

/*
 * Convert flat files to XML and JSON
 * */
public class DataConverter {
	
	public static final String personFile = "data/Persons.csv";
	public static final String storeFile = "data/Stores.csv";
	public static final String itemFile = "data/Items.csv";
	
	
	public static void main(String args[]) {
		
		List<Person> persons = csvLoader.loadPersons(personFile);
		List<Store> stores = csvLoader.loadStores(storeFile, personFile);
		List<Item> items = csvLoader.loadItems(itemFile);
		
		//to JSON: 
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try {
			PrintWriter pJson = new PrintWriter("data/Persons.json");
			pJson.println("{");
			pJson.println("\"persons\": ");
			pJson.write(gson.toJson(persons));
			pJson.println("}");
			pJson.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		try {
			PrintWriter sJson = new PrintWriter("data/Stores.json");
			sJson.println("{");
			sJson.println("\"stores\": ");
			sJson.write(gson.toJson(stores));
			sJson.println("}");
			sJson.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		try {
			PrintWriter iJson = new PrintWriter("data/Items.json");
			iJson.println("{");
			iJson.println("\"stores\": ");
			iJson.write(gson.toJson(items));
			iJson.println("}");
			iJson.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		//to XML:
		XStream xstream = new XStream();
		xstream.alias("person", Person.class);
		xstream.alias("store", Store.class);
		xstream.alias("item", Item.class);
		
		try {
			PrintWriter pXML = new PrintWriter("data/Persons.xml");
			pXML.write(xstream.toXML(persons));
			pXML.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		try {
			PrintWriter sXML = new PrintWriter("data/Stores.xml");
			sXML.write(xstream.toXML(stores));
			sXML.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
		try {
			PrintWriter iXML = new PrintWriter("data/Items.xml");
			iXML.write(xstream.toXML(items));
			iXML.close();
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
		
	}
}