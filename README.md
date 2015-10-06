# Hands-on Java: Working with MarkLogic's Java Client API

This project is used for hands-on sessions, guided by someone who is familiar
with the MarkLogic Java Client API. The goal is that, after going through 
the exercises, a developer will be comfortable using the API to interact with
MarkLogic. 

## Setup

1. Install MarkLogic 8.
2. Clone the [Geophoto repository][geophoto]. 
3. Follow Geophoto's setup instructions, including data import
4. Clone the hands-on-java repo and import into your favorite IDE
	-remember to reference the [Java Client API][javaclient] in your project
     
## Introduction

MarkLogic is a document- and triple-store database. One of the features in
MarkLogic 8 is a client API for Java developers. The exercises presented
here will give you a taste of how the Java Client API works. Note that it is
intended to get you started and show the power of the API, but it is not a full
replacement for MarkLogic University's class on the subject.

## GeoPhoto

This session is based on the [Geophoto application][geophoto]. Once you've
cloned and set up the application and imported the data, start the application
(see Geophoto's README for instructions).

### Features

Things to note in the Geophoto application:

- map view
  - click a marker
  - edit the image metadata
  - see the semantic info
- geo search
  - circle search
  - MarkLogic also supports arbitrary polygons

### Data Model

The database holds three types of data: binary images, metadata to describe
them, and triples to provide context.

### Execution

Trace the steps of editing the title of an image.

- views/partials/edit.jade
- public/js/editor/photoeditor.controller.js (updateTitle)
- public/js/data/photofactory.js (update)
- POST /api/image/update/:id/:update
- routes.js: update -> apiupdate -> updateDocument (read, edit, write)

[geophoto]: https://github.com/marklogic/Geophot
[javaclient]: http://developer.marklogic.com/products/java