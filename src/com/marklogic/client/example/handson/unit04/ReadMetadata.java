package com.marklogic.client.example.handson.unit04;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.DocumentMetadataHandle.Capability;
import com.marklogic.client.io.DocumentMetadataHandle.DocumentCollections;
import com.marklogic.client.io.DocumentMetadataHandle.DocumentPermissions;
import com.marklogic.client.io.DocumentMetadataHandle.DocumentProperties;

/**
 *  ReadMetadata illustrates how to read document metadata
 *  Run WriteDocumentJSON if you have ran DeleteDocumen
 */
public class ReadMetadata {

	public static void main(String[] args)
	throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props) throws IOException {
		System.out.println("example: "+ReadMetadata.class.getName());

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);

		// read document metadata
	    readMetadata(client);
	}
	
	public static void readMetadata(DatabaseClient client) {
		//create a document manager for the appropriate document type
		JSONDocumentManager docMgr = client.newJSONDocumentManager();

		//create document identifier
		String docId = "/image/20140721_144421b.jpg.json";
		
		//create a metadata handle
		DocumentMetadataHandle metadataHandle = new DocumentMetadataHandle();
		
		//read metadata
		docMgr.readMetadata(docId, metadataHandle); 
		 
		//raw properties come back as XML
		//System.out.println(metadataHandle.toString());
		
		//get collections
		DocumentCollections collections = metadataHandle.getCollections();
		Iterator<String> colls = collections.iterator();
	    while (colls.hasNext()){
	      System.out.println("Collection: "+ colls.next());
	    }
		 
		//get properties
		DocumentProperties properties = metadataHandle.getProperties();
		System.out.println("Properties"+properties.toString());
		
		//add properties to document
		//metadataHandle.getProperties().put("name", "value");
		
	    //get permissions
		DocumentPermissions permissions = metadataHandle.getPermissions();
		Set<String> perms = permissions.keySet();
		
		
		for (String p : perms) {
			Set<Capability> value = permissions.get(p);
		    System.out.println("Role Name: "+ p + " Capability: "+ value.toString());
		}
	    //get quality
		int quality = metadataHandle.getQuality();
		System.out.println("Quality: "+quality);
		
		//write added properties
		//docMgr.writeMetadata(docId, metadataHandle); 
		
		System.out.println("\nRead metadata for document" + docId);
	}
}
