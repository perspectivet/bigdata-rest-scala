package com.github.perspectivet.bigdata.rest

import scala.collection._
import scala.collection.JavaConverters._

import org.openrdf.model.{Literal,Resource,Statement,URI,Value}
import org.openrdf.model.impl.{StatementImpl,URIImpl}
import org.openrdf.query.TupleQueryResult

import com.bigdata.rdf.sail.webapp.client.RemoteRepository._
import java.util.{List => JList,LinkedList => JLinkedList,Vector => JVector}

import grizzled.slf4j.Logger

object RDFDocUtils {
  val log = Logger(classOf[Rest])

  def tuplesToDocument(tuples:TupleQueryResult,subject:Resource,predBinding:String,objBinding:String):Document = {
    val bindings = tuples.getBindingNames.asScala
    log.debug("returned bindings for " + bindings.mkString(","))
    
    val poList = new JLinkedList[PredicateObject]()
 
    while(tuples.hasNext) {
      val bindingSet = tuples.next
      poList.add(
	new PredicateObject(
	  new URIImpl(bindingSet.getValue(predBinding).stringValue),
	  bindingSet.getValue(objBinding))
      )
    }
    new Document(subject,poList)
  }

  def addOp(s:Resource,p:URI,o:Value):AddOp = {
    val list = new JVector[Statement](1)
    list.add(new StatementImpl(s,p,o))
    new AddOp(list)
  }

  def calcAddOp(a1:Document, a2:Document):Option[AddOp] = {
    if(a1.subject != a2.subject) {
      None
    } else {
      val preAdd = a2.properties.asScala.toSet
      val toAdd = preAdd diff a1.properties.asScala.toSet
      val addList:Set[Statement] = toAdd map { a => 
	new StatementImpl (a1.subject,
			   a.pred,
			   a.obj) }
      addList.headOption.map { a => new AddOp(addList.asJava) }
    }
  }

  def removeOp(s:Resource,p:URI,o:Value):RemoveOp = {
    val list = new JVector[Statement](1)
    list.add(new StatementImpl(s,p,o))
    new RemoveOp(list)
  }

  def calcRemoveOp(a1:Document, a2:Document):Option[RemoveOp] = {
    if(a1.subject != a2.subject) {
      None
    } else {
      val preDelete = a1.properties.asScala.toSet
      val toDelete = preDelete diff a2.properties.asScala.toSet
      val removeList:Set[Statement] = toDelete map { a => 
	new StatementImpl (a1.subject,
			   a.pred,
			   a.obj) }
      removeList.headOption.map { a => new RemoveOp(removeList.asJava) }
    }
  }

  def resultToString(results:TupleQueryResult):String = {
    val bindings = results.getBindingNames.asScala
    log.debug("returned bindings for " + bindings.mkString(","))

    var retString = ""
    while(results.hasNext) {
      val bindingSet = results.next
      val resMsg =  bindings.map { b => b + ":" + bindingSet.getValue(b).stringValue }.mkString("\n",",",".")
      retString = retString + resMsg
    }

    retString
  }

  def bindingToCollection(binding:String,results:TupleQueryResult):Seq[Value] = {
    if( ! results.getBindingNames.contains(binding)) {
      Nil
    } else {
      val list = mutable.ListBuffer[Value]()
      while(results.hasNext) {
	list += results.next.getBinding(binding).getValue
      }

      list
    }
  }
}
