Bazooka
======

Bazooka is a generic shooter application designed to test and debug APIs,
retrieving a response for the provided request and configuration. The main
idea is to allow developers to create plug-ins that will reuse a set of
available libraries (mostly web services-related) through scripting languages
(e.g. Groovy, JRuby, Scala, Rhino, etc).


Get the application running
------

* /Toolbox/gwt-2.0.0-ms2
* /Toolbox/repository/com/google/gwt/gwt-dev/2.0.0-ms2/gwt-dev-2.0.0-ms2.jar
* /Toolbox/repository/com/google/gwt/gwt-servlet/2.0.0-ms2/gwt-servlet-2.0.0-ms2.jar
* /Toolbox/repository/com/google/gwt/gwt-user/2.0.0-ms2/gwt-user-2.0.0-ms2.jar
* /Toolbox/repository/com/gwtext/gwtext/2.0.5/gwtext-2.0.5.jar
* /Workspace/bazooka > mvn clean package jetty:run-war


Author
------

Tiago Fernandez
[Blog][b] | [Twitter][t]

[b]: http://tiagofernandez.blogspot.com
[t]: http://twitter.com/tiagofernandez