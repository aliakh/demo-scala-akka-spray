# demo-scala-akka-spray-supervisor

REST services with Scala, Akka, Spray: Part 4

Another basic idea of actor-based applications is supervision. That means, an actor gets tasks from its supervisor (parent), divides the tasks into smaller parts and sends the parts to its subordinates (children). It’s worth noting that an actor can have only one supervisor. The responsibility of the actor isn’t only to divide the tasks and to combine the solutions, but also to handle the subordinates’ failures. An actor has supervision strategy that decides what action it should do with the failed subordinate, depends on the type of the failure (exception): to ignore the failure, to restart the subordinate, to re-create the subordinate, or to escalate the failure to the supervisor. And that action is executed not only on the subordinate, but also on all its sub-subordinates as well.
 
Besides supervision described above, each actor can process monitoring on any other actor. Because of the nature of actors, the only available information is the state change of the monitored actor from alive to dead. Monitoring is used to connect one actor to another so the observer can react to the observable’s termination, in contrast to supervision which reacts to subordinate’s failure.

They say, such self-maintained hierarchical actors structures allow to build highly-available, fault-tolerant applications that work for years without restarts. More details about supervision and monitoring in Akka can be found [here](http://doc.akka.io/docs/akka/current/general/supervision.html).

To make the next step, you can visit the GitHub repository and browse through the fourth example. This is a supervisor – a REST-server, which uses both supervision and monitoring to recover from randomly-driven subordinates’ failures. In this part you can get to know how to:

* create a pool of subordinates
* supervise the subordinates with supervisorStrategy method
* monitor the subordinates with watch method and Terminated message
* resume tasks processing after subordinate’s restart or re-creation
