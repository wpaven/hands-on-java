package com.marklogic.client.example.handson.unit03;

import java.io.IOException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.example.handson.unit01.ReadDocumentJSON;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.RawQueryByExampleDefinition;

public class Unit03_Exercises {

	//Task 1:  Write a query that finds pictures taken with "Apple" phones in "Hungary".
	// Hint: you'll look in the "make" and "country" JSON properties
	
    //  qb.byExample({ country: 'Hungary', make: 'Apple' } 
	
	//Task 2: Delete the facets and tuples query options used in StructuredSearch.java
	
	public static void main(String[] args)
	throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props)
	throws IOException {
		System.out.println("example: "+Unit03_Exercises.class.getName()+"\n");

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);
				
		// run tasks
		runTask1(client);
		runTask2(client);
	}
	
	public static void runTask1(DatabaseClient client) 
	throws IOException {
		
	    // create a manager for searching
		QueryManager queryMgr = client.newQueryManager();

		// create a raw query example
		String rawJSONQuery =
				"{"+
                   "\"$query\": { \"make\": \"Apple\", \"country\": \"Hungary\" }"+
                  "}";
		
		System.out.println(rawJSONQuery);
		//create a handle
		StringHandle rawHandle = new StringHandle();
		rawHandle.withFormat(Format.JSON).set(rawJSONQuery);
		
		//create a raw query by example definition
		RawQueryByExampleDefinition querydef =
			    queryMgr.newRawQueryByExampleDefinition(rawHandle);
		
		// run the search
		SearchHandle resultsHandle = 
			    queryMgr.search(querydef, new SearchHandle());
			
	    Util.displayResults(resultsHandle);
	    System.out.println("\nReturned results for runTask1()\n\n");
	}
	  
		
	
	
	public static void runTask2(DatabaseClient client) 
	throws IOException {
	  
		QueryOptionsManager optionsMgr = client.newServerConfigManager().newQueryOptionsManager();
		optionsMgr.deleteOptions("cities");
		optionsMgr.deleteOptions("facets");
		optionsMgr.deleteOptions("products");
		optionsMgr.deleteOptions("tuples");
		
		System.out.println("\nDeleted options for runTask2()\n\n");
		
	}
}
