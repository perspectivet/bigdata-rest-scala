package com.github.perspectivet.bigdata.rest

import com.codahale.jerkson._
import dispatch._
import grizzled.slf4j.Logger

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
import org.openrdf.model.{Literal,Resource,Statement,URI}
import org.openrdf.repository.{Repository,RepositoryConnection,RepositoryException,RepositoryResult}
import org.openrdf.query.TupleQueryResult

import org.apache.http.client.HttpClient
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.impl.client.DefaultHttpClient

import com.bigdata.gom.gpo.IGPO
import com.bigdata.gom.om.IObjectManager
import com.bigdata.gom.om.NanoSparqlObjectManager
import com.bigdata.rdf.sail.webapp.client.DefaultClientConnectionManagerFactory
import com.bigdata.rdf.sail.webapp.client.RemoteRepository
import com.bigdata.rdf.sail.webapp.client.RemoteRepository._
import com.bigdata.striterator.ICloseableIterator

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

  def getSubjectDocument(subject:String,prefixes:List[String]):Document = {
    val queryPrefix = prefixes.map { "PREFIX " + _ } mkString("","\n","\n") 
    val query = queryPrefix + ("SELECT ?p ?o WHERE { %s ?p ?o }" format subject)

    log.debug(query)
    val results = sparql(query)
    val bindings = results.getBindingNames.asScala
    log.debug("returned bindings for " + bindings.mkString(","))
    
    val poList = new JLinkedList[PredicateObject]()
 
    while(results.hasNext) {
      val bindingSet = results.next
      poList.add(
	new PredicateObject(
	  bindingSet.getValue("p").stringValue,
	  bindingSet.getValue("o").stringValue)
      )
    }

    new Document(subject,poList)
  }

  def sparql(query:String):TupleQueryResult = {
    val pq = repo.prepareTupleQuery(query)
    pq.evaluate()
  }

/*
  def resultToString(results:TupleQueryResult):String = {
    val bindings = results.getBindingNames.asScala
    log.debug("returned bindings for " + bindings.mkString(","))
    
    while(results.hasNext) {
      val bindingSet = results.next
      val resMsg =  bindings.map { bindingSet.getValue(_).stringValue }.mkString("result tuple:",",",".")
      log.debug(resMsg)
      log.debug("next binding:" + results.next.toString)
    }
  }
*/

  def putFile(filePath:String,format:RDFFormat):Long = {
    val addFile = new AddOp(new File(filePath),format)
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

/*
  def updateDocument(a1:Document,a2:Document):Boolean = {
    
  }
*/

  def shutdown = {
    //http.shutdown
  }
}


