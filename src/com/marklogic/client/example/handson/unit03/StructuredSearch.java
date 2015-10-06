package com.marklogic.client.example.handson.unit03;

import java.io.IOException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.FailedRequestException;
import com.marklogic.client.ForbiddenUserException;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.ResourceNotResendableException;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.RawCombinedQueryDefinition;
import com.marklogic.client.query.RawQueryByExampleDefinition;
import com.marklogic.client.query.RawStructuredQueryDefinition;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;
/**
 * StructuredSearch illustrates searching for documents and iterating over results
 * with structured criteria referencing a constraint defined by options.
 */
public class StructuredSearch {
	
	static final private String OPTIONS_NAME = "cities";

	public static void main(String[] args)
	throws IOException, ResourceNotFoundException, ForbiddenUserException, FailedRequestException, ResourceNotResendableException {
		run(Util.loadProperties());
	}
	
	public static void run(ExampleProperties props)
	throws IOException, ResourceNotFoundException, ForbiddenUserException, FailedRequestException, ResourceNotResendableException {
		System.out.println("example: "+StructuredSearch.class.getName());
		
		configure(props.host, props.port,
				props.adminUser, props.adminPassword, props.authType);
		
		runJSONPropertyStructuredQuery(props.host, props.port,
				props.adminUser, props.adminPassword, props.authType);
		
		//runJSONPropertyStructuredQueryDynamicOptions(props.host, props.port,
		//		props.adminUser, props.adminPassword, props.authType);
		
		runJSONPropertyStructuredQueryValueConstraint(props.host, props.port,
				props.adminUser, props.adminPassword, props.authType);
		
		runFetchQueryOptions(props.host, props.port,
				props.adminUser, props.adminPassword, props.authType);

	}
	
	public static void configure(String host, int port, String user, String password, Authentication authType)
	throws IOException 
	{
		DatabaseClient client = DatabaseClientFactory.newClient(host, port, user, password, authType);
		
		// create a manager for writing query options
		QueryOptionsManager optionsMgr = client.newServerConfigManager().newQueryOptionsManager();

		// construct the query options
        String options = new StringBuilder()
        .append("{\"options\":"
                    +"{\"constraint\":["
        		    +"{\"name\":\"city\","
                    +"\"value\":{\"json-property\":\"city\"}}]}}")
        .toString();

        System.out.println("Query Options Are: "+options+"\n");
        
        // create a handle to send the query options
        StringHandle writeHandle = new StringHandle();
        
        //set format if persisting options as JSON
        writeHandle.withFormat(Format.JSON).set(options);

		// write the query options to the database
		optionsMgr.writeOptions(OPTIONS_NAME, writeHandle);

		System.out.println("Query Options saved for configure()\n");

		// release the client
		client.release();
	}
	
	//Term queries and word queries differ primarily in how they handle containment.
	//A term query finds matches anywhere within its context container, while a word query matches only immediate children.
	//Many sub-query types constrain matches to the context of a particular container. 
	//A container is a JSON property, XML element, or XML element attribute.
	//You can wrap a query in a container-query to further limit the scope of the matches. 
	//A structured query allows us to search for the term 'lge', when it's only the value of a property 'make'
	//where a term query for 'lge' would return hits anywhere 'lge' in the document is found.
	
	public static void runJSONPropertyStructuredQuery(String host, int port, String user, String password, Authentication authType)
    throws IOException 
	{
		DatabaseClient client = DatabaseClientFactory.newClient(host, port, user, password, authType);
	    // create a manager for searching
	    QueryManager queryMgr = client.newQueryManager();

		// create a query builder
        StructuredQueryBuilder qb = new StructuredQueryBuilder();

        // build a search definition
        StructuredQueryDefinition querydef = 
        		   qb.and(qb.containerQuery(qb.jsonProperty("make"), qb.term("lge")),
        				   qb.term("medellin"));

	    // create a handle for the search results
	    SearchHandle resultsHandle = new SearchHandle();
	    
		// run the search
		queryMgr.search(querydef, resultsHandle);
	
	    Util.displayResults(resultsHandle);
	    
	    System.out.println("Returned results for runJSONPropertyStructuredQuery()\n");
	    client.release();
	 }
	
