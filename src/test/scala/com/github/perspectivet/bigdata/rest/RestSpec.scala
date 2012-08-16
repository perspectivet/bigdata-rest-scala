package com.github.perspectivet.bigdata.rest

import org.specs2.mutable._

import org.scardf._
import org.scardf.jena.JenaGraph

object User extends Vocabulary( "http://perspectivet.github.com/user#" ) {
  val userName = propStr("userName")
  val firstName = propStr("firstName")
  val lastName = propStr("lastName")
}

object Comment extends Vocabulary( "http://perspectivet.github.com/ontology/comment#" ) {
  val creator = prop("creator")
  val createTime = prop("createTime")
  val contents = propStr("contents")
}

class RestSpec extends Specification {

    val rest = new Rest("http://localhost:8080/bigdata/sparql")
  
  "The Rest object should" should {

    "insert an rdf triple" in {
      val prefixList = List("dc: <http://purl.org/dc/elements/1.1/>")
      val rdf = """      
      <http://perspectivet.github.com/ontology/article/1> dc:title "a great article" ;
      dc:creator "Perspectivet" .
      """
      
      val results = rest.putRdf(prefixList,rdf)
      println("results:" + results)
      success
    }

    "execute a sparql query" in {
      val query = """
      SELECT ?s ?p ?o WHERE { ?s ?p ?o }
      """

      val results = rest.sparql(query)
      println("results:" + results)
      success
    }

    "shutdown" in {
      rest.shutdown
      success
    }
  }
}
