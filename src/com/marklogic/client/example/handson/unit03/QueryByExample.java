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
import com.marklogic.client.io.Format;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.RawCombinedQueryDefinition;
import com.marklogic.client.query.RawQueryByExampleDefinition;

public class QueryByExample {
	/*
	 * A QBE enables rapid prototyping of queries for 'documents that look like this' 
	 * using search criteria that resemble the structure of documents in your database
	 */
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

		runQBE(client);
		runQBEComplex(client);
		runJSONPropertyValueRangeQuery(client);
		runJSONCombinedQuery(client);

		// release the client
		client.release();
	}
	
	public static void runQBE(DatabaseClient client) 
    throws IOException 
	{
	    // create a manager for searching
		QueryManager queryMgr = client.newQueryManager();

		// create a raw query example
		String rawJSONQuery =
				"{"+
                   "\"$query\": { \"make\": \"Apple\" }"+
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
	    System.out.println("\nReturned results for runQBE()\n\n");
	}
	
	public static void runQBEComplex(DatabaseClient client) 
	throws IOException 
	{
		// create a manager for searching
	    QueryManager queryMgr = client.newQueryManager();

		// create a raw query example
	    // See JSON Query Options reference for examples
	    // http://docs.marklogic.com/guide/rest-dev/appendixa#chapter
		String rawJSONQuery =
				"{"+
		            "\"$query\": { "
						+"\"make\": \"Apple\" ,"
		                +"\"location\": {" 
				        + "\"city\": \"Amsterdam\""
						       + "}"
						+ "}"
		        +"}";
		
		//create a handle
		StringHandle rawHandle = new StringHandle();
		rawHandle.withFormat(Format.JSON).set(rawJSONQuery);
				
		//create a raw query by example definition
		RawQueryByExampleDefinition querydef =
			  queryMgr.newRawQueryByExampleDefinition(rawHandle);
		
		//how to validate the QBE
		StringHandle validationReport = 
			    queryMgr.validate(querydef, new StringHandle());	
		
		System.out.println("Validation Report "+validationReport.toString());

		// run the search
		SearchHandle resultsHandle = 
					     queryMgr.search(querydef, new SearchHandle());
					
		Util.displayResults(resultsHandle);
		System.out.println("\nReturned results for runQBEComplex()\n\n");
	}	
	
	//requires range index
	public static void runJSONPropertyValueRangeQuery(DatabaseClient client) 
	throws IOException 
	{
		// create a manager for searching
	    QueryManager queryMgr = client.newQueryManager();

		// create a raw query example
		String rawJSONQuery =
				"{"+
					"\"$query\": { \"country\":  { \"$ne\": \"Columbia\" }"+
		        "}}";
				
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
		System.out.println("\nReturned results for runJSONPropertyValueRangeQuery()\n");
	}	
	
	public static void runJSONCombinedQuery(DatabaseClient client)
	throws IOException 
	{
		/*
		 * Structure of a Combined Query
		 * {"search": {
                        "query": { structured query, same syntax as standalone },
                         "qtext": "your string query here",
                         "options": { same syntax as standalone query options },
                      } 
           } 
		 */
		
		// create a manager for writing query options
		QueryManager queryMgr = client.newQueryManager();
		 
		
	    // construct the query 
	    String rawJSONQuery =
	    "\"query\": { "
	    	+"\"queries\": [{"
	    	    +"\"term-query\": {"
	            +"\"text\": [ \"lge\" ]"
	            +"},"
	    		+"\"value-constraint-query\": {"
	                +"\"constraint-name\": \"foo\","
	                +"\"text\": [ \"Medellin\" ]  }"
                +"}]}"
        ;
		
		// construct the query options
        String options = new StringBuilder()
        .append("\"options\":"
                    +"{\"constraint\":["
        		    +"{\"name\":\"foo\","
                    +"\"value\":{\"json-property\":\"city\"}}]}")
        .toString();

        //combine the queries
        String comboq =
            "{\"search\": {" 
                + rawJSONQuery
                +","
                + options 
                +"}"
            +"}";
        
    	//create a handle
		StringHandle rawHandle = new StringHandle();
		rawHandle.withFormat(Format.JSON).set(comboq);
		
        System.out.println("comboq:\n"+comboq);
        
        // create a search definition for the search criteria
        RawCombinedQueryDefinition querydef
                = queryMgr.newRawCombinedQueryDefinitionAs(Format.JSON, comboq);
		
		// create a results handle
		SearchHandle resultsHandle = 
			    queryMgr.search(querydef, new SearchHandle());
		
        // run the search
        queryMgr.search(querydef, resultsHandle);
			
	    Util.displayResults(resultsHandle);
			    
	    System.out.println("Returned results for runJSONCombinedQuery()\n");
		client.release();
	}
	

}
