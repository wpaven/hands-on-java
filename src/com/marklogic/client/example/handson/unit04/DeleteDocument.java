package com.marklogic.client.example.handson.unit04;

import java.io.IOException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.GenericDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;

/**
 *  DeleteDocument illustrates deleting documents
 */
public class DeleteDocument {
	public static void main(String[] args)
	throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props) throws IOException {
		System.out.println("example: "+DeleteDocument.class.getName());

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);

		// delete the documents that the examples wrote
	    deleteDocuments(client);
	}
	
	public static void deleteDocuments(DatabaseClient client) {
		GenericDocumentManager docMgr =  client.newDocumentManager();

		String jsonDocId = "/image/20140721_144421b.jpg.json";
		String binaryDocId = "/binary/20140721_144421b.jpg";

		docMgr.delete(jsonDocId,binaryDocId);
		
		System.out.println("Deleted "+ jsonDocId + " and "+ binaryDocId);
	}

}
