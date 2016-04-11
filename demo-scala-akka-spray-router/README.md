# demo-scala-akka-spray-router

REST services with Scala, Akka, Spray: Part 2

The main idea of actor-based concurrency is that actors (concurrent objects with behavior and state which execute asynchronously) could communicate only by sending immutable messages. Messages in Akka [can be sent](http://doc.akka.io/docs/akka/current/scala/actors.html#Send_messages) ether by tell or ask methods. Tell method (“!” in Scala) works in "fire-and-forget" mode, it sends a message asynchronously and returns immediately. This method should be used for performance. Ask method (“?” in Scala) sends a message asynchronously as well but returns a Future representing a possible reply. This method should be used only for special purposes, like communication between an actor-based library and the rest of the application.

To make the next step, you can visit the GitHub repository and browse through the second example. This is a router - a REST server, which by-turn makes a REST client call to fulfill its server request. In this part you can get to know how to:

* call ask method with an implicit timeout to run a HTTP server
* call tell methods to send and receive messages between actors
* use different types of Scala pattern matching to sort incoming messages
* make low-level and high-level HTTP client calls
* change actor's behavior with become method

To be continued.
