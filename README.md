bigdata-rest-scala
==================

A rest client for the bigdata rdf db for scala

Notable Dependencies:
+ [Jerkson](https://github.com/codahale/jerkson/)
+ [Grizzled slf4j](http://software.clapper.org/grizzled-slf4j/)
+ [Perspectivet's fork of Scardf](https://github.com/perspectivet/scardf) 
+ [Databinder Dispatch](http://dispatch.databinder.net/Dispatch.html)
+ [Jena](http://jena.apache.org/)

If you run in to PermGen space issues run sbt with the following command line:

>$ JAVA_OPTS="-XX:MaxPermSize=256M -Xmx512M" sbt

Or add those to the default options in the sbt launcher script.

