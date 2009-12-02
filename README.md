Bazooka
======

Bazooka is a generic shooter application designed to test and debug APIs,
retrieving a response for the provided request and configuration. The main
idea is to allow developers to create plug-ins that will reuse a set of
available libraries (mostly web services-related) through scripting languages
(e.g. Groovy, JRuby, Scala, Rhino, etc). Currently only Groovy is supported.

Bazooka is built with Google Web Toolkit 2.0 and Guice 2.0. The back-end uses
the Java Persistence API with Hibernate as concrete implementation targeting
an H2 database, and Groovy 1.6.5 is utilized as scripting engine.


Get the application running
------

* /Toolbox/gwt-2.0.0-rc2
* /Toolbox/repository/com/google/gwt/gwt-dev/2.0.0-rc2/gwt-dev-2.0.0-rc2.jar
* /Toolbox/repository/com/google/gwt/gwt-servlet/2.0.0-rc2/gwt-servlet-2.0.0-rc2.jar
* /Toolbox/repository/com/google/gwt/gwt-user/2.0.0-rc2/gwt-user-2.0.0-rc2.jar
* /Toolbox/repository/com/gwtext/gwtext/2.0.5/gwtext-2.0.5.jar
* /Workspace/bazooka > mvn clean package jetty:run-war


Write your own shooter
------

HTTP Shooter:

import org.apache.commons.httpclient.*
import org.apache.commons.httpclient.methods.*

method = new GetMethod(url)

new HttpClient().executeMethod(method)
response = new String(method.responseBody)

method.releaseConnection()

response


Bound variables
------

* request: the provided payload
* parameters: a map containing the provided parameters

PS: Additionally, each parameter is individually put as binding variables.


Author
------

Tiago Fernandez (2009) | [Blog][b] | [Twitter][t]

[b]: http://tiagofernandez.blogspot.com
[t]: http://twitter.com/tiagofernandez