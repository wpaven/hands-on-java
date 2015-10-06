package com.marklogic.client.example.handson.unit03;

import java.io.IOException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.FailedRequestException;
import com.marklogic.client.ForbiddenUserException;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.ResourceNotResendableException;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

public class StringSearch {
	
	public static void main(String[] args)
	throws IOException, ResourceNotFoundException, ForbiddenUserException, FailedRequestException, ResourceNotResendableException {
		run(Util.loadProperties());
	}
	
	public static void run(ExampleProperties props)
	throws IOException, ResourceNotFoundException, ForbiddenUserException, FailedRequestException, ResourceNotResendableException {
		System.out.println("example: "+StringSearch.class.getName());
		
		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);

		search(client);

		// release the client
		client.release();
	}
	
	public static void search(DatabaseClient client) 
    throws IOException 
	{
	    // create a manager for searching
	    QueryManager queryMgr = client.newQueryManager();

	    // create a search definition
	    StringQueryDefinition query = queryMgr.newStringDefinition();
	    
	    //search term example
	    query.setCriteria("apple");
	    
	    //search grammar example
	    //query.setCriteria("index OR Cassel NEAR Hare");
	    
	    //try the following without phrase search 
	    //(remove quotes around san francisco)
	    //to view difference in results
	    //query.setCriteria("atlanta OR \"san francisco\""); 

	    // create a handle for the search results
	    SearchHandle resultsHandle = new SearchHandle();

	    // run the search
	    queryMgr.search(query, resultsHandle);
	
	    Util.displayResults(resultsHandle);
	 }

}