	public static void runJSONPropertyStructuredQueryDynamicOptions(String host, int port, String user, String password, Authentication authType)
	throws IOException 
	{// SEE http://docs.marklogic.com/guide/java/searches#id_50826
		/*
		 * Structure of a Combined Query
		 * {"search": {
                        "query": { structured query, same syntax as standalone },
                         "qtext": "your string query here",
                         "options": { same syntax as standalone query options },
                      } 
           } 
		 */
		DatabaseClient client = DatabaseClientFactory.newClient(host, port, user, password, authType);
		
		// create a manager for writing query options
		QueryManager queryMgr = client.newQueryManager();
		 
		// construct the query options
        String options = new StringBuilder()
        .append("{\"options\":"
                    +"{\"constraint\":["
        		    +"{\"name\":\"city\","
                    +"\"value\":{\"json-property\":\"city\"}}]}}")
        .toString();
        
        StructuredQueryBuilder qb = queryMgr.newStructuredQueryBuilder();
        StructuredQueryDefinition rsq = qb.containerQuery(qb.jsonProperty("make"), qb.term("Apple"));
          
          //qb.build( qb.term("apple"), 
          //         qb.valueConstraint("city", "Tokyo")); //setHandle(rawHandle);
        
        
        System.out.println("rawHandle"+rsq.serialize());
        String comboq =
            "{\"search\": {" 
                + rsq.serialize()
                + options 
                +"}"
            +"}";
        System.out.println("comboq:\n"+comboq);
        
       
        RawCombinedQueryDefinition query = 
                queryMgr.newRawCombinedQueryDefinition(new StringHandle(comboq));

        // create a handle for the search results
	    SearchHandle resultsHandle = new SearchHandle();
        // run the search
        queryMgr.search(query, resultsHandle);
			
	    Util.displayResults(resultsHandle);
			    
	    System.out.println("Returned results for runJSONPropertyStructuredQueryDynamicOptions()\n");
		client.release();
	}	
	
	public static void runJSONPropertyStructuredQueryValueConstraint
	(String host, int port, String user, String password, Authentication authType)
	throws IOException 
	{
		DatabaseClient client = DatabaseClientFactory.newClient(host, port, user, password, authType);
		// create a manager for searching
		QueryManager queryMgr = client.newQueryManager();

		// create a query builder using the query options
		StructuredQueryBuilder qb = new StructuredQueryBuilder(OPTIONS_NAME);

        // build a search definition
        StructuredQueryDefinition querydef
                = qb.and(qb.term("apple"), qb.valueConstraint("city", "Tokyo"));
        
	    // create a handle for the search results
	    SearchHandle resultsHandle = new SearchHandle();

		// run the search
	    queryMgr.search(querydef, resultsHandle);
			
		Util.displayResults(resultsHandle);
			    
	    System.out.println("Returned results for runJSONPropertyStructuredQueryValueConstraint()\n");
		client.release();
	}
	
	public static void runFetchQueryOptions(String host, int port, String user, String password, Authentication authType)
	throws IOException 
	{
		DatabaseClient client = DatabaseClientFactory.newClient(host, port, user, password, authType);
		String optionsName = OPTIONS_NAME;

		// create a manager for writing, reading, and deleting query options
		QueryOptionsManager optionsMgr = client.newServerConfigManager().newQueryOptionsManager();

		// read the query options from the database
		String readOptions = optionsMgr.readOptionsAs(optionsName, Format.JSON, String.class);
			    
		System.out.println("Returned results for runFetchQueryOptions()\n"+
			                       readOptions);
		client.release();
	}

}

