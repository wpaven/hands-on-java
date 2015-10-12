package com.marklogic.client.example.handson.unit04;

import java.io.IOException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.DocumentPatchBuilder;
import com.marklogic.client.document.DocumentPatchBuilder.Position;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.marker.DocumentPatchHandle;

/**
 *  UpdateDocument illustrates how to partially update document content
 */
public class UpdateDocument {
	
	public static void main(String[] args)
	throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props) throws IOException {
		System.out.println("example: "+UpdateDocument.class.getName());

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);
		
		//add state of main after location/city to document added in WriteDocumentJSON
	    updateDocument(client);

		// release the client
		client.release();
	}
	
	public static void updateDocument(DatabaseClient client) 
	throws IOException 
	{
		// create a manager for JSON documents
		JSONDocumentManager docMgr = client.newJSONDocumentManager();

		// create an identifier for the document
		String docId = "/image/20140721_144421b.jpg.json";
		
		//create a patch builder
		DocumentPatchBuilder builder = docMgr.newPatchBuilder();
        builder.insertFragment("/location/city", Position.AFTER, "{\"state\": \"Maine\"}");
        
        //create a handle associated with the patch
        DocumentPatchHandle handle = builder.build();
        
        //apply the patch
        docMgr.patch(docId, handle);

		System.out.println("Added {state: 'Maine'} after location/city to document "+docId);
	}
	
}
