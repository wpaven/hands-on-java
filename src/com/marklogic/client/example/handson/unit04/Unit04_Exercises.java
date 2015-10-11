package com.marklogic.client.example.handson.unit04;

public class Unit04_Exercises {
	
	//Task 1 
	// The Geophoto application lets us provide a title for a picture. Add a title
	// property with the value "Portland Head Lighthouse". This should be a child
	// of the root element. When you're done, '/image/20140721_144421b.jpg.json'
	// should look like this:
	// {
	//   "originalFilename":"../data/photos/20140721_144421b.jpg",
	//   "filename":"20140721_144421b.jpg",
	//   "binary":"/binary/20140721_144421b.jpg",
	//   "make":"SAMSUNG",
	//   "model":"SAMSUNG-SM-G900A",
	//   "created":1405968260000,
	//   "location":{
    //	     "type":"Point",
    //	     "coordinates":[43.6231, -70.208058],
    //	     "city":"Cape Elizabeth",
    //	     "state": "Maine",
    //	     "country":"United States"
	//   },
	//   "title":"Portland Head Lighthouse"
	// }
	
	//Task 2
	// The image we inserted above describes the camera used to
	// take the picture. In this exercise, we'll change the model. In the original
	// document, the model is "SAMSUNG-SM-G900A". Change it to "Galaxy S5".
	//
	// {
    //  "originalFilename":"../data/photos/20140721_144421b.jpg",
    //  "filename":"20140721_144421b.jpg",
    //  "binary":"/binary/20140721_144421b.jpg",
    //  "make":"SAMSUNG",
    //  "model":"Galaxy S5",
    //  "created":1405968260000,
    //  "location":{
    //    "type":"Point",
    //    "coordinates":[43.6231, -70.208058],
    //    "city":"Cape Elizabeth",
    //    "state": "Maine",
    //    "country":"United States"
    //  },
    //  "title":"Portland Head Lighthouse"
    //}

	//Task 3
	// In AddCollection, '/image/20140721_144421b.jpg.json' was added
	// to the 'phone' collection. Now remove it from that collection

}
