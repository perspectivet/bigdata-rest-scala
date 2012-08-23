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
  val sparqlUrl = "http://localhost:8080/bigdata/sparql"
  
  "The Rest object should" should {

    "insert an rdf triple" in {
      val rest = new Rest(sparqlUrl)
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
      val rest = new Rest(sparqlUrl)
      val query = """
      SELECT ?s ?p ?o WHERE { ?s ?p ?o }
      """

      val results = rest.sparql(query)
      println("results:" + results)
      rest.shutdown
      success
    }

    "insert an rdf chunk" in {
      val rdf = """
      <rdf:RDF xmlns=\"http://purl.uniprot.org/core/\" xmlns:dcterms=\"http://purl.org/dc/terms/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\" xmlns:bibo=\"http://purl.org/ontology/bibo/\" xmlns:foaf=\"http://xmlns.com/foaf/0.1/\">
      <owl:Ontology rdf:about="">
      <owl:imports rdf:resource=\"http://purl.uniprot.org/core/\"/>
      </owl:Ontology>
      <rdf:Description rdf:about=\"http://purl.uniprot.org/go/0000023\">
    <rdf:type rdf:resource=\"http://purl.uniprot.org/core/Concept\"/>
    <rdfs:label>maltose metabolic process</rdfs:label>
    <rdfs:label>malt sugar metabolic process</rdfs:label>
    <rdfs:label>malt sugar metabolism</rdfs:label>
    <rdfs:label>maltose metabolism</rdfs:label>
    <rdfs:comment>The chemical reactions and pathways involving the disaccharide maltose (4-O-alpha-D-glucopyranosyl-D-glucopyranose), an intermediate in the catabolism of glycogen and starch.</rdfs:comment>
    <rdfs:subClassOf rdf:resource=\"http://purl.uniprot.org/go/0005984\"/>
    <skos:narrower rdf:resource=\"http://purl.uniprot.org/go/0000025\"/>
    <skos:narrower rdf:resource=\"http://purl.uniprot.org/go/0000024\"/>
    <skos:exactMatch rdf:resource=\"http://www.geneontology.org/go#GO:0000023\"/>
      </rdf:Description>
      </rdf:RDF>
      """

      val rest = new Rest(sparqlUrl)
      val prefixList = List("dc: <http://purl.org/dc/elements/1.1/>")
      val results = rest.putRdf(prefixList,rdf)
      println("results:" + results)
      rest.shutdown
      
    }
  }
}
