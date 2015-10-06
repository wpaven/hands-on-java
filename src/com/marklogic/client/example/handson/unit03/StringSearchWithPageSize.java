package com.marklogic.client.example.handson.unit03;

import java.io.IOException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.FailedRequestException;
import com.marklogic.client.ForbiddenUserException;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.ResourceNotResendableException;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

public class StringSearchWithPageSize {
	
	public static int PAGE_SIZE = 5;

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
	    
	    // Set page size to 5 results
	    queryMgr.setPageLength(PAGE_SIZE);

	    // create a search definition
	    StringQueryDefinition query = queryMgr.newStringDefinition();
	    
	    //ask yourself - what is this searching on?
	    //ask yourself - what might you do differently with the criteria?
	    query.setCriteria("iphone 4");

	    // create a handle for the search results
	    SearchHandle resultsHandle = new SearchHandle();

	    // get the 3rd page of search results
	    int pageNum = 3;
	    int start = PAGE_SIZE * (pageNum - 1) + 1;
	 		
	    // run the search
	    queryMgr.search(query, resultsHandle, start);
	
	    Util.displayResults(resultsHandle);
	 }

}