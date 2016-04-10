# demo-scala-akka-spray-basic

REST services with Scala, Akka, Spray: Part 1

Apart from the Java Servlet API 3.x, it’s possible to build high-performance REST services on other technologies. One of them is [Akka](http://akka.io/) toolkit, which provides actor-based concurrency as high-performance, high-available alternative to thread-based concurrency with lock-based synchronization. There’re some Web frameworks built on top of Akka, that allow to build REST services, but this example is about [Spray](http://spray.io/). (Despite this example is written in Scala, it’s possible to write it in Java as well).

To make a quick start with these tools, you can visit the GitHub repository and browse through the first example (this is a simple REST server). In this part you can get to know how to:

* create an SBT project
* bootstrap an application and run a HTTP server
* write the application’s configuration
* write the HTTP server’s routes with spray-routing
* convert objects to JSON with spray-json
* write unit tests for the HTTP servers with spray-testkit and scalatest

To be continued.
