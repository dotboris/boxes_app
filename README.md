Boxes App
=========

[![Build Status](https://travis-ci.org/beraboris/boxes_app.svg)](https://travis-ci.org/beraboris/boxes_app)

This is the client portion of a collaborative drawing app called Boxes. This is an Android application.

Boxes is a school project and is meant to be more of a proof of concept than anything else. This is, of course, not
meant for production.

For this app to work, you'll need to have the [server](https://github.com/beraboris/boxes_server) up and running.

Dependencies
============

- Some version of java
- The android SDK
- Android build tools 21.1.1
- Android sdk platform 21

Building
========

Compiling is done through the `gradlew` wrapper (`gradlew.bat` on windows). To build the app apk, run:

    $ ./gradlew assemble

The different versions of the apk can be found in `app/build/outputs/apk`. These can be installed on an android device.

The minimum sdk version is 16. This means that the app should work on android 4.1+. Note that the app was only tested
on android 4.1.2 and android 4.4.2.

Running
=======

To run the app you'll need to have the server component running. You will also need to know the url for the gallery and
drive through services. These are HTTP urls. In both cases, it should have the following form: `http://<host>:<port>`
where host is the ip address of the machine running the service and the port is the port the gallery / drive through
service is running on.

The host should be pretty easy to figure out. Figuring out the port number is explained in the 
[server instructions](https://github.com/beraboris/boxes_server#running-the-services).

Once the server is up and you have both urls, you need to go in the app's settings (Menu -> Settings) and set the url
for both services.
