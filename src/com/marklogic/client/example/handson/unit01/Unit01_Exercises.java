package com.marklogic.client.example.handson.unit01;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.BinaryDocumentManager;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.JacksonHandle;

public class Unit01_Exercises {

//Task 1: Print the model used where image 03.JPG was taken.
          //URI: /image/03.JPG.json
          //JSON path within document: model
	
//Task 2: Delete the /binary/mlfavicon-2.png document
	
//Task 3 (Optional): Print the city  where image 03.JPG was taken.
//URI: /image/03.JPG.json
//JSON path within document: location.city
	public static void main(String[] args)
	throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props)
	throws IOException {
		System.out.println("example: "+Unit01_Exercises.class.getName()+"\n");

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);
				
		// run tasks
		runTask1(client);
		runTask2(client);

		// release the client
		client.release();
	}
	public static void runTask1(DatabaseClient client) 
	throws IOException {
		// create a manager for XML documents
		JSONDocumentManager docMgr = client.newJSONDocumentManager();

		// create an identifier for the document
		String docId = "/image/03.JPG.json";
		
		//read the document as JSON strongly typed
		JacksonHandle readHandle = new JacksonHandle();
		docMgr.read(docId, readHandle);
		JsonNode readDocument = readHandle.get();

		System.out.println("\nRead "+docId+" with content: \n"+ readDocument); 
		
		Iterator<Map.Entry<String,JsonNode>> fieldsIter = readDocument.fields();
	    while (fieldsIter.hasNext()){
	      Map.Entry myEntry = fieldsIter.next();
	     // System.out.println("key: "+ myEntry.getKey());
	      
	      if(myEntry.getKey().equals("model"))
	      {
	    	  System.out.println("\nModel : "+ myEntry.getValue() );
	      }
	      
	         
	    }

	}
	
	public static void runTask2(DatabaseClient client) 
	throws IOException {
		
		//create manager for Binary Documents
		BinaryDocumentManager docMgr = client.newBinaryDocumentManager();
		
		// create an identifier for the document
		String docId = "/binary/mlfavicon-2.png";
		docMgr.delete(docId);
		
		System.out.println("\nDeleted "+docId+"  from the database.");
		
	}
}
