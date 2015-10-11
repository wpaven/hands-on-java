package com.marklogic.client.example.handson.unit04;

import java.io.IOException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JacksonHandle;
/**
 *  AddCollection illustrates how to write metadata content to a database document.
 */
public class AddCollection {
	public static void main(String[] args)
	throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props) throws IOException {
		System.out.println("example: "+AddCollection.class.getName());

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);

		//Run WriteDocumentJSON again to insert the document for this example as needed
		
		//write metadata only for the document
	    addCollection(client);
		//write content and metadata for the document
		addCollectionWithRead(client);

		// release the client
		client.release();
	}
	
	public static void addCollection(DatabaseClient client) 
	throws IOException 
	{
		// create a manager for JSON documents
		JSONDocumentManager docMgr = client.newJSONDocumentManager();

		// create an identifier for the document
		String docId = "/image/20140721_144421b.jpg.json";

		// create and initialize a handle on the metadata
		DocumentMetadataHandle metadataHandle = new DocumentMetadataHandle();
		metadataHandle.getCollections().addAll("phone");

		// write the document metadata
		docMgr.writeMetadata(docId, metadataHandle);

		System.out.println("Added "+docId+" to the phone collection.");
	}
	
	public static void addCollectionWithRead(DatabaseClient client) 
	throws IOException 
	{
		// create a manager for JSON documents
		JSONDocumentManager docMgr = client.newJSONDocumentManager();

		// create an identifier for the document
		String docId = "/image/20140721_144421b.jpg.json";
	
		//read the document as JSON
		JacksonHandle readHandle = new JacksonHandle();
		docMgr.read(docId, readHandle);

		// create and initialize a handle on the metadata
		DocumentMetadataHandle metadataHandle = new DocumentMetadataHandle();
		metadataHandle.getCollections().addAll("phone");

		// write the document metadata and content
		docMgr.write(docId, metadataHandle, readHandle);

		System.out.println("Wrote content and added "+docId+" to the phone collection.");
	}
	
}
