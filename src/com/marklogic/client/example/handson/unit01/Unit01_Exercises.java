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
		
		// release the client
		client.release();
	}

}
