bigdata-rest-scala
==================

A rest client abstraction library for the bigdata rdf db for scala

Requirements:
[Bigdata NanoSparqlServer](http://sourceforge.net/apps/mediawiki/bigdata/index.php?title=NanoSparqlServer)

Notable Dependencies:
+ [Jerkson](https://github.com/codahale/jerkson/)
+ [Grizzled slf4j](http://software.clapper.org/grizzled-slf4j/)
+ [Databinder Dispatch](http://dispatch.databinder.net/Dispatch.html)
+ [Sesame](http://www.openrdf.org/index.jsp)

If you run in to PermGen space issues run sbt with the following command line:

>$ JAVA_OPTS="-XX:MaxPermSize=256M -Xmx512M" sbt

Or add those to the default options in the sbt launcher script.

Notes:

If you run the tests the RestSpec.scala:"insert an rdf file" test will insert the uniprot locations.rdf file (include in src/test/resources) into your bigdata instance.

You may have to edit RestSpec to match your test bigdata instance.
