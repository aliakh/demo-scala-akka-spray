# demo-scala-akka-spray-aggregator

REST services with Scala, Akka, Spray: Part 3

One of the common patterns in actor-based applications is aggregation. For example, a parent actor receives a large task from its caller actor, divides the task into smaller parts and sends the requests to its child actors. After receiving all responses the parent actor combines responses together and sends the result back to its caller actor. Various implementation of this pattern in Akka can be easily found, as well as their possible pitfalls. If tell method is used, a reference to the caller actor should be saved when aggregation starts to send the result to it when aggregation finishes (because at that time the parent actor can process a task from another caller actor). If ask method is used, the result should be combined from sequence of futures in non-blocking way.

Logging in asynchronous applications is mandatory for its correct DevOps (as a matter of fact, logging is as important as code for any application). Logging in Akka should be asynchronous and non-blocking to ensure that it has minimal performance impact. By default, Akka writes log messages to stdout, but for production is recommended to use SLF4J with Logback as backend. More details about logging in Akka can be found here.

To make the next step, you can visit the GitHub repository and browse through the third example. This is an aggregator - a REST server, which by-turn makes some REST client calls and combines their responses together to fulfill its server request. In this part you can get to know how to:

* make aggregation in Akka with tell and ask methods
* write application and Akka log messages in asynchronous and non-blocking way with Logback AsyncAppender
* load custom application settings from configuration file with Akka Extensions
* use Scala companion objects to create actors in the recommended way

To be continued.
