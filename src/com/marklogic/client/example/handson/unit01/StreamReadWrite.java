/*
 * BytesHandle, FileHandle, InputStreamHandle, OutputStreamHandle.
 */
package com.marklogic.client.example.handson.unit01;
 

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.xpath.XPathExpressionException;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.BinaryDocumentManager;
import com.marklogic.client.example.handson.Util;
import com.marklogic.client.example.handson.Util.ExampleProperties;
import com.marklogic.client.io.BytesHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.OutputStreamHandle;
import com.marklogic.client.io.OutputStreamSender;

public class StreamReadWrite {
	public static void main(String[] args)
	throws IOException, XPathExpressionException {
		run(Util.loadProperties());
	}

	public static void run(ExampleProperties props)
	throws IOException {
		System.out.println("example: "+StreamReadWrite.class.getName()+"\n");

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(
				props.host, props.port, props.adminUser, props.adminPassword,
				props.authType);
				
		writeBinaryContent(client);
		readWriteBinaryStream(client);
		readBinaryBuffer(client);
		
		// release the client
		client.release();
	}
	
	/**
	 * Demonstrates use of BinaryDocumentManager. 
	 * Use BinaryDocumentManager for insert or update of binary documents.  
	 **/
	public static void writeBinaryContent(DatabaseClient client) 
	throws IOException {
	
		//create manager for Binary Documents
		BinaryDocumentManager docMgr = client.newBinaryDocumentManager();
		
		// create an identifier for the document
		String docId = "/binary/mlfavicon-1.png";
		
		// define mimetype
		String mimetype = "image/png";
		
		//write content to the database
		docMgr.write(
				docId,
				new FileHandle().with(new File(StreamReadWrite.class.getProtectionDomain().getCodeSource().getLocation().getPath()
						                         + File.separator +"data"
						                         + File.separator + "mlfavicon.png")).withMimetype(mimetype)
				    );
		
		System.out.println("writeBinaryContent() - Wrote "+docId+" content. \n");
		
	}

	/**
	 * Demonstrates how to write content to a document
	 * using an OutputStream. You provide the content during execution of
     * the write operation instead of when starting the write operation.
	 **/
	public static void readWriteBinaryStream(DatabaseClient client) 
	throws IOException {
		
		BinaryDocumentManager docMgr = client.newBinaryDocumentManager();
		
		final int    MAX_BUF  = 8192;
		final String FILENAME = "mlfavicon.png";
		//String docIdToRead = "/binary/mlfavicon.png";
		String docIdToRead = "data"+File.separator+FILENAME;
		String docIdToWrite = "/binary/mlfavicon-2.png";
				
		// create an anonymous class with a callback method
		OutputStreamSender sender = new OutputStreamSender() {
		   // the callback receives the output stream
			public void write(OutputStream out) throws IOException {
				
		        // acquire the content from the database
				/* InputStream docStream =
						docMgr.read(docIdToRead, new InputStreamHandle()).get();
						if (docStream == null)
							throw new IOException("Could not read document example"); 
				*/
			
				// acquire the content from the file system
				InputStream docStream = Util.openStream(docIdToRead);
        		if (docStream == null)
		      			throw new IOException("Could not read document example");

		        // copy content to the output stream
		       	byte[] buf = new byte[MAX_BUF];
		      	int byteCount = 0;
		        while ((byteCount=docStream.read(buf)) != -1) {
		        	out.write(buf, 0, byteCount);
		        }
		    }
		};
		
        // create a handle for writing the content
		OutputStreamHandle handle = new OutputStreamHandle(sender);

		// write the document content
		docMgr.write(docIdToWrite, handle);

		System.out.println("readWriteBinaryStream() - \n"
				           + "Read " + docIdToRead + " content. \n"
				           + "Wrote "+ docIdToWrite +" content. \n");

	}
	
	/**
	 * Demonstrates BytesHandle to buffer binary content 
	 **/
	public static void readBinaryBuffer(DatabaseClient client) 
	throws IOException {
		//create manager for Binary Documents
		BinaryDocumentManager docMgr = client.newBinaryDocumentManager();
		
		// create an identifier for the document
		String docId = "/binary/mlfavicon-2.png";

		InputStream docStream =
					docMgr.read(docId, new InputStreamHandle()).get();
					if (docStream == null)
						throw new IOException("Could not read document example"); 

	    // create a handle to receive the document content
		BytesHandle readHandle = new BytesHandle();

	    // read the document content
		docMgr.read(docId, readHandle);
		
		//to read part of the content
		//docMgr.read(docId, readHandle,9,10);
		
		// access the document content
		byte[] buf = readHandle.get();
		
		// ... do something with the document content ...

		System.out.println("\nRead "+docId+" with buffer having length: "
				               + buf.length ); 
	}
	



}