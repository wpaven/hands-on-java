/*
 * 
 */
package com.marklogic.client.example.handson.unit01;
 

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.StringHandle;

public class ReadDocumentJSON {
	public static void main(String[] args)
	throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props)
	throws IOException {
		System.out.println("example: "+ReadDocumentJSON.class.getName()+"\n");

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);
				
		// use either shortcut or strong typed IO
		runShortcutString(client);
		runShortcutJSON(client);
		runStrongTyped(client);

		// release the client
		client.release();
	}
	
	public static void runShortcutString(DatabaseClient client) 
	throws IOException {
	
		JSONDocumentManager docMgr = client.newJSONDocumentManager();
		// create an identifier for the document
		String docId = "/image/01.JPG.json";
		
		// read the document content as string
		String doc = docMgr.read(docId, new StringHandle()).get();
		
		System.out.println("(Shortcut String) Read "+docId+" with content: \n"+ doc + "\n\n");
		
	}
	
	public static void runShortcutJSON(DatabaseClient client) 
	throws IOException {
		
		JSONDocumentManager docMgr = client.newJSONDocumentManager();
		// create an identifier for the document
		String docId = "/image/01.JPG.json";
		
		// read the document as JSON shortcut
		JsonNode readDocument = docMgr.readAs(docId, JsonNode.class);
		String aRootField = readDocument.fieldNames().next();
		
		System.out.println("(Shortcut JSON) Read "+docId+" with content: \n"+ readDocument +
				           "having root fieldName "+aRootField +"\n\n");
		
	}
	
	public static void runStrongTyped(DatabaseClient client) 
	throws IOException {
		// create a manager for JSON documents
		JSONDocumentManager docMgr = client.newJSONDocumentManager();

		// create an identifier for the document
		String docId = "/image/01.JPG.json";
		
		//read the document as JSON strongly typed
		JacksonHandle readHandle = new JacksonHandle();
		docMgr.read(docId, readHandle);
		JsonNode readDocument = readHandle.get();
		//String aRootField = readDocument.fieldNames().next();
		
		
		Iterator<String> fieldNamesIter = readDocument.fieldNames();
	    while (fieldNamesIter.hasNext()){
	      System.out.println("FieldName: "+ fieldNamesIter.next());
	    }

		System.out.println("\nRead "+docId+" with content: \n"+ readDocument); 
	         
	    }

	


}
