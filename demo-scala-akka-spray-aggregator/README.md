# demo-scala-akka-spray-aggregator

REST services with Scala, Akka, Spray: Part 3

One of the common patterns in actor-based applications is _aggregation_. 
For example, a parent actor receives a large task from its caller actor, divides the task into smaller parts and sends the requests to its child actors. 
After receiving all responses the parent actor combines responses together and sends the result back to its caller actor. 
Various implementation of this pattern in Akka can be easily found, as well as their possible pitfalls. 
If _tell_ method is used, a reference to the caller actor should be saved when aggregation starts to send the result to it when aggregation finishes (because at that time the parent actor can process a task from another caller actor). 
If _ask_ method is used, the result should be combined from sequence of futures in non-blocking way.

Logging in asynchronous applications is mandatory for its correct DevOps (as a matter of fact, logging is as important as code for any application). 
Logging in Akka should be asynchronous and non-blocking to ensure that it has minimal performance impact. 
By default, Akka writes log messages to _stdout_, but for production is recommended to use SLF4J with Logback as backend. 
More details about logging in Akka can be found [here](http://doc.akka.io/docs/akka/current/scala/logging.html).

To make the next step, you can visit the GitHub [repository](https://github.com/aliakh/demo-scala-akka-spray/tree/master/demo-scala-akka-spray-aggregator) and browse through the third example. 
This is an aggregator - a REST server, which by-turn makes some REST client calls and combines their responses together to fulfill its server request. 
In this part you can get to know how to:

- make aggregation in Akka with _tell_ and _ask_ methods
- write application and Akka log messages in asynchronous and non-blocking way with [Logback AsyncAppender](http://logback.qos.ch/manual/appenders.html#AsyncAppender)
- load custom application settings from configuration file with [Akka Extensions](http://doc.akka.io/docs/akka/current/scala/extending-akka.html)
- use Scala companion objects to create actors in the [recommended](http://doc.akka.io/docs/akka/current/scala/actors.html#Recommended_Practices) way

[To be continued.](https://github.com/aliakh/demo-scala-akka-spray/tree/master/demo-scala-akka-spray-supervisor)