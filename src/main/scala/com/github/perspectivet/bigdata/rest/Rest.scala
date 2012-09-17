package com.github.perspectivet.bigdata.rest

import com.codahale.jerkson._
import dispatch._
import grizzled.slf4j.Logger

import scala.collection._
import scala.collection.JavaConverters._

//imported to use the Callable below to folow the bigdata example.
//better ways exist in scala
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.{List => JList,LinkedList => JLinkedList}

import java.io.{CharArrayReader, CharArrayWriter, StringReader}
import java.io.{File,FileReader}
import java.net.URLEncoder

import org.openrdf.rio.RDFFormat
import org.openrdf.model.{Literal,Resource,Statement,URI,Value}
import org.openrdf.model.impl.{StatementImpl,URIImpl}
import org.openrdf.repository.{Repository,RepositoryConnection,RepositoryException,RepositoryResult}
import org.openrdf.query.TupleQueryResult

import org.apache.http.client.HttpClient
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.impl.client.DefaultHttpClient

import com.bigdata.rdf.sail.webapp.client.DefaultClientConnectionManagerFactory
import com.bigdata.rdf.sail.webapp.client.RemoteRepository
import com.bigdata.rdf.sail.webapp.client.RemoteRepository._
import com.bigdata.striterator.ICloseableIterator
import com.bigdata.rdf.sail.webapp.client._

class Rest(val restUrl:String) {
  val log = Logger(classOf[Rest])

  val executor = Executors.newCachedThreadPool()

  val ccm = DefaultClientConnectionManagerFactory.getInstance()
                    .newInstance()

  val httpClient = new DefaultHttpClient(ccm)

  val repo = new RemoteRepository(
                    restUrl, httpClient, executor)

  /*
  val namespace = "kb";

  val objMgr = new NanoSparqlObjectManager(repo,
                    namespace)
  */

  val PREFIX_STMT = "PREFIX %s"
  val INSERT_NTRIPLES_STMT = """
  %s
  INSERT DATA
  { 
    %s
  }
  """
  val INSERT_RDFXML_STMT = """
  INSERT DATA
  { 
    %s
  }
  """
  def sparql(query:String):TupleQueryResult = {
    val pq = repo.prepareTupleQuery(query)
    pq.evaluate()
  }

  def getSubjectDocument(subject:Resource,prefixes:List[String]):Document = {
    val queryPrefix = prefixes.map { "PREFIX " + _ } mkString("","\n","\n") 
    val query = queryPrefix + ("SELECT ?p ?o WHERE { <%s> ?p ?o }" format subject.stringValue)

    log.debug(query)
    val results = sparql(query)
    RDFDocUtils.tuplesToDocument(results,subject,"p","o")
  }

  def putFile(filePath:String,format:RDFFormat):Long = {
    val addFile = new AddOp(new File(filePath),format)
    val mutationCount = repo.add(addFile)
    log.debug("added %s records" format mutationCount)
    mutationCount
  }

  def putAdd(op:AddOp):Long = {
    val mutationCount = repo.add(op)
    log.debug("added %s records" format mutationCount)
    mutationCount
  }

  def putRemove(op:RemoveOp):Long = {
    val mutationCount = repo.remove(op)
    log.debug("removed %s records" format mutationCount)
    mutationCount
  }

  def putUpdate(rop:RemoveOp,aop:AddOp):Long = {
    val mutationCount = repo.update(rop,aop)
    log.debug("removed %s records" format mutationCount)
    mutationCount
  }

  def putN3String(rdf:String):Long = {
    putString(rdf,RDFFormat.N3)
  }
  def putString(rdf:String,format:RDFFormat):Long = {
    val addFile = new AddOp(rdf.getBytes("UTF-8"),format)
    val mutationCount = repo.add(addFile)
    log.debug("added %s records" format mutationCount)
    mutationCount
  }

  def putNTriples(prefixList:List[String],rdf:String):String = {

    log.debug("inserting rdf:" + rdf)

    val prefix = prefixList map { PREFIX_STMT format _ } mkString ("\n")
    val query = INSERT_NTRIPLES_STMT format (prefix,rdf)

    log.debug("query:" + query)
    "no result"
  }

  def putRDFXML(rdfxml:String):String = {

    log.debug("inserting rdfxml:\n" + rdfxml)

    val query = INSERT_RDFXML_STMT format rdfxml

    "no result"
  }

  def shutdown = {
    //http.shutdown
  }
}


