package com.marklogic.client.example.handson.unit04;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.DocumentWriteSet;
import com.marklogic.client.document.GenericDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.InputStreamHandle;
/**
 *  WriteMultiDocument illustrates how to write a mixed document set to the database
 *  For multi-document write, use DocumentWriteSet
 */
public class WriteMultiDocument {
	
	public static void main(String[] args) throws IOException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props) throws IOException {
		System.out.println("example: "+WriteMultiDocument.class.getName());

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);

		// use either shortcut or strong typed IO
		loadContent(client);
		tearDownExample(client);

		// release the client
		client.release();
	}

	
	public static void loadContent(DatabaseClient client) throws IOException {
		String jsonFile = "20140721_144421b.jpg.json";
		String binaryFile = "20140721_144421b.jpg";

		// acquire the content
		InputStream jsonContent = Util.openStream("data"+File.separator+jsonFile);
		if (jsonContent == null)
			throw new IOException("Could not read json document example");
		
		 FileHandle binaryContent = 
		          new FileHandle().with(new File(WriteMultiDocument.class.getProtectionDomain().getCodeSource().getLocation().getPath()
	                         + File.separator +"data"
	                         + File.separator + binaryFile)).withMimetype("image/jpeg");

		// create a generic manager for mixed type documents
		//When you use GenericDocumentManager, you must either use handles that 
		//imply a specific document or content type, or explicitly set it
		GenericDocumentManager docMgr = client.newDocumentManager();

		// create identifiers for the documents
		String jsonDocId = "/image/"+jsonFile;
		String binaryDocId = "/binary/"+binaryFile;

		// create a handle on the content
		InputStreamHandle handle = new InputStreamHandle();
		handle.set(jsonContent);

	    DocumentWriteSet batch = docMgr.newWriteSet();
	    batch.add(jsonDocId, handle);
	    batch.add(binaryDocId, binaryContent);
	    docMgr.write(batch);

		System.out.println("Loaded files: \n "+ jsonDocId +"\n"+binaryDocId);
	}
	
	// clean up by deleting the document that the example wrote
	public static void tearDownExample(DatabaseClient client) {
		GenericDocumentManager docMgr =  client.newDocumentManager();

		String jsonDocId = "/image/20140721_144421b.jpg.json";
		String binaryDocId = "/binary/20140721_144421b.jpg";

		docMgr.delete(jsonDocId,binaryDocId);
		
		System.out.println("Deleted "+ jsonDocId + " and "+ binaryDocId);
	}


}
