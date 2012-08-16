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

  
  "The Rest object should" should {

    "insert an rdf triple" in {
      val rest = new Rest("http://localhost:8080/bigdata/sparql")
      val prefixList = List("dc: <http://purl.org/dc/elements/1.1/>")
      val rdf = """      
      <http://perspectivet.github.com/ontology/article/1> dc:title "a great article" ;
      dc:creator "Perspectivet" .
      """
      
      val results = rest.putRdf(prefixList,rdf)
      println("results:" + results)
      rest.shutdown
      success
    }

    "execute a sparql query" in {
      val rest = new Rest("http://localhost:8080/bigdata/sparql")
      val query = """
      SELECT ?s ?p ?o WHERE { ?s ?p ?o }
      """

      val results = rest.sparql(query)
      println("results:" + results)
      rest.shutdown
      success
    }
  }
}
