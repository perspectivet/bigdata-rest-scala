package com.github.perspectivet.bigdata.rest

import com.codahale.jerkson._
import dispatch._
import grizzled.slf4j.Logger

import java.io.{CharArrayReader, CharArrayWriter, FileReader, StringReader}
import java.net.URLEncoder

import org.scardf._
import org.scardf.jena.JenaGraph

case class Head(val vars:List[String])
case class Result(val `type`:String, val datatype:Option[String], val value:String)
case class Bindings(val bindings:List[Map[String,Result]])
case class ResultSet(val head:Head, val results:Bindings) {
  def columnNames = head.vars
  def columns = results.bindings
}

class Rest(val restUrl:String) {
  val log = Logger(classOf[Rest])
  val sparqlUrl = url(restUrl) 
  val http = new Http()

  val PREFIX_STMT = "PREFIX %s"
  val INSERT_STMT = """
  %s
  INSERT DATA
  { 
    %s
  }
  """

  def sparql(query:String):ResultSet = {
    log.debug("executing query:" + query)

    val result = http(sparqlUrl <:< Map("Accept" -> "application/sparql-results+json") <<? Map("query" -> query)  as_str)
    
    log.debug("query result:" + result)
    Json.parse[ResultSet](result)
  }

  def putGraph(prefixList:List[String],g:Graph):String = {
    val s = new Serializator( NTriple )
    val w = new CharArrayWriter
    s.write(g, w)

    putRdf(prefixList,w.toString)
  }

  def putRdf(prefixList:List[String],rdf:String):String = {

    log.debug("inserting rdf:" + rdf)

    val prefix = prefixList map { PREFIX_STMT format _ } mkString ("\n")
    val query = INSERT_STMT format (prefix,rdf)

    val result = http(sparqlUrl <:< Map("Accept" -> "application/sparql-results+json") << Map("update" -> query)  as_str)

    log.debug("insert result:" + result)
    result
  }

  def shutdown = {
    http.shutdown
  }
}
