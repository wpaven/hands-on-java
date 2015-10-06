package com.marklogic.client.example.handson.unit03;

import java.io.IOException;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.FailedRequestException;
import com.marklogic.client.ForbiddenUserException;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.ResourceNotResendableException;
import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.TuplesHandle;
import com.marklogic.client.query.FacetResult;
import com.marklogic.client.query.FacetValue;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.query.Tuple;
import com.marklogic.client.query.ValuesDefinition;

public class Facets {
	
	static final private String FACET_OPTIONS_NAME = "facets";
	static final private String TUPLE_OPTIONS_NAME = "tuples";
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

		configureFacetOptions(client);
		searchWithFacets(client);
		configureTupleOptions(client);
		retrieveTuples(client);

		// release the client
		client.release();
	}
	
	public static void configureFacetOptions(DatabaseClient client)
	throws IOException 
	{
		// create a manager for writing query options
		QueryOptionsManager optionsMgr = client.newServerConfigManager().newQueryOptionsManager();

		// construct the query options
        String options = new StringBuilder()
        .append("{\"options\":"
                    +"{\"constraint\":["
        		    +"{\"name\":\"facets\","
                    +"\"range\":{\"json-property\":\"city\"}}]}}")
        .toString();

        System.out.println("Query Options Are: "+options+"\n");
        
        // create a handle to send the query options
        StringHandle writeHandle = new StringHandle();
        
        //set format if persisting options as JSON
        writeHandle.withFormat(Format.JSON).set(options);

		// write the query options to the database
		optionsMgr.writeOptions(FACET_OPTIONS_NAME, writeHandle);

		System.out.println("Query Options saved for configureFacetOptions()\n");

	}
	
	public static void searchWithFacets(DatabaseClient client) 
    throws IOException 
	{
	    // create a manager for searching
	    QueryManager queryMgr = client.newQueryManager();

	    // create a search definition
	    StringQueryDefinition query = queryMgr.newStringDefinition();
	    
	    //search term example
	    query.setCriteria("lge");
	    query.setOptionsName(FACET_OPTIONS_NAME);
	    
	    // create a handle for the search results
	    SearchHandle resultsHandle = new SearchHandle();

	    // run the search
	    queryMgr.search(query, resultsHandle);
	    
	 // Show the resulting facets & their values
	    for (FacetResult facet : resultsHandle.getFacetResults()) {
	        System.out.println(facet.getName() + ":");
	        for (FacetValue value : facet.getFacetValues()) {
	            System.out.println("  " + value.getCount() + " occurrences of " + value.getName());
	        }
	    }
	
	   // Util.displayResults(resultsHandle);
	    System.out.println("Returned facets for searchFacets()\n");
	 }
	
	public static void configureTupleOptions(DatabaseClient client)
	throws IOException 
	{
		//add element range index of type string on 'make' for geophoto-content database
		
		// create a manager for writing query options
		QueryOptionsManager optionsMgr = client.newServerConfigManager().newQueryOptionsManager();

		// construct the query options
        String options = new StringBuilder()
            .append("{"
        	+"\"options\": {"
        	  +"\"tuples\": [{"
        	      +"\"name\": \"city-make\","
        	      +"\"range\": ["
        	         +"{"
        	           +"\"type\": \"xs:string\","
        	           +"\"json-property\": \"city\""
        	         +"},"
        	         +"{"
        	           +"\"type\": \"xs:string\","
                       +"\"json-property\": \"make\""
        	         +"}"
        	    +"]}"
        	 +"]}}")
        	 .toString();

        //System.out.println("QueryTuple Options Are: "+options+"\n");
        
        // create a handle to send the query options
        StringHandle writeHandle = new StringHandle();
        
        //set format if persisting options as JSON
        writeHandle.withFormat(Format.JSON).set(options);

		// write the query options to the database
		optionsMgr.writeOptions(TUPLE_OPTIONS_NAME, writeHandle);

		System.out.println("Query Options saved for configureTupleOptions()\n");

	}
	
	public static void retrieveTuples(DatabaseClient client) 
	throws IOException 
	{
		// create a manager for searching
		QueryManager queryMgr = client.newQueryManager();
		
		// create a values definition
		ValuesDefinition valuesDef = queryMgr.newValuesDefinition("city-make", TUPLE_OPTIONS_NAME);
		 
		// retrieve the tuples
		TuplesHandle tuplesHandle = queryMgr.tuples(valuesDef, new TuplesHandle());
		
		// print out each city/make co-occurrence
		for (Tuple tuple : tuplesHandle.getTuples()) {
		    System.out.println("City: "     + tuple.getValues()[0].get(String.class)
		                   + "\nMake: " + tuple.getValues()[1].get(String.class));
		    System.out.println();
		}
			
	    // Util.displayResults(resultsHandle);
		System.out.println("Returned tuples for searchTuples()\n");
	}

}